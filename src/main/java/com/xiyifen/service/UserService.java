package com.xiyifen.service;

import com.xiyifen.pojo.Users;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 用户名存在，进行登录
     * @param username
     * @param pwd
     * @return
     */
    public Users queryUserForLogin(String username, String pwd);

    public Users saveUser(Users user) throws Exception;

    /***
     * 修改用户记录
     * @param user
     * @return
     */
    public Users updateUserInfo(Users user);



}
