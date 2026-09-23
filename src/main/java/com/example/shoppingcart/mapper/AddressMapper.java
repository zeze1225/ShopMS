package com.example.shoppingcart.mapper;

import com.example.shoppingcart.entity.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {

    @Select("SELECT * FROM address WHERE user_id = #{userId}")
    List<Address> findByUserId(Integer userId);

    @Select("SELECT * FROM address WHERE user_id = #{userId} AND is_default = 1 LIMIT 1")
    Address findDefaultByUserId(Integer userId);

    @Select("SELECT * FROM address WHERE id = #{id}")
    Address findById(Integer id);

    @Insert("INSERT INTO address (user_id, receiver, phone, address, is_default) VALUES (#{userId}, #{receiver}, #{phone}, #{address}, #{isDefault})")
    void insert(Address addr);

    @Update("UPDATE address SET receiver=#{receiver}, phone=#{phone}, address=#{address}, is_default=#{isDefault} WHERE id=#{id}")
    void update(Address addr);

    @Delete("DELETE FROM address WHERE id = #{id}")
    void deleteById(Integer id);
}
