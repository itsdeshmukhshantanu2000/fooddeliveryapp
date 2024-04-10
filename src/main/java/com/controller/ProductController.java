package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dto.ProductDTO;
import com.serviceimpl.ProductServiceImpl;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*") // Frontend Connection
public class ProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@PostMapping("/addProduct")
	public ProductDTO addNewProduct(@RequestBody ProductDTO productDTO) {
		return productServiceImpl.addProduct(productDTO);
	}

	@GetMapping("/getById/{id}")
	public ProductDTO getProductById(@PathVariable(value = "id") int id) {
		return productServiceImpl.getById(id);
	}

	@PutMapping("/updateProduct/{no}")
	public String updateProduct(@PathVariable(value = "no") int no, @RequestBody ProductDTO product) {

		return productServiceImpl.updateProduct(no, product);
	}

	@DeleteMapping("/deleteProduct/{no}")
	public boolean deleteProduct(@PathVariable(value = "no") int no) {
		productServiceImpl.deleteProduct(no);

		return true;
	}

	@GetMapping("/getByBrand/{name}")
	public List<ProductDTO> getProductById(@PathVariable(value = "name") String name) {
		return productServiceImpl.getProductByBrand(name);
	}

	@GetMapping("/getAllProducts")
	public List<ProductDTO> getAllProducts() {
		return productServiceImpl.getAllProducts();
	}

	@GetMapping("/getByCategoryId/{id}")
	public List<ProductDTO> getProductByCategoryId(@PathVariable(value = "id") int id) {
		return productServiceImpl.getByCategoryId(id);
	}

	@PostMapping("/{id}/upload-image")
	public ResponseEntity<?> handleImageUpload(@PathVariable("id") int productId,
			@RequestParam("image") MultipartFile image) {
		if (!image.isEmpty()) {
			try {
				// Generate a unique filename for the image to avoid naming conflicts
				String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
				// Define the path where you want to save the image
				// Note: Avoid using "file:///" prefix when specifying file paths in Java IO
				// operations
				String directoryPath = "C:/Users/asahuii/Downloads/";
				String imagePath = directoryPath + filename;

				File imageFile = new File(imagePath);
				// Ensure the directory exists
				imageFile.getParentFile().mkdirs();
				// Save the image file
				image.transferTo(imageFile);

				// Link the image path to the product in your database
				// Adjust this method to suit how you're linking images in your DB
				productServiceImpl.linkImageToProduct(productId, imagePath);

				return ResponseEntity.ok("Image uploaded successfully");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body("Failed to upload image");
			}
		} else {
			return ResponseEntity.badRequest().body("No image file provided");
		}
	}

}
