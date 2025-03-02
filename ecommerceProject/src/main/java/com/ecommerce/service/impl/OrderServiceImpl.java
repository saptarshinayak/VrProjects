package com.ecommerce.service.impl;

import java.util.Date;
//import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.ecommerce.Repositry.CartRepository;
import com.ecommerce.Repositry.OrderHistoryRepository;
import com.ecommerce.Repositry.OrderRepository;
import com.ecommerce.Repositry.PaymentRepository;
import com.ecommerce.Repositry.ProductRepository;
import com.ecommerce.Repositry.PromoCodeRepository;
import com.ecommerce.Repositry.TransactionRepository;
import com.ecommerce.Repositry.WalletRepository;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderHistory;
import com.ecommerce.model.Payment;
import com.ecommerce.model.Product;
import com.ecommerce.model.PromoCode;
import com.ecommerce.model.Transaction;
import com.ecommerce.model.Wallet;
import com.ecommerce.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private PromoCodeRepository promoCodeRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Order placeOrder(Long userId, String promoCode, String address) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty for user ID: " + userId);
        }

        double totalAmount = 0.0;
        double discountAmount = 0.0;

        // Calculate total amount of the cart
        for (Cart cart : cartItems) {
            totalAmount += cart.getTotalAmount();
        }
        if (promoCode != null && !promoCode.isEmpty()) {
            PromoCode promo = promoCodeRepository.findByCodeAndStatus(promoCode, "ACTIVE");
            if (promo != null) {
                discountAmount = (totalAmount * promo.getDiscount()) / 100;
            }
        }

        double finalAmount = totalAmount - discountAmount;

        // Check wallet balance
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null || wallet.getAmount() < finalAmount) {
        	Order order = new Order();
        	order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setPromoCode(promoCode);
            order.setDiscountAmount(discountAmount);
            order.setFinalAmount(finalAmount);
            order.setStatus("FAILED");
            order.setAddress(address);
            Order savedOrder = orderRepository.save(order);

            saveOrderHistory(userId,savedOrder.getId(),"Failed");

            
        	Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setOrderId(savedOrder.getId()); 
            payment.setAmount(finalAmount);
            payment.setPaymentMethod("WALLET"); 
            payment.setPaymentStatus("Failed"); 
            payment.setTransactionId("TXN" + System.currentTimeMillis()); 
            // Save the payment
            Payment savedPayment = paymentRepository.save(payment);
        	Transaction failedTransaction = new Transaction();
            failedTransaction.setUserId(userId);
            failedTransaction.setPaymentId(savedPayment.getId());
            failedTransaction.setAmount(finalAmount);
            failedTransaction.setTransactionType("NO PAYMENT");
            failedTransaction.setTransactionStatus("FAILED");
            failedTransaction.setTransactionDate(new Date(userId));
            failedTransaction.setReferenceNumber("TXN" + System.currentTimeMillis());
            
            // Save the failed transaction
            transactionRepository.save(failedTransaction);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient wallet balance for user ID: " + userId);
        }

        // Deduct amount from wallet
        wallet.setAmount(wallet.getAmount() - finalAmount);
        walletRepository.save(wallet);

        // Create the order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPromoCode(promoCode);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setStatus("PLACED");
        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);
        
        saveOrderHistory(userId,savedOrder.getId(),"Placed");

        // Create the payment record
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setOrderId(savedOrder.getId()); // Link to the order
        payment.setAmount(finalAmount);
        payment.setPaymentMethod("WALLET"); // Assuming the payment method is from wallet
        payment.setPaymentStatus("SUCCESS"); // Mark payment as successful
        payment.setTransactionId("TXN" + System.currentTimeMillis()); // Generate a transaction ID

        // Save the payment
        Payment savedPayment = paymentRepository.save(payment);

        // Create the transaction record
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setPaymentId(savedPayment.getId()); // Link to the payment
        transaction.setAmount(finalAmount);
        transaction.setTransactionType("DEBIT"); // Debit since money is deducted from wallet
        transaction.setTransactionStatus("COMPLETED"); // Assuming the transaction was successful
        transaction.setTransactionDate(new Date());
        transaction.setReferenceNumber("TXN" + System.currentTimeMillis()); // Optional reference number

        // Save the transaction
        transactionRepository.save(transaction);

        // Update stock quantities in product
        for (Cart cart : cartItems) {
            Optional<Product> productOptional = productRepository.findById(cart.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                int newStock = product.getStockQuantity() - cart.getQuantity();
                if (newStock < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product ID: " + cart.getProductId());
                }
                product.setStockQuantity(newStock);
                productRepository.save(product);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for product ID: " + cart.getProductId());
            }
        }
        cartRepository.deleteByUserId(userId);

        return savedOrder;
    }


    @Override
    public void saveOrderHistory(Long userId, Long orderId, String status) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setUserId(userId);
        orderHistory.setOrderId(orderId);
        orderHistory.setStatus(status);
        orderHistoryRepository.save(orderHistory);
    }


    
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> 
            new RuntimeException("Order not found for ID: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
    }
	
}
