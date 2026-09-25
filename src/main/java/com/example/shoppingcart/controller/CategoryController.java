package com.example.shoppingcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.shoppingcart.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 支持搜索，不填关键字就返回全部
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(String guanjianci, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        List<Category> list;
        if (guanjianci != null && !guanjianci.trim().isEmpty()) {
            list = categoryService.search(guanjianci);
        } else {
            list = categoryService.findAll();
        }
        result.put("code", 0);
        result.put("data", list);
        return result;
    }

    @RequestMapping("/get")
    @ResponseBody
    public Map<String, Object> get(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Category category = categoryService.findById(id);
        if (category != null) {
            result.put("code", 0);
            result.put("data", category);
        } else {
            result.put("code", 1);
            result.put("msg", "该分类不存在");
        }
        return result;
    }

    // 分类名不能重复（但目前没做校验，有个坑）
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(Category category, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        categoryService.add(category);
        result.put("code", 0);
        result.put("msg", "添加成功");
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(Category category, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        try {
            categoryService.update(category);
            result.put("code", 0);
            result.put("msg", "修改成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "修改失败");
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        // 删除分类不会检查是否有商品引用，可能出问题
        categoryService.delete(id);
        result.put("code", 0);
        result.put("msg", "删除成功");
        return result;
    }

    @PostMapping("/deleteBatch")
    @ResponseBody
    public Map<String, Object> deleteBatch(@RequestParam("ids") List<Integer> ids, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        try {
            categoryService.deleteBatch(ids);
            result.put("code", 0);
            result.put("msg", "批量删除成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除出错：" + e.getMessage());
        }
        return result;
    }
}
