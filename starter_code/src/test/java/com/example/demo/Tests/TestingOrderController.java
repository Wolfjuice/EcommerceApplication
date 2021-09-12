package com.example.demo.Tests;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestingOrderController {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("Beef");
        BigDecimal price = BigDecimal.valueOf(4.00);
        item.setPrice(price);
        item.setDescription("Meat From a cow");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("me");
        user.setPassword("here_i_am");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(4.00);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepository.findByUsername("me")).thenReturn(user);
        when(userRepository.findByUsername("not_me")).thenReturn(null);

    }

    @Test
    public void iDidOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("me");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void iDidNotOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("not_me");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void iOrderedAndICanProveIt() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("me");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);
    }

    @Test
    public void iDidNotOrderThereforeNothing() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("not_me");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());
    }
    @Test
    public void ohNoIDidntOrderUnderMyName() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("whiefnweifnwejnew");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());
    }
}
