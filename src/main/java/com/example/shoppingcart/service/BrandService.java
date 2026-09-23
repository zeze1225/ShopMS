package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Brand;
import com.example.shoppingcart.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    public Brand findById(Integer id) {
        return brandMapper.findById(id);
    }
}
