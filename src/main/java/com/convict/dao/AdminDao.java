package com.convict.dao;

import com.convict.po.Admin;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Author Convict
 * @Date 2018/12/25 10:23
 */
@Repository
public interface AdminDao {

    @Select("select * from admin where account = #{account} and password = #{password}")
    Admin selectAdmin(Admin admin);

    Boolean updateAdminMess(Admin admin);

    @Update("update admin set password = #{password} where account = #{account}")
    Boolean updatePassword(Admin admin);
}
