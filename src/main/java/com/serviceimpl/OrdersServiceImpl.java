package com.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.CartDTO;
import com.dto.CartItemDTO;
import com.dto.OrderedCartDTO;
import com.dto.OrderedCartItemDTO;
import com.dto.OrdersDTO;
import com.dto.ProductDTO;
import com.dto.UserDTO;
import com.entity.Cart;
import com.entity.CartItem;
import com.entity.CustomerEntity;
import com.entity.DeliveryPartner;
import com.entity.OrderedCart;
import com.entity.OrderedCartItems;
import com.entity.Orders;
import com.entity.PartnerStatus;
import com.entity.Product;
import com.entity.Status;
import com.exception.CustomerNotFoundException;
import com.repository.CartRepository;
import com.repository.DeliveryPartnerRepository;
import com.repository.OrderedCartRepository;
import com.repository.OrdersRepository;
import com.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private OrderedCartRepository orderedCartRepository; 
	@Autowired
	private DeliveryPartnerServiceImpl deliveryPartnerServiceImpl;
	@Autowired
	private DeliveryPartnerRepository deliveryPartnerRepository;

	@Override
	public OrdersDTO addOrders(int customerId) {

		OrdersDTO ordersDTO = new OrdersDTO();
		try {
			Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new CustomerNotFoundException());
			Orders order = new Orders();
			order.setCart(cart);
			order.setDate(LocalDateTime.now());
			order.setStatus(Status.pending);
			order.setDeliveryPartnerName(null);

			ordersRepository.save(order);

			OrderedCart orderedCart = new OrderedCart();

			List<CartItem> cartItems = cart.getCartItems();

			List<OrderedCartItems> orderedCartItems = new ArrayList<OrderedCartItems>();

			for (CartItem cartItem : cartItems) {
				OrderedCartItems orderedCartItem = new OrderedCartItems();
				orderedCartItem.setOrderedcart(orderedCart);
				orderedCartItem.setProduct(cartItem.getProduct());
				orderedCartItem.setQuantity(cartItem.getQuantity());

				orderedCartItems.add(orderedCartItem);
			}

			orderedCart.setCartItems(orderedCartItems);

			orderedCart.setCustomer(cart.getCustomer());
			orderedCart.setOrderid(order.getOrderId());
			orderedCart.setTotalPrice(cart.getTotalPrice());
			orderedCart.setTotalQuantity(cart.getTotalQuantity());
			order.setOrderedCart(orderedCart);

			orderedCartRepository.save(orderedCart);

			ordersDTO.setCart(mapCartToDTO(order.getCart()));
			ordersDTO.setDate(order.getDate());
			ordersDTO.setOrderId(order.getOrderId());
			ordersDTO.setStatus(order.getStatus());
			ordersDTO.setOrderedCartDTO(mapCartToDTO(orderedCart));

			cartRepository.deleteCartItemsByCartId(cart.getId());
			cart.setTotalQuantity(0);
			cart.setTotalPrice(0);
			cartRepository.save(cart);

		} catch (CustomerNotFoundException e) {

			System.out.println(e);

		}

		return ordersDTO;
	}

	@Override
	public OrdersDTO getById(int id) {

		Orders order = ordersRepository.findById(id).get();
		OrderedCart cart = orderedCartRepository.findByOrderId(id);

		OrdersDTO orderDTO = new OrdersDTO();

		orderDTO.setOrderedCartDTO(mapCartToDTO(cart));

		orderDTO.setDate(order.getDate());
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setStatus(order.getStatus());

		return orderDTO;
	}

	@Override
	public List<OrdersDTO> findAll() {

		List<Orders> orders = ordersRepository.findAll();
		

		List<OrdersDTO> ordersDTOs = new ArrayList<OrdersDTO>();
		List<OrderedCart> carts = orderedCartRepository.findAll();
		for (Orders order : orders) {
			OrdersDTO orderDTO = new OrdersDTO();
			for (OrderedCart cart : carts) {
				if (order.getOrderId() == cart.getOrderid())
					orderDTO.setOrderedCartDTO(mapCartToDTO(cart));
			}
			orderDTO.setDate(order.getDate());
			orderDTO.setOrderId(order.getOrderId());
			orderDTO.setStatus(order.getStatus());
			orderDTO.setOrderedCartDTO(mapCartToDTO(order.getOrderedCart()));
			orderDTO.setDeliveryPartnerName(order.getDeliveryPartnerName());
			ordersDTOs.add(orderDTO);

		}

		Collections.reverse(ordersDTOs);
		return ordersDTOs;
	}

	@Override
	public String deleteOrders(int orderId) {

		OrderedCart cart = orderedCartRepository.findByOrderId(orderId);

		cart.getCartItems().clear();

		ordersRepository.deleteById(orderId);
		orderedCartRepository.deleteByOrderId(orderId);
		return "Deleted Successfully";
	}

	@Override
	public List<OrdersDTO> getOrderCustomerId(int customerId) {

		List<OrderedCart> carts = orderedCartRepository.findByCustomerId(customerId);

		List<Orders> orders = ordersRepository.findAll();

		List<OrdersDTO> ordersDTOs = new ArrayList<OrdersDTO>();

		for (Orders order : orders) {
			for (OrderedCart cart : carts) {
				if (order.getOrderId() == cart.getOrderid()) {
					OrdersDTO orderDTO = new OrdersDTO();
					orderDTO.setDate(order.getDate());
					orderDTO.setOrderId(order.getOrderId());
					orderDTO.setStatus(order.getStatus());
					orderDTO.setOrderedCartDTO(mapCartToDTO(cart));
					orderDTO.setDeliveryPartnerName(order.getDeliveryPartnerName());

					ordersDTOs.add(orderDTO);

				}
			}
		}
		Collections.reverse(ordersDTOs);
		return ordersDTOs;

	}

	private CartDTO mapCartToDTO(Cart cart) {
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setCustomer(mapCustomerToDTO(cart.getCustomer()));
		cartDTO.setCartItems(mapCartItemsToDTO(cart.getCartItems()));
		cartDTO.setTotalPrice(cart.getTotalPrice());
		cartDTO.setTotalQuantity(cart.getTotalQuantity());
		return cartDTO;
	}

	private UserDTO mapCustomerToDTO(CustomerEntity customer) {
		UserDTO customerDTO = new UserDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setAddress(customer.getAddress());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setNumber(customer.getNumber());
		customerDTO.setUsername(customer.getUsername());
		return customerDTO;
	}

	private List<CartItemDTO> mapCartItemsToDTO(List<CartItem> cartItems) {
		List<CartItemDTO> cartitemDTOs = new ArrayList<CartItemDTO>();

		for (CartItem cartitem : cartItems) {

			CartItemDTO cartItemDTO = new CartItemDTO();
			cartItemDTO.setId(cartitem.getId());
			cartItemDTO.setProduct(mapProductsToDTO(cartitem.getProduct()));
			cartItemDTO.setQuantity(cartitem.getQuantity());

			cartitemDTOs.add(cartItemDTO);

		}
		return cartitemDTOs;

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

	private OrderedCartDTO mapCartToDTO(OrderedCart cart) {
		OrderedCartDTO cartDTO = new OrderedCartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setCustomer(mapCustomerToDTO(cart.getCustomer()));
		cartDTO.setCartItems(mapCartItemsToDTO1(cart.getCartItems()));
		cartDTO.setTotalPrice(cart.getTotalPrice());
		cartDTO.setTotalQuantity(cart.getTotalQuantity());
		return cartDTO;
	}

	private List<OrderedCartItemDTO> mapCartItemsToDTO1(List<OrderedCartItems> cartItems) {
		List<OrderedCartItemDTO> cartitemDTOs = new ArrayList<OrderedCartItemDTO>();

		for (OrderedCartItems cartitem : cartItems) {

			OrderedCartItemDTO cartItemDTO = new OrderedCartItemDTO();
			cartItemDTO.setId(cartitem.getId());
			cartItemDTO.setProduct(mapProductsToDTO(cartitem.getProduct()));
			cartItemDTO.setQuantity(cartitem.getQuantity());

			cartitemDTOs.add(cartItemDTO);

		}
		return cartitemDTOs;
	}
//	@Override
//	public String orderAccepted(int orderId) {
//		
//		int partnerId=deliveryPartnerServiceImpl.assignDeliveryPartner();
//		if(partnerId==0) {
//			return "No partners available currently.";
//		}
//		
//		try {
//			Orders order=ordersRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException());
//			order.setDeliveryPartner(partnerId);
//			order.setStatus(Status.Accepted);
//			ordersRepository.save(order);
//		}catch(OrderNotFoundException e) {
//			return "Invalid order ID.";
//		}
//		return "Order Accepted";
//	}

	@Override
	public String orderStatus(int orderId) {
		int partnerId = deliveryPartnerServiceImpl.assignDeliveryPartner();
		if (partnerId == 0) {
			return "No partners available currently.";
		}
		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
		order.setDeliveryPartnerName(deliveryPartnerRepository.findById(partnerId).get().getPartnerName());
		order.setStatus(Status.Accepted);
		ordersRepository.save(order);
		return "Order with ID " + orderId + " has been accepted.";

	}

	@Override
	public String orderDelivered(int orderId) {
	    try {
	        Orders order = ordersRepository.findById(orderId)
	                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
	        // Update the order status to "Delivered"
	        order.setStatus(Status.Delivered);
	        DeliveryPartner partner= deliveryPartnerRepository.findByPartnerName(order.getDeliveryPartnerName());
	        partner.setStatus(PartnerStatus.Not_Assigned);
	        deliveryPartnerRepository.save(partner);
	        // Save the updated order
	        ordersRepository.save(order);
	        return "Order with ID " + orderId + " has been marked as delivered.";
	    } catch (Exception e) {
	        // Handle any exceptions
	        return "Failed to mark order as delivered. Error: " + e.getMessage();
	    }
	}

}
