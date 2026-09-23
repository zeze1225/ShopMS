package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Collect;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectMapper {

    @Select("SELECT * FROM collect WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Collect> findByUserId(Integer userId);

    @Insert("INSERT INTO collect (user_id, product_id, create_time) VALUES (#{userId}, #{productId}, datetime('now','localtime'))")
    void insert(Collect collect);

    @Delete("DELETE FROM collect WHERE id = #{id}")
    void deleteById(Integer id);

    @Delete("DELETE FROM collect WHERE user_id = #{userId} AND product_id = #{productId}")
    void deleteByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
