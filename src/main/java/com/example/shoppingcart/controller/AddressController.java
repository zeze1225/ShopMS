package com.example.shoppingcart.controller;

import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.entity.Address;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressMapper addressMapper;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        List<Address> list = addressMapper.findByUserId(user.getId());
        result.put("code", 0); result.put("data", list);
        return result;
    }

    @PostMapping("/save")
    @ResponseBody
    public Map<String, Object> save(Address addr, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        User user = (User) loginUser;
        addr.setUserId(user.getId());
        if (addr.getId() != null) { addressMapper.update(addr); }
        else { addressMapper.insert(addr); }
        result.put("code", 0); result.put("msg", "保存成功");
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) { result.put("code", -1); result.put("msg", "请先登录"); return result; }
        addressMapper.deleteById(id);
        result.put("code", 0); result.put("msg", "删除成功");
        return result;
    }
}
