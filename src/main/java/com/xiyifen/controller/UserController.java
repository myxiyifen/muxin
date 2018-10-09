package com.xiyifen.controller;

import com.xiyifen.pojo.Users;
import com.xiyifen.pojo.vo.UsersVO;
import com.xiyifen.service.UserService;
import com.xiyifen.utils.IMoocJSONResult;
import com.xiyifen.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/u")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception{
        System.out.println("==========================");
        if (user.getUsername().trim().equals("")||user.getPassword().equals("")){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空...");

        }
//        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
//            return IMoocJSONResult.errorMsg("用户名和密码不能为空...");
//        }
        // 判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist){
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("用户名或密码不正确...");
            }
        }else {
            userResult = userService.saveUser(user);
        }
        UsersVO userVO=new UsersVO();
        BeanUtils.copyProperties(userResult,userVO);

        return IMoocJSONResult.ok(userVO);
    }
}
