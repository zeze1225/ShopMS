package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> findAll() {
        return productMapper.findAll();
    }

    public List<Product> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productMapper.findAll();
        }
        return productMapper.search(keyword);
    }

    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

    public void add(Product product) {
        productMapper.insert(product);
    }

    public void update(Product product) {
        productMapper.update(product);
    }

    public void delete(Integer id) {
        productMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            productMapper.deleteByIds(ids);
        }
    }
}
