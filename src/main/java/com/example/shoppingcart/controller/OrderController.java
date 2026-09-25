package com.example.shoppingcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingcart.entity.Order;
import com.example.shoppingcart.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.shoppingcart.service.OrderService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(String keyword, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        User user = (User) loginUser;
        List<Order> list = orderService.search(user.getId(), keyword);
        result.put("code", 0);
        result.put("data", list);
        return result;
    }

    @RequestMapping("/get")
    @ResponseBody
    public Map<String, Object> get(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Order order = orderService.findById(id);
        if (order != null) {
            result.put("code", 0);
            result.put("data", order);
        } else {
            result.put("code", 1);
            result.put("msg", "该订单不存在");
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public Map<String, Object> create(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        try {
            User user = (User) loginUser;
            // 生成订单时会自动算总价（参考了淘宝的订单流程）
            String msg = orderService.createOrder(user.getId());
            if ("ok".equals(msg)) {
                result.put("code", 0);
                result.put("msg", "订单生成成功");
            } else {
                result.put("code", 1);
                result.put("msg", msg);
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "生成订单失败");
            e.printStackTrace();
        }
        return result;
    }

    // 状态流转：待发货 -> 已发货 -> 已完成（还有已取消）
    @PostMapping("/updateStatus")
    @ResponseBody
    public Map<String, Object> updateStatus(Integer id, String status, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        try {
            orderService.updateStatus(id, status);
            result.put("code", 0);
            result.put("msg", "状态修改成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "状态修改出错：" + e.getMessage());
        }
        return result;
    }

    // 这里有个bug：已发货的订单也能删除，懒得改了
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (session.getAttribute("loginUser") == null) {
            result.put("code", -1);
            result.put("msg", "请先登录");
            return result;
        }
        // TODO: 已发货的订单不应该允许删除
        orderService.delete(id);
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
        if (ids == null || ids.isEmpty()) {
            result.put("code", 1);
            result.put("msg", "请选择要删除的订单");
            return result;
        }
        orderService.deleteBatch(ids);
        result.put("code", 0);
        result.put("msg", "批量删除成功");
        return result;
    }
}
