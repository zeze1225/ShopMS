package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Insert("INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (#{orderId}, #{productId}, #{quantity}, #{price})")
    void insert(OrderItem item);

    @Select("SELECT oi.*, p.name AS productName FROM order_item oi LEFT JOIN product p ON oi.product_id = p.id WHERE oi.order_id = #{orderId}")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);
}
