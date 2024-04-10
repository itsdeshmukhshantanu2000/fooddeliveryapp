package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dto.CartDTO;
import com.dto.CartItemDTO;
import com.dto.UserDTO;
import com.entity.Cart;
import com.entity.CartItem;
import com.entity.CustomerEntity;
import com.entity.Product;
import com.repository.CartRepository;
import com.repository.CustomerRepository;
import com.repository.ProductRepository;
import com.serviceimpl.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void testAddToCart() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);
        customerEntity.setUsername("testUser");
        customerEntity.setEmail("test@example.com");
        customerEntity.setAddress("Test Address");
        customerEntity.setNumber(1234567890);
        customerEntity.setPassword("testPassword");

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(50.0);

        Cart cart = new Cart();
        customerEntity.setCart(cart);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customerEntity));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        CartDTO cartDTO = cartService.addToCart(1, 1);

        assertNotNull(cartDTO);
        assertEquals(1, cartDTO.getCustomer().getId());
        assertEquals(1, cartDTO.getCartItems().size());
        assertEquals(50.0, cartDTO.getTotalPrice());
        assertEquals(1, cartDTO.getTotalQuantity());

        UserDTO userDTO = cartDTO.getCustomer();
        assertNotNull(userDTO);
        assertEquals("testUser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("Test Address", userDTO.getAddress());
        assertEquals(1234567890, userDTO.getNumber());
        assertEquals("testPassword", userDTO.getPassword());

        CartItemDTO cartItemDTO = cartDTO.getCartItems().get(0);
        assertNotNull(cartItemDTO);
        assertEquals("Test Product", cartItemDTO.getProduct().getProductName());
        assertEquals(50.0, cartItemDTO.getProduct().getProductPrice());
        assertEquals(1, cartItemDTO.getQuantity());
    }

    @Test
    public void testDeleteProduct() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);
        customerEntity.setUsername("testUser");
        customerEntity.setEmail("test@example.com");
        customerEntity.setAddress("Test Address");
        customerEntity.setNumber(1234567890);
        customerEntity.setPassword("testPassword");

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(50.0);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        Cart cart = new Cart();
        cart.setCartItems(cartItems);
        cart.setTotalPrice(50.0);
        cart.setTotalQuantity(1);
        customerEntity.setCart(cart);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customerEntity));

        String result = cartService.deleteProduct(1, 1);

        assertEquals("deleted Successfully", result);
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(0.0, cart.getTotalPrice());
        assertEquals(1, cart.getCartItems().size());
    }
}

