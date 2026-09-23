package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Category;
import com.example.shoppingcart.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    public List<Category> search(String mingcheng) {
        if (mingcheng == null || mingcheng.trim().isEmpty()) {
            return categoryMapper.findAll();
        }
        return categoryMapper.findByName(mingcheng);
    }

    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public void update(Category category) {
        categoryMapper.update(category);
    }

    public void delete(Integer id) {
        categoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            categoryMapper.deleteByIds(ids);
        }
    }
}
