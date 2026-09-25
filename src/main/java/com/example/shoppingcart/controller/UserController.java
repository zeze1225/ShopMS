package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String username, String password, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("loginUser", user);
            result.put("code", 0);
            result.put("msg", "登录成功");
        } else {
            result.put("code", 1);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(User user) {
        Map<String, Object> result = new HashMap<>();
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            result.put("code", 1);
            result.put("msg", "用户名不能为空");
            return result;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            result.put("code", 1);
            result.put("msg", "密码不能为空");
            return result;
        }
        boolean success = userService.register(user);
        if (success) {
            result.put("code", 0);
            result.put("msg", "注册成功，请登录");
        } else {
            result.put("code", 1);
            result.put("msg", "用户名已存在");
        }
        return result;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginUser");
        return "redirect:/login.html";
    }

    @RequestMapping("/info")
    @ResponseBody
    public Map<String, Object> getInfo(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("loginUser");
        if (user != null) {
            result.put("code", 0);
            result.put("data", user);
        } else {
            result.put("code", 1);
            result.put("msg", "未登录");
        }
        return result;
    }
}
