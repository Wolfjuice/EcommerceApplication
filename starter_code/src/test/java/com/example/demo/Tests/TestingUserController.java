package com.example.demo.Tests;


import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TestingUserController {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername("Lion");
        user.setPassword("jungleking");
        user.setCart(cart);
        when(userRepository.findByUsername("Lion")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("Leopard")).thenReturn(null);

    }

    @Test
    public void rawr() {
        when(encoder.encode("jungleking")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Lion");
        r.setPassword("jungleking");
        r.setConfirmPassword("jungleking");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals("Lion", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void passwordTooShort() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Lion");
        r.setPassword("mooot");
        r.setConfirmPassword("mooot");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void passwordDontMatch() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Lion");
        r.setPassword("jungleking");
        r.setConfirmPassword("bungleking");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void animalFoundByName() {
        final ResponseEntity<User> response = userController.findByUserName("Lion");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("Lion", u.getUsername());
    }

    @Test
    public void animalNotFoundByName() {
        final ResponseEntity<User> response = userController.findByUserName("Leopard");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void animalAlsoNotFoundByName() {
        final ResponseEntity<User> response = userController.findByUserName("Elephant");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void animalFoundById() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());;
    }

    @Test
    public void animalNotFoundById() {
        final ResponseEntity<User> response = userController.findById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void animalAlsoNotFoundById() {
        final ResponseEntity<User> response = userController.findById(6L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
