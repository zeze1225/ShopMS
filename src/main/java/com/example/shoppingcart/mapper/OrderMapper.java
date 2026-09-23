package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findByUserId(@Param("userId") Integer userId);

    List<Order> searchByUserId(@Param("userId") Integer userId, @Param("keyword") String keyword);

    Order findById(@Param("id") Integer id);

    void insert(Order order);

    void updateStatus(@Param("id") Integer id, @Param("status") String status);

    void deleteById(@Param("id") Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);
}
