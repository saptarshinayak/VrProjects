package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.Repositry.CategoryRepository;
import com.ecommerce.Repositry.ProductRepository;
import com.ecommerce.Repositry.UserRepository;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.secure.Secure;
import com.ecommerce.service.WalletService;

@RestController
public class AdminController {
	
	//User Related Operations
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
	@GetMapping("/admin/getAllUser")
	public List<User> getAllUser() {
		List<User> users=  userRepository.findAll();
		for(User user:users) {
			user.setUsername(Secure.decrypt(user.getUsername()));
			user.setEmail(Secure.decrypt(user.getEmail()));
			user.setPassword(Secure.decrypt(user.getPassword()));
			user.setPhoneNumber(Secure.decrypt(user.getPhoneNumber()));
		}
		return users;
	}
	
	@GetMapping("/admin/getUser/{id}")
	public User getUserById(@PathVariable Long id) {
		User user=userRepository.findById(id).get();
		user.setUsername(Secure.decrypt(user.getUsername()));
		user.setPassword(Secure.decrypt(user.getPassword()));
		user.setEmail(Secure.decrypt(user.getEmail()));
		user.setPhoneNumber(Secure.decrypt(user.getPhoneNumber()));
		return user;
	}
	
	@DeleteMapping("/admin/deleteUser/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id){
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return ResponseEntity.ok("User Deleted successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this id");
		}
	}
	
	@PutMapping("/admin/user/update/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user){
		if(userRepository.existsById(id)) {
			user.setId(id);
			userRepository.save(user);
			return ResponseEntity.ok("User updated successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found with id:"+id);
		}
	}
	
	//Admin Login
	
	@PostMapping("/admin/login")
	public ResponseEntity<String> loginAdmin(@RequestBody LoginRequest request) {
	    User user = userRepository.findByEmailOrPhoneNumber(Secure.encrypt(request.getEmailOrPhone()), Secure.encrypt(request.getEmailOrPhone()));

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
	    }

	    if (!user.getPassword().equals(Secure.encrypt(request.getPassword()))) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
	    }

	    if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin does not exist");
	    }

	    return ResponseEntity.ok("Admin login successful");
	}

	
	//Category related operations
	
	@PostMapping("/admin/addCategory")
	public ResponseEntity<String> addCategory(@RequestBody Category category){
		repository.save(category);
		return ResponseEntity.ok("Category added Succesfully");
	}
	
	@GetMapping("/admin/getAllCategory")
    public ResponseEntity<List<Category>> getAllCategory() {
        try {
            List<Category> categories = repository.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping("/admin/getCategoryById/{id}")
	public ResponseEntity<Object> getCategoryById(@PathVariable int id) {
	    List<Category> categories = repository.findAll(); // Get all categories
	    for (Category category : categories) {
	        if (category.getId() == id) {
	            return new ResponseEntity<>(category, HttpStatus.OK);
	        }
	    }
	    // Custom message if category not found
	    return new ResponseEntity<>("Category with ID " + id + " not found", HttpStatus.NOT_FOUND);
	}

	
	@PutMapping("/admin/updateCategory/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody Category category){
		if(repository.existsById(id)) {
			category.setId(id);
			repository.save(category);
			return ResponseEntity.ok("Category update Successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("category not found with id:"+id);
		}
	}
	
	@DeleteMapping("/admin/deleteCategory/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable int id){
		if(repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.ok("Category Deleted Successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category Not found");
		}
	}
	
	
	//Product Related Operations 
	@PostMapping("/admin/addProduct")
	 public ResponseEntity<String> addCategory(@RequestBody Product product){
		 productRepository.save(product);
		 return ResponseEntity.ok("Product Added Successfully");
		 
	 }
	
	@GetMapping("/admin/getProduct")
	public ResponseEntity<List<Product>> getAllProduct(){
		try {
			List<Product> product=productRepository.findAll();
			return new ResponseEntity<>(product,HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/admin/getProductById/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long    id) {
	    Optional<Product> product = productRepository.findById(id);
	    if (product.isPresent()) {
	        return new ResponseEntity<>(product.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
	@PutMapping("/admin/updateProduct/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
	    if (productRepository.existsById(id)) {
	        product.setId(id);
	        productRepository.save(product);
	        return ResponseEntity.ok("Product updated successfully");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
	}
	
	@DeleteMapping("/admin/deleteProduct/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		if(productRepository.existsById(id)) {
			productRepository.deleteById(id);
			return ResponseEntity.ok("Product deleted successfully");
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
		}
	}
	
	
	//Wallet Related operations
	

//    @PostMapping("/admin/createWallet")
//    public ResponseEntity<String> createWallet(@RequestParam Long userId, @RequestParam double amount) {
//        return walletService.createWallet(userId, amount);
//    }
}
