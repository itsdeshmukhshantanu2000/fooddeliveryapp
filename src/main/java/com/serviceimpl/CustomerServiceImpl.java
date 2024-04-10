package com.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import com.exception.CustomerNotFoundException;
import com.repository.CartRepository;
import com.repository.CustomerRepository;
import com.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public CustomerEntity registerCustomer(UserDTO userDTO) {
		CustomerEntity customer = new CustomerEntity();
		customer.setUsername(userDTO.getUsername());
		customer.setAddress(userDTO.getAddress());
		customer.setEmail(userDTO.getEmail());
		customer.setNumber(userDTO.getNumber());
		customer.setPassword(userDTO.getPassword());

		customerRepository.save(customer);
		
		return customer;
	}

	// Read all Users
	@Override
	public List<UserDTO> readAllCustomers() {
		List<CustomerEntity> customers = customerRepository.findAll();
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for (CustomerEntity customer : customers) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUsername(customer.getUsername());
			userDTO.setAddress(customer.getAddress());
			userDTO.setPassword(customer.getPassword());
			userDTO.setEmail(customer.getEmail());
			userDTO.setNumber(customer.getNumber());
			userDTO.setId(customer.getId());
			userDTOs.add(userDTO);

		}

		return userDTOs;
	}

	// Update User
	@Override
	public String updateCustomer(int id, UserDTO user) {

		CustomerEntity customer;
		try {
			customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException());

			if (user.getUsername() != null)
				customer.setUsername(user.getUsername());

			if (user.getNumber() != 0)
				customer.setNumber(user.getNumber());

			if (user.getAddress() != null)
				customer.setAddress(user.getAddress());

			if (user.getEmail() != null)
				customer.setEmail(user.getEmail());
			if(user.getPassword() != "")
				customer.setPassword(user.getPassword());

			customerRepository.save(customer);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return "Invalid customer.Customer data not updated";

		}

		return "Customer Updated Successfully";

	}

	// Delete user
	@Override
	public String deleteCustomer(int id) {
		try {
			customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException());
			customerRepository.deleteById(id);
			
		}catch (CustomerNotFoundException e) {
			System.out.println(e);
			return "Invalid customer id";
		}
		

		return "Customer deleted successfully.";
	}

	@Override
	public List<UserDTO> getByEmail(String email) {
		List<CustomerEntity> customers = customerRepository.findByEmail(email);

		List<UserDTO> userDTOs = new ArrayList<UserDTO>();

		for (CustomerEntity customer : customers) {
			UserDTO dtoUser = new UserDTO();
			dtoUser.setAddress(customer.getAddress());
			dtoUser.setEmail(customer.getEmail());
			dtoUser.setPassword(customer.getPassword());
			dtoUser.setNumber(customer.getNumber());
			dtoUser.setUsername(customer.getUsername());
			dtoUser.setId(customer.getId());
			userDTOs.add(dtoUser);
		}

		return userDTOs;
	}

	@Override
	public CartDTO getCartByCustomer(int customerId) {

		try {
			Cart cart = cartRepository.findByCustomerId(customerId).get();
			if (cart!= null) {
				CartDTO cartDTO = mapCartToDTO(cart);
				return cartDTO;
			}
		}catch(NoSuchElementException e) {
			e.getMessage();
		}
		
		return null;
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
		customerDTO.setPassword(customer.getPassword());
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

	@Override
	public UserDTO getCustomerById(int id) {
		CustomerEntity customer = customerRepository.findById(id).get();

		UserDTO dtoUser = new UserDTO();
		dtoUser.setAddress(customer.getAddress());
		dtoUser.setEmail(customer.getEmail());
		dtoUser.setPassword(customer.getPassword());
		dtoUser.setNumber(customer.getNumber());
		dtoUser.setUsername(customer.getUsername());
		dtoUser.setId(customer.getId());

		return dtoUser;
	}

}
