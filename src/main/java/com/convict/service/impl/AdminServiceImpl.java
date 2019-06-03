package com.convict.service.impl;

import com.convict.dao.AdminDao;
import com.convict.po.Admin;
import com.convict.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Convict
 * @Date 2018/12/25 10:48
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Admin queryAdmin(Admin admin) {
        return adminDao.selectAdmin(admin);
    }

    @Override
    public Boolean modifyAdminMess(Admin admin) {
        return adminDao.updateAdminMess(admin);
    }

    @Override
    public Boolean modifyPassword(Admin admin) {
        return adminDao.updatePassword(admin);
    }

}
