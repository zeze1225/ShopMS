package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    List<Product> search(@Param("keyword") String keyword);

    Product findById(@Param("id") Integer id);

    void insert(Product product);

    void update(Product product);

    void deleteById(@Param("id") Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);
}
