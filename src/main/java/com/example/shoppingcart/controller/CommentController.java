package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Integer productId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0); result.put("data", commentMapper.findByProductId(productId));
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(Integer productId, String content, Integer star, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        if (star == null) star = 5;
        commentMapper.insert(user.getId(), productId, null, content, star);
        result.put("code", 0); result.put("msg", "评价成功");
        return result;
    }
}
