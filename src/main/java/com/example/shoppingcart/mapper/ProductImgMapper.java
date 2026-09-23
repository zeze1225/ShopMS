package com.example.shoppingcart.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductImgMapper {

    @Select("SELECT * FROM product_img WHERE product_id = #{productId} ORDER BY sort_order")
    List<Map<String, Object>> findByProductId(@Param("productId") Integer productId);

    @Insert("INSERT INTO product_img (product_id, img_url, sort_order) VALUES (#{productId}, #{imgUrl}, #{sortOrder})")
    void insert(@Param("productId") Integer productId, @Param("imgUrl") String imgUrl, @Param("sortOrder") Integer sortOrder);

    @Delete("DELETE FROM product_img WHERE product_id = #{productId}")
    void deleteByProductId(@Param("productId") Integer productId);
}
