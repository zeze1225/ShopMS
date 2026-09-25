package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        result.put("code", 0);
        result.put("data", brandService.findAll());
        return result;
    }
}
