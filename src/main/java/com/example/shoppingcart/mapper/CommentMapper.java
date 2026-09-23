package com.example.shoppingcart.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    @Select("SELECT c.*, u.username FROM comment c LEFT JOIN users u ON c.user_id = u.id WHERE c.product_id = #{productId} ORDER BY c.create_time DESC")
    List<Map<String, Object>> findByProductId(@Param("productId") Integer productId);

    @Insert("INSERT INTO comment (user_id, product_id, order_id, content, star, create_time) VALUES (#{userId}, #{productId}, #{orderId}, #{content}, #{star}, datetime('now','localtime'))")
    void insert(@Param("userId") Integer userId, @Param("productId") Integer productId,
                @Param("orderId") Integer orderId, @Param("content") String content, @Param("star") Integer star);
}
