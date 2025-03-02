package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.Repositry.CategoryRepository;
import com.ecommerce.Repositry.ProductRepository;
import com.ecommerce.Repositry.UserRepository;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.secure.Secure;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		user.setUsername(Secure.encrypt(user.getUsername()));
		user.setEmail(Secure.encrypt(user.getEmail()));
		user.setPassword(Secure.encrypt(user.getPassword()));
		user.setPhoneNumber(Secure.encrypt(user.getPhoneNumber()));
		userRepository.save(user);
		return ResponseEntity.ok("user Inserted Sucessfully");
	}
	
	@GetMapping("/getAllUser")
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setUsername(Secure.decrypt(user.getUsername())); 
            user.setEmail(Secure.decrypt(user.getEmail())); 
            user.setPassword(Secure.decrypt(user.getPassword()));
            user.setPhoneNumber(Secure.decrypt(user.getPhoneNumber()));
        }
        return users;
    }
	
	@GetMapping("/getUser/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).get();
        user.setUsername(Secure.decrypt(user.getUsername()));
        user.setEmail(Secure.decrypt(user.getEmail()));
        user.setPassword(Secure.decrypt(user.getPassword())); 
        user.setPhoneNumber(Secure.decrypt(user.getPhoneNumber()));
        return user;
    }
	
	@DeleteMapping("/user/deleteUser/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id){
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return ResponseEntity.ok("User Deleted successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this id");
		}
	}
	
	@PutMapping("/user/update/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
	    if (userRepository.existsById(id)) {
	        user.setId(id);
	        user.setUsername(Secure.encrypt(user.getUsername())); // Encrypt name
	        user.setEmail(Secure.encrypt(user.getEmail()));
	        user.setPassword(Secure.encrypt(user.getPassword())); // Encrypt password
	        user.setPhoneNumber(Secure.encrypt(user.getPhoneNumber()));
	        userRepository.save(user);
	        return ResponseEntity.ok("User updated successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
	    }
	}

	@PostMapping("/user/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
	    User user = userRepository.findByEmailOrPhoneNumber(Secure.encrypt(request.getEmailOrPhone()), Secure.encrypt(request.getEmailOrPhone()));

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }

	    // Decrypt stored password for comparison
	    String decryptedPassword = Secure.decrypt(user.getPassword());

	    if (!decryptedPassword.equals(request.getPassword())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
	    }

	    if (!"USER".equalsIgnoreCase(user.getRole())) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
	    }

	    return ResponseEntity.ok("User login successful");
	}
	
	
	
	
	//CategoryRelated Operations
	
	@GetMapping("/user/getAllCategory")
    public ResponseEntity<List<Category>> getAllCategory() {
        try {
            List<Category> categories = repository.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	//ProductRelated Operations
	
	@GetMapping("/user/getProduct")
	public ResponseEntity<List<Product>> getAllProduct(){
		try {
			List<Product> product=productRepository.findAll();
			return new ResponseEntity<>(product,HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Cart operations
	
//	@PostMapping("/user/add")
//	public ResponseEntity<String> addToCart(@RequestBody Cart cart) {
//	    Product product = productRepository.findById(cart.getProductId()).orElse(null);
//	    if (product == null) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
//	    }
//
//	    // ✅ Check if the product already exists in the cart for the user
//	    Cart existingCart = cartRepository.findByUserIdAndProductId(cart.getUserId(), cart.getProductId());
//
//	    if (existingCart != null) {
//	        // ✅ Update quantity and total amount
//	        existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
//	        existingCart.setTotalAmount(product.getPrice() * existingCart.getQuantity());
//	        cartRepository.save(existingCart);
//	        return ResponseEntity.ok("Cart updated successfully");
//	    } else {
//	        // ✅ Add a new row if the product is not in the cart
//	        cart.setTotalAmount(product.getPrice() * cart.getQuantity());
//	        cartRepository.save(cart);
//	        return ResponseEntity.ok("Product added to cart successfully");
//	    }
//	}
	
	
	
	


	
	
	
}
