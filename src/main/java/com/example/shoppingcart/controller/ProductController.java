package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.Product;
import org.springframework.stereotype.Controller;
import com.example.shoppingcart.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 这里没有做分页，数据多了会卡，暂时够用
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(String keyword, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        // System.out.println("搜索关键字: " + keyword);
        List<Product> list = productService.search(keyword);
        result.put("code", 0);
        result.put("data", list);
        return result;
    }

    @RequestMapping("/get")
    @ResponseBody
    public Map<String, Object> get(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (id == null) {
            result.put("code", 1);
            result.put("msg", "参数错误");
            return result;
        }
        Product product = productService.findById(id);
        if (product != null) {
            result.put("code", 0);
            result.put("data", product);
        } else {
            result.put("code", 1);
            result.put("msg", "该商品不存在");
        }
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(Product product, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        // 前端可能价格和库存没填，这里做个兜底
        if (product.getPrice() == null) product.setPrice(0.0);
        if (product.getStock() == null) product.setStock(0);
        try {
            productService.add(product);
            result.put("code", 0);
            result.put("msg", "添加商品成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "添加商品失败，请重试");
            System.out.println("添加商品出错：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(Product product, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        productService.update(product);
        result.put("code", 0);
        result.put("msg", "修改成功");
        return result;
    }

    // 删除商品，不管库存多少都能删，先这样写
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        try {
            productService.delete(id);
            result.put("code", 0);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    // TODO: 这里应该加事务，防止部分删除成功
    @PostMapping("/deleteBatch")
    @ResponseBody
    public Map<String, Object> deleteBatch(@RequestParam("ids") List<Integer> ids, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        if (ids == null || ids.isEmpty()) {
            result.put("code", 1);
            result.put("msg", "请选择要删除的商品");
            return result;
        }
        productService.deleteBatch(ids);
        result.put("code", 0);
        result.put("msg", "批量删除成功");
        return result;
    }
}
