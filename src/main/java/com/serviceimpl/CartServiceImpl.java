package com.serviceimpl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.CartDTO;
import com.dto.CartItemDTO;
import com.dto.ProductDTO;
import com.dto.UserDTO;
import com.entity.Cart;
import com.entity.CartItem;
import com.entity.CustomerEntity;
import com.entity.Product;
import com.exception.AddToCartNotFoundException;
import com.exception.CustomerNotFoundException;
import com.repository.CartRepository;
import com.repository.CustomerRepository;
import com.repository.ProductRepository;
import com.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public CartDTO addToCart(int customerId, int productId) {
		double total;
		int quantity;
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new AddToCartNotFoundException());

		Product product = productRepository.findById(productId).get();
		CartDTO cartDTO = new CartDTO();
		Cart cart = customer.getCart();

		if (cart == null) {
			cart = new Cart();
			cart.setCustomer(customer);
			customer.setCart(cart);
		}

		CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getProduct().equals(product)).findFirst()
				.orElse(null);

		if (cartItem != null) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		} else {
			cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			cartItem.setCart(cart);
			cart.getCartItems().add(cartItem);
		}
		total = cart.getTotalPrice();

		quantity = cart.getTotalQuantity();
		quantity = quantity + 1;
		total = product.getProductPrice() + total;
		cart.setTotalPrice(total);
		cart.setTotalQuantity(quantity);

		cartRepository.save(cart);

		UserDTO userDTO = new UserDTO();

		userDTO.setAddress(customer.getAddress());
		userDTO.setEmail(customer.getEmail());
		userDTO.setNumber(customer.getNumber());
		userDTO.setPassword(customer.getPassword());
		userDTO.setId(customer.getId());
		userDTO.setUsername(customer.getUsername());

		cartDTO.setCustomer(userDTO);

		List<CartItemDTO> cartItemDTOs = new ArrayList<>();
		for (CartItem product_i : cart.getCartItems()) {
			CartItemDTO cartItemDTO = new CartItemDTO();

			cartItemDTO.setProduct(mapProductsToDTO(product_i.getProduct()));
			cartItemDTO.setQuantity(product_i.getQuantity());
			cartItemDTOs.add(cartItemDTO);
		}
		cartDTO.setCartItems(cartItemDTOs);
		cartDTO.setTotalPrice(total);
		cartDTO.setTotalQuantity(quantity);
		return cartDTO;

	}

	private ProductDTO mapProductsToDTO(Product product) {

		ProductDTO productDTO = new ProductDTO();

		productDTO.setBrand(product.getBrand());
		productDTO.setCategory(product.getCategory());
		productDTO.setProductId(product.getProductId());
		productDTO.setProductImage(product.getProductImage());
		productDTO.setProductName(product.getProductName());
		productDTO.setProductPrice(product.getProductPrice());

		return productDTO;

	}

	@Override
	public String deleteProduct(int custid, int prodid) {

		CustomerEntity customer;
		try {
			customer = customerRepository.findById(custid).orElseThrow(() -> new CustomerNotFoundException());

			Cart cart = customer.getCart();

			List<CartItem> cartItems = cart.getCartItems();

			int quantity;
			double totalprice;
			quantity = cart.getTotalQuantity();
			totalprice = cart.getTotalPrice();
			for (CartItem cartItem : cartItems) {

				Product product = cartItem.getProduct();

				if (product.getProductId() == prodid) {
					quantity = quantity - 1;
					totalprice = totalprice - product.getProductPrice();
					cartItem.setQuantity(cartItem.getQuantity() - 1);
				}
			}
			cart.setTotalPrice(totalprice);
			cart.setTotalQuantity(quantity);
			cartRepository.save(cart);

			return "deleted Successfully";
		} catch (CustomerNotFoundException e) {
			System.out.println(e);
			return "Customer not Found";
		}

	}
}