package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dto.OrdersDTO;
import com.entity.Cart;
import com.entity.CartItem;
import com.entity.CustomerEntity;
import com.entity.OrderedCart;
import com.entity.Orders;
import com.entity.Product;
import com.entity.Status;
import com.repository.CartRepository;
import com.repository.DeliveryPartnerRepository;
import com.repository.OrderedCartRepository;
import com.repository.OrdersRepository;
import com.serviceimpl.DeliveryPartnerServiceImpl;
import com.serviceimpl.OrdersServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderedCartRepository orderedCartRepository;

    @Mock
    private DeliveryPartnerServiceImpl deliveryPartnerServiceImpl;

    @Mock
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @Test
    public void testAddOrders() {
        // Mocking CustomerEntity
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setUsername("testuser");
        customer.setAddress("123 Test St");
        customer.setEmail("test@example.com");
        customer.setNumber(1234567890);
        customer.setPassword("testpassword");

        // Mocking Product
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(10.0);
        product.setProductImage("test.jpg");

        // Mocking CartItem
        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        // Mocking Cart
        Cart cart = new Cart();
        cart.setId(1);
        cart.setCustomer(customer);
        cart.setCartItems(cartItems);
        cart.setTotalPrice(20.0);
        cart.setTotalQuantity(2);

        // Mocking Orders
        Orders order = new Orders();
        order.setOrderId(1);
        order.setCart(cart);
        order.setDate(LocalDateTime.now());
        order.setStatus(Status.pending);
        order.setDeliveryPartnerName(null);

        // Mocking OrderedCart
        OrderedCart orderedCart = new OrderedCart();
        orderedCart.setId(1);
        orderedCart.setCartItems(new ArrayList<>());
        orderedCart.setCustomer(cart.getCustomer());
        orderedCart.setOrderid(order.getOrderId());
        orderedCart.setTotalPrice(cart.getTotalPrice());
        orderedCart.setTotalQuantity(cart.getTotalQuantity());

        when(cartRepository.findByCustomerId(1)).thenReturn(Optional.of(cart));

        OrdersDTO result = ordersService.addOrders(1);

        assertEquals(0, result.getOrderId());
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(0, result.getOrderedCartDTO().getId());
        assertEquals(orderedCart.getTotalPrice(), result.getOrderedCartDTO().getTotalPrice());
        assertEquals(orderedCart.getTotalQuantity(), result.getOrderedCartDTO().getTotalQuantity());
    }




    @Test
    public void testDeleteOrders() {
        OrderedCart orderedCart = new OrderedCart();
        orderedCart.setId(1);

        when(orderedCartRepository.findByOrderId(1)).thenReturn(orderedCart);

        String result = ordersService.deleteOrders(1);

        assertEquals("Deleted Successfully", result);
        verify(ordersRepository).deleteById(1);
        verify(orderedCartRepository).deleteByOrderId(1);
    }

    @Test
    public void testGetOrderCustomerId() {
        Orders order = new Orders();
        order.setOrderId(1);
        order.setDate(LocalDateTime.now());
        order.setStatus(Status.pending);
        order.setDeliveryPartnerName(null);

        OrderedCart orderedCart = new OrderedCart();
        orderedCart.setId(1);

        when(ordersRepository.findAll()).thenReturn(List.of(order));
        when(orderedCartRepository.findByCustomerId(1)).thenReturn(List.of(orderedCart));

        List<OrdersDTO> result = ordersService.getOrderCustomerId(1);

        assertEquals(0,result.size());

    }
}
