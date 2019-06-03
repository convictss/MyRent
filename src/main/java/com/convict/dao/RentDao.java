package com.convict.dao;

import com.convict.dto.PageInfDto;
import com.convict.dto.RentDto;
import com.convict.po.Rent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentDao {

    /**
     * 新增模块
     */
    @Insert("insert into ${tableName}(link, title, region, community, address, area, house_type, floor, price, deposit_type, contact, phone_number, traffic_route, publish_date) " +
            "values(#{r.link}, #{r.title}, #{r.region}, #{r.community}, #{r.address}, #{r.area}, #{r.houseType}, #{r.floor}, #{r.price}, #{r.depositType}, #{r.contact}, #{r.phoneNumber}, #{r.trafficRoute}, #{r.publishDate})")
    @Options(useGeneratedKeys = true, keyProperty = "r.id", keyColumn = "id")
    void insert(@Param("r") Rent rent, @Param("tableName") String tableName);

    void insertImages(@Param("dto") RentDto dto);

    /**
     * 更新模块
     */
    @Update("update ${tableName} set link = #{r.link}, title = #{r.title}, region = #{r.region}, community = #{r.community}, " +
            "address = #{r.address}, area = #{r.area}, house_type = #{r.houseType}, floor = #{r.floor}, price = #{r.price}, " +
            "deposit_type = #{r.depositType}, contact = #{r.contact}, phone_number = #{r.phoneNumber}, traffic_route = #{r.trafficRoute}, " +
            "publish_date = #{r.publishDate} " +
            "where id = #{r.id}")
    boolean update(@Param("r") Rent rent, @Param("tableName") String tableName);

    /**
     * 删除模块
     */
    @Delete("delete from ${tableName} where id=#{id}")
    void delete(RentDto dto);

    void deleteBatch(@Param("ids") List<Integer> ids, @Param("tableName") String tableName);

    @Delete("delete from ${tableName}")
    void deleteAll(@Param("tableName") String tableName);

    /**
     * 查询模块
     */
    @Select("select * from ${tableName} limit #{offset}, #{pageSize}")
    List<Rent> selectPageRentExcImages(PageInfDto dto);// 根据页码查数据，不查照片

    List<Rent> selectPageRentIncImages(PageInfDto dto);// 根据页码查数据，查照片，暂时没用到

    Rent selectRentIncImagesById(RentDto dto);// 根据id查数据，包含图片

    Rent selectRentExcImagesById(RentDto dto);// 根据id查数据，不包含图片

    List<Rent> selectRentExcImagesByKey(@Param("r") Rent rent, @Param("tableName") String tableName);

    @Select("select count(*) from ${tableName}")
    Integer selectAllCountByTableName(@Param("tableName") String tableName);

    @Select("select count(*) from ${tableName} where link = #{link}")
    Integer selectCountByLink(@Param("link") String link, @Param("tableName") String tableName);

    @Select("select image from ${tableName}_image where rent_id = #{id}")
    List<String> selectImagesByRentId(RentDto dto);
}
