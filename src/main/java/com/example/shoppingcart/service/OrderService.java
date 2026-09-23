package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Cart;
import com.example.shoppingcart.entity.Order;
import com.example.shoppingcart.entity.OrderItem;
import com.example.shoppingcart.entity.Address;
import com.example.shoppingcart.mapper.AddressMapper;
import com.example.shoppingcart.mapper.CartMapper;
import com.example.shoppingcart.mapper.OrderItemMapper;
import com.example.shoppingcart.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private AddressMapper addressMapper;

    public List<Order> findByUserId(Integer userId) {
        return orderMapper.findByUserId(userId);
    }

    public List<Order> search(Integer userId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return orderMapper.findByUserId(userId);
        }
        return orderMapper.searchByUserId(userId, keyword);
    }

    public Order findById(Integer id) {
        return orderMapper.findById(id);
    }

    // 结算购物车
    @Transactional
    public String createOrder(Integer userId) {
        List<Cart> cartList = cartMapper.findByUserId(userId);
        if (cartList == null || cartList.size() == 0) {
            return "购物车为空，无法生成订单";
        }

        double total = 0;
        for (Cart item : cartList) {
            total += item.getProductPrice() * item.getQuantity();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderNo = sdf.format(new Date()) + "_" + userId;

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(orderNo);
        order.setTotal(total);
        order.setStatus("待发货");
        Address defAddr = addressMapper.findDefaultByUserId(userId);
        if (defAddr != null) {
            order.setAddress(defAddr.getReceiver() + " " + defAddr.getPhone() + " " + defAddr.getAddress());
        } else {
            order.setAddress("");
        }
        orderMapper.insert(order);

        for (Cart item : cartList) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(item.getProductId());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getProductPrice());
            orderItemMapper.insert(oi);
        }

        cartMapper.clearByUserId(userId);

        return "ok";
    }

    public void updateStatus(Integer id, String status) {
        orderMapper.updateStatus(id, status);
    }

    public void delete(Integer id) {
        orderMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            orderMapper.deleteByIds(ids);
        }
    }
}
