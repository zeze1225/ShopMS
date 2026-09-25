package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.entity.Cart;
import org.springframework.stereotype.Controller;
import com.example.shoppingcart.service.CartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    private static final int MAX_CART_ITEMS = 20; // 作业里限制一下购物车数量

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        User user = (User) loginUser;
        List<Cart> list = cartService.findByUserId(user.getId());
        result.put("code", 0);
        result.put("data", list);
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(Integer productId, Integer quantity, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        User user = (User) loginUser;
        if (quantity == null || quantity <= 0) {
            quantity = 1; // 默认加1件
        }
        List<Cart> cartList = cartService.findByUserId(user.getId());
        // 检查购物车数量是否超限（这里没写完，暂时不管了）
        // if (cartList != null && cartList.size() >= MAX_CART_ITEMS) { ... }
        String msg = cartService.addToCart(user.getId(), productId, quantity);
        if ("ok".equals(msg)) {
            result.put("code", 0);
            result.put("msg", "添加成功");
        } else {
            result.put("code", 1);
            result.put("msg", msg);
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(Integer id, Integer quantity, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        cartService.updateQuantity(id, quantity);
        result.put("code", 0);
        result.put("msg", "修改成功");
        return result;
    }

    // 从购物车中移除一件商品
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
            cartService.delete(id);
            result.put("code", 0);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除出错");
            e.printStackTrace();
        }
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
        cartService.deleteBatch(ids);
        result.put("code", 0);
        result.put("msg", "批量删除成功");
        return result;
    }
}
