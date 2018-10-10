package com.xiyifen.controller;

import com.xiyifen.pojo.Users;
import com.xiyifen.pojo.bo.UserBO;
import com.xiyifen.pojo.vo.UsersVO;
import com.xiyifen.service.UserService;
import com.xiyifen.utils.FastDFSClient;
import com.xiyifen.utils.FileUtils;
import com.xiyifen.utils.IMoocJSONResult;
import com.xiyifen.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/u")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception {
        System.out.println("==========================");
        if (user.getUsername().trim().equals("") || user.getPassword().equals("")) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空...");

        }
//        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
//            return IMoocJSONResult.errorMsg("用户名和密码不能为空...");
//        }
        // 判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            userResult = userService.saveUser(user);
        }
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult, userVO);

        return IMoocJSONResult.ok(userVO);
    }

    /**
     * 用户头像上传
     *
     * @param userBO
     * @return
     */
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UserBO userBO) throws Exception {

        // 获取前端传过来的base64字符串，转为文件对象再上传
        String faceData = userBO.getFaceData();
        String userFacePath = "E:\\" + userBO.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, faceData);

        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

        //		"dhawuidhwaiuh3u89u98432.png"
//		"dhawuidhwaiuh3u89u98432_80x80.png"

        // 获取缩略图url
        String thump = "_80x80.";
        String arr[] =url.split("\\.");
        String thumpImgUrl=arr[0] + thump + arr[1];

        Users user=new Users();
        user.setId(userBO.getUserId());
        // 缩略图
        user.setFaceImage(thumpImgUrl);
        // 大图
        user.setFaceImageBig(url);

        Users result = userService.updateUserInfo(user);
        return IMoocJSONResult.ok(result);
    }
}
