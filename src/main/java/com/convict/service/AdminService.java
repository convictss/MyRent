package com.convict.service;

import com.convict.po.Admin;

/**
 * @Author Convict
 * @Date 2018/12/25 10:28
 */
public interface AdminService {

    Admin queryAdmin(Admin admin);

    Boolean modifyAdminMess(Admin admin);

    Boolean modifyPassword(Admin admin);

}
