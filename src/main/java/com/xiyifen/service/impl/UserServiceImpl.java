package com.xiyifen.service.impl;

import com.xiyifen.mapper.UsersMapper;
import com.xiyifen.pojo.Users;
import com.xiyifen.service.UserService;
import com.xiyifen.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);

        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {

        Example userExample=new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",pwd);
        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }

    @Override
    public Users saveUser(Users user) throws Exception{
        String userId = sid.nextShort();

        user.setNickname(user.getUsername());
        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
        // todo 为每个用户生成唯一的二维码
        user.setQrcode("");
        user.setId(userId);
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserById(String userId){
        return usersMapper.selectByPrimaryKey(userId);
    }



}
