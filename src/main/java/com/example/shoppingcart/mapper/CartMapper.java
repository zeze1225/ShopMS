package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByUserId(@Param("userId") Integer userId);

    Cart findByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    void insert(Cart cart);

    void updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

    void deleteById(@Param("id") Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);

    void clearByUserId(@Param("userId") Integer userId);
}
