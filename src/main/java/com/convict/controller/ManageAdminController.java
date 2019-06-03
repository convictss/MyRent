package com.convict.controller;

import com.alibaba.fastjson.JSONObject;
import com.convict.model.WebPath;
import com.convict.po.Admin;
import com.convict.service.AdminService;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import com.convict.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @Author Convict
 * @Date 2018/12/25 10:21
 */
@Controller
@RequestMapping("/manage")
public class ManageAdminController extends MaintainSessionList {

    private AdminService adminService;

    private HttpServletRequest request;

    @Autowired
    public ManageAdminController(AdminService adminService, HttpServletRequest request) {
        this.adminService = adminService;
        this.request = request;
    }

    /**
     * 登录
     * @param admin
     * @param userCaptcha
     * @return
     */
    @RequestMapping("/login")
    public String login(Admin admin, String userCaptcha) {
        admin.setPassword(CommonUtil.getMD5Value(admin.getPassword()));
        Admin curAdmin = adminService.queryAdmin(admin);
        String viewName;
        if (curAdmin == null) {
            autoUpdateSession(request, "errorTip", "用户名或密码错误！");
            viewName = "index";
        } else {
            String rightCaptcha = (String) request.getSession().getAttribute("rightCaptcha");
            if (userCaptcha.equals(rightCaptcha)) {
                autoUpdateSession(request, "curAdmin", curAdmin);
                viewName = "other/catalog";
            } else {
                autoUpdateSession(request, "errorTip", "验证码错误！");
                viewName = "index";
            }
        }
        return viewName;
    }

    /**
     * 登出
     * @param sessionStatus
     * @return
     */
    @RequestMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        request.getSession().invalidate();
        sessionStatus.setComplete();
        return "index";

    }

    /**
     * 修改用户信息
     * @param preAdmin
     * @param alreadyModifyUserPhoto
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyAdminMess")
    public JSONObject modifyAdminMess(Admin preAdmin, @RequestParam("alreadyModifyUserPhoto") Boolean alreadyModifyUserPhoto) {
        Admin curAdmin = (Admin) request.getSession().getAttribute("curAdmin");
        WebPath wp = SpringContextUtil.getBean("webPath");
        String webPath = wp.getPath();
        String userPhotoPath = webPath + "\\\\static\\\\image\\\\fix\\\\theme\\\\";
        // 如果修改了图片
        if (alreadyModifyUserPhoto) {
            // 删除原来的头像照片
            FileUtil.deleteFileByPath(userPhotoPath + curAdmin.getUserPhoto());

            File userPhotoPathDir = new File(userPhotoPath);
            for (String fileName : userPhotoPathDir.list()) {
                if (fileName.startsWith("userTemp")) {
                    // eg: userTemp.jpg 改成 user.jpg
                    File tempFile = new File(userPhotoPath + fileName);
                    File targetFile = new File(userPhotoPath + fileName.replace("Temp", ""));
                    tempFile.renameTo(targetFile);
                    // 修改curAdmin的userPhoto值
                    curAdmin.setUserPhoto(fileName.replace("Temp", ""));
                    break;
                }
            }
        }
        curAdmin.setNickname(preAdmin.getNickname());

        JSONObject jsonObject = new JSONObject();
        if (adminService.modifyAdminMess(curAdmin)) {
            autoUpdateSession(request, "curAdmin", curAdmin);
            jsonObject.put("status", 1);
            jsonObject.put("message", "修改成功！");
        } else {
            jsonObject.put("status", 0);
            jsonObject.put("message", "修改失败！");
        }
        return jsonObject;
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param preAdmin
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyPassword")
    public JSONObject modifyPassword(@RequestParam("oldPassword") String oldPassword, Admin preAdmin) {

        JSONObject jsonObject = new JSONObject();
        Admin curAdmin = (Admin) request.getSession().getAttribute("curAdmin");
        if (curAdmin.getPassword().equals(CommonUtil.getMD5Value(oldPassword))) {
            // 旧密码校验正确，设置新的传递过来的admin实例
            preAdmin.setAccount(curAdmin.getAccount());
            preAdmin.setPassword(CommonUtil.getMD5Value(preAdmin.getPassword()));
            if (adminService.modifyPassword(preAdmin)) {
                autoUpdateSession(request, "curAdmin", adminService.queryAdmin(preAdmin));
                jsonObject.put("status", 1);
                jsonObject.put("message", "修改成功！请牢记您的新密码！");
            } else {
                jsonObject.put("status", 0);
                jsonObject.put("message", "修改失败，系统异常。");
            }
        } else {
            jsonObject.put("status", 2);
            jsonObject.put("message", "旧密码错误！");
        }
        return jsonObject;
    }

    // 预上传头像
    @ResponseBody
    @RequestMapping("/preUploadUserPhoto")
    public JSONObject preUploadUserPhoto(@RequestParam("userPhoto")MultipartFile file) throws IOException {
        WebPath wp = SpringContextUtil.getBean("webPath");
        String webPath = wp.getPath()+ "\\\\static\\\\image\\\\fix\\\\theme\\\\";
        String origin = file.getOriginalFilename();

        // eg: userTemp.jpg
        String tempName = "userTemp" + origin.substring(origin.lastIndexOf("."));

        // eg: D:\\image\\fix\\theme\\userTemp.jpg
        webPath += tempName;

        // 二进制流保存为文件
        FileUtil.saveFileByBytes(file.getBytes(), webPath);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tempName", tempName);
        return jsonObject;
    }
}
