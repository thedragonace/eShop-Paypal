package com.staskost.eshop.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staskost.eshop.model.Cart;
import com.staskost.eshop.model.Product;
import com.staskost.eshop.model.User;
import com.staskost.eshop.repos.CartRepository;

@Service
public class CartServiseImpl implements CartService {

	private CartRepository cartRepository;
	
	private UserService userService;

	public CartServiseImpl(CartRepository cartRepository, UserService userService) {
		this.cartRepository = cartRepository;
		this.userService = userService;
	}

	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		cartRepository.save(cart);
		return cart;
	}

	public Cart getUsersCart(User user) {
		Cart cart = cartRepository.findByUser(user);
		return cart;
	}

	public void addProduct(Cart cart, Product product) {
		cart.addProcuct(product);
		cartRepository.save(cart);
	}

	public void removeProduct(Cart cart, Product product) {
		cart.removeProduct(product);
		cartRepository.save(cart);
	}

	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}

	public double getTotal(Cart cart) {
		List<Product> products = cart.getCartProducts();
		double sum = 0.0;
		for (Product p : products) {
			sum += p.getPrice();
		}
		return sum;
	}

	public void checkout(User user, Cart cart) {
		double total = getTotal(cart);
		double totalAfterDiscount = userService.getTotalAfterDiscount(total, user);
		userService.withdraw(totalAfterDiscount);
		userService.givePointsToLoyal(totalAfterDiscount, user);
		cartRepository.delete(cart);
	}
}