package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BrandMapper {

    @Select("SELECT * FROM brand ORDER BY id")
    List<Brand> findAll();

    @Select("SELECT * FROM brand WHERE id = #{id}")
    Brand findById(Integer id);
}
