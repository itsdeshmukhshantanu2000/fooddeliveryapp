package com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.ProductDTO;
import com.entity.CartItem;
import com.entity.OrderedCartItems;
import com.entity.Product;
import com.exception.ProductNotFoundException;
import com.repository.CartItemRepository;
import com.repository.OrderCartItemsRepository;
import com.repository.ProductRepository;
import com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private OrderCartItemsRepository orderCartItemsRepository;

	@Override
	public ProductDTO addProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setBrand(productDTO.getBrand());
		product.setCategory(productDTO.getCategory());
		product.setProductId(productDTO.getProductId());
		product.setProductImage(productDTO.getProductImage());
		product.setProductName(productDTO.getProductName());
		product.setProductPrice(productDTO.getProductPrice());

		productRepository.save(product);
		return productDTO;
	}

	@Override
	public ProductDTO getById(int id) {
		Product product = productRepository.findById(id).get();

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
	public String updateProduct(int id, ProductDTO productDTO) {

		Product product;
		try {
			product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());

			if (product.getProductName() != null)
				product.setProductName(productDTO.getProductName());
			if (product.getBrand() != null)
				product.setBrand(productDTO.getBrand());
			if (product.getCategory() != null)
				product.setCategory(productDTO.getCategory());
			if (product.getProductImage() != null)
				product.setProductImage(productDTO.getProductImage());
			if (product.getProductPrice() != 0)
				product.setProductPrice(productDTO.getProductPrice());

			productRepository.save(product);
		} catch (ProductNotFoundException e) {
			System.out.println(e);
			return "Product data not updated";
		}
		return "Product updated Successfully";

	}

	@Override
	public boolean deleteProduct(int id) {

		Product product = productRepository.findById(id).get();
		List<CartItem> cartItems = cartItemRepository.findByProduct(product);

		for (CartItem item : cartItems) {
			item.setProduct(null);
			item.setQuantity(0);
			cartItemRepository.save(item);
		}

		List<OrderedCartItems> orderedItems = orderCartItemsRepository.findByProduct(product);

		for (OrderedCartItems item : orderedItems) {
			item.setProduct(null);
			item.setQuantity(0);
			orderCartItemsRepository.save(item);
		}
		productRepository.deleteById(id);
		return true;

	}

	@Override
	public List<ProductDTO> getProductByBrand(String brandName) {

		List<Product> products = productRepository.findByBrand(brandName);

		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();

		for (Product product : products) {
			ProductDTO productDTO = new ProductDTO();

			productDTO.setCategory(product.getCategory());
			productDTO.setBrand(brandName);
			productDTO.setProductId(product.getProductId());
			productDTO.setProductImage(product.getProductImage());
			productDTO.setProductName(product.getProductName());
			productDTO.setProductPrice(product.getProductPrice());

			productDTOs.add(productDTO);
		}

		return productDTOs;

	}

	@Override
	public List<ProductDTO> getAllProducts() {

		List<Product> products = productRepository.findAll();

		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();

		for (Product product : products) {
			ProductDTO productDTO = new ProductDTO();

			productDTO.setCategory(product.getCategory());
			productDTO.setBrand(product.getBrand());
			productDTO.setProductId(product.getProductId());
			productDTO.setProductImage(product.getProductImage());
			productDTO.setProductName(product.getProductName());
			productDTO.setProductPrice(product.getProductPrice());

			productDTOs.add(productDTO);
		}

		return productDTOs;

	}

	@Override
	public List<ProductDTO> getByCategoryId(int id) {
		List<Product> products = productRepository.findByProductsByCatogory(id);
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		for (Product product : products) {

			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setProductId(product.getProductId());
			productDTO.setProductImage(product.getProductImage());
			productDTO.setProductName(product.getProductName());
			productDTO.setProductPrice(product.getProductPrice());

			productDTOs.add(productDTO);
		}

		return productDTOs;

	}

	public void linkImageToProduct(int productId, String imagePath) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).get();
		product.setProductImage(imagePath);
		productRepository.save(product);

	}

}
