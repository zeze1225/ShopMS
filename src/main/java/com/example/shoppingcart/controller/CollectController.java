package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.mapper.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectMapper collectMapper;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        result.put("code", 0); result.put("data", collectMapper.findByUserId(user.getId()));
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(Integer productId, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        com.example.shoppingcart.entity.Collect c = new com.example.shoppingcart.entity.Collect();
        c.setUserId(user.getId()); c.setProductId(productId);
        collectMapper.insert(c);
        result.put("code", 0); result.put("msg", "收藏成功");
        return result;
    }

    @PostMapping("/remove")
    @ResponseBody
    public Map<String, Object> remove(Integer productId, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        collectMapper.deleteByUserAndProduct(user.getId(), productId);
        result.put("code", 0); result.put("msg", "取消收藏");
        return result;
    }
}
