package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dto.ProductDTO;
import com.entity.CartItem;
import com.entity.OrderedCartItems;
import com.entity.Product;
import com.repository.CartItemRepository;
import com.repository.OrderCartItemsRepository;
import com.repository.ProductRepository;
import com.serviceimpl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderCartItemsRepository orderCartItemsRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testAddProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setBrand("TestBrand");
        productDTO.setProductId(1);
        productDTO.setProductName("TestProduct");
        productDTO.setProductImage("test.jpg");
        productDTO.setProductPrice(100.0);

        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        ProductDTO result = productService.addProduct(productDTO);

        assertEquals(productDTO, result);
    }

    @Test
    public void testGetById() {
        Product product = new Product();
        product.setBrand("TestBrand");
        product.setProductId(1);
        product.setProductName("TestProduct");
        product.setProductImage("test.jpg");
        product.setProductPrice(100.0);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        ProductDTO result = productService.getById(1);

        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getBrand(), result.getBrand());
        assertEquals(product.getCategory(), result.getCategory());
        assertEquals(product.getProductImage(), result.getProductImage());
        assertEquals(product.getProductPrice(), result.getProductPrice());
        assertEquals(product.getProductId(), result.getProductId());
    }

    @Test
    public void testUpdateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setBrand("TestBrand");
        productDTO.setProductId(1);
        productDTO.setProductName("TestProduct");
        productDTO.setProductImage("test.jpg");
        productDTO.setProductPrice(100.0);

        Product product = new Product();
        product.setBrand("OldBrand");
        product.setProductName("OldProduct");
        product.setProductImage("old.jpg");
        product.setProductPrice(50.0);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        String result = productService.updateProduct(1, productDTO);

        assertEquals("Product updated Successfully", result);
        assertEquals(productDTO.getProductName(), product.getProductName());
        assertEquals(productDTO.getBrand(), product.getBrand());
        assertEquals(productDTO.getCategory(), product.getCategory());
        assertEquals(productDTO.getProductImage(), product.getProductImage());
        assertEquals(productDTO.getProductPrice(), product.getProductPrice());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setProductId(1);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        OrderedCartItems orderedCartItem = new OrderedCartItems();
        orderedCartItem.setProduct(product);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        List<OrderedCartItems> orderedCartItems = new ArrayList<>();
        orderedCartItems.add(orderedCartItem);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(cartItemRepository.findByProduct(any(Product.class))).thenReturn(cartItems);
        when(orderCartItemsRepository.findByProduct(any(Product.class))).thenReturn(orderedCartItems);

        assertTrue(productService.deleteProduct(1));
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetProductByBrand() {
        Product product = new Product();
        product.setBrand("TestBrand");
        product.setProductId(1);
        product.setProductName("TestProduct");
        product.setProductImage("test.jpg");
        product.setProductPrice(100.0);

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productRepository.findByBrand(anyString())).thenReturn(productList);

        List<ProductDTO> result = productService.getProductByBrand("TestBrand");

        assertEquals(1, result.size());
        assertEquals("TestBrand", result.get(0).getBrand());
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product();
        product.setBrand("TestBrand");
        product.setProductId(1);
        product.setProductName("TestProduct");
        product.setProductImage("test.jpg");
        product.setProductPrice(100.0);

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("TestProduct", result.get(0).getProductName());
    }

   
}
