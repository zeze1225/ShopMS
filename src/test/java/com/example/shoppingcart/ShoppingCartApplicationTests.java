package com.example.shoppingcart;

import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.OrderService;
import com.example.shoppingcart.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingCartApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
    }

    @Test
    void testLoginSuccess() {
        User user = userService.login("admin", "123456");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    void testLoginFail() {
        User user = userService.login("admin", "wrong");
        assertNull(user);
    }

    @Test
    void testRegisterDuplicate() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        boolean result = userService.register(user);
        assertFalse(result);
    }

    @Test
    void testAddToCart() {
        String result = cartService.addToCart(1, 1, 3);
        assertEquals("ok", result);
    }

    @Test
    void testAddToCartNoStock() {
        String result = cartService.addToCart(1, 1, 9999);
        assertTrue(result.contains("库存不足"));
    }

    @Test
    void testAddToCartProductNotExist() {
        String result = cartService.addToCart(1, 999, 1);
        assertEquals("商品不存在", result);
    }

    @Test
    void testCreateOrderEmptyCart() {
        cartService.clearCart(1);
        String result = orderService.createOrder(1);
        assertTrue(result.contains("购物车为空"));
    }

    @Test
    void testCreateOrderSuccess() {
        cartService.clearCart(1);
        cartService.addToCart(1, 1, 1);
        String result = orderService.createOrder(1);
        assertEquals("ok", result);
    }
}
