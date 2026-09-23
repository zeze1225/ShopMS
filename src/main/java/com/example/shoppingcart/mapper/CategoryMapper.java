package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    List<Category> findByName(@Param("name") String name);

    Category findById(@Param("id") Integer id);

    void insert(Category category);

    void update(Category category);

    void deleteById(@Param("id") Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);
}
