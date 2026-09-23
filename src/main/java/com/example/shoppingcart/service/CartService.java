package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Cart;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.mapper.CartMapper;
import com.example.shoppingcart.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public List<Cart> findByUserId(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    public String addToCart(Integer userId, Integer productId, Integer quantity) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            return "商品不存在";
        }
        if (product.getStock() < quantity) {
            return "商品库存不足，当前库存：" + product.getStock();
        }

        Cart existCart = cartMapper.findByUserAndProduct(userId, productId);
        if (existCart != null) {
            int newQuantity = existCart.getQuantity() + quantity;
            if (newQuantity > product.getStock()) {
                return "累计数量超过库存，当前库存：" + product.getStock();
            }
            cartMapper.updateQuantity(existCart.getId(), newQuantity);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cartMapper.insert(cart);
        }
        return "ok";
    }

    public void updateQuantity(Integer id, Integer quantity) {
        cartMapper.updateQuantity(id, quantity);
    }

    public void delete(Integer id) {
        cartMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            cartMapper.deleteByIds(ids);
        }
    }

    public void clearCart(Integer userId) {
        cartMapper.clearByUserId(userId);
    }
}
