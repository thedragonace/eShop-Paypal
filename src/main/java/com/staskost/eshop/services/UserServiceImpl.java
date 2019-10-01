package com.staskost.eshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.staskost.eshop.model.User;
import com.staskost.eshop.repos.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void createUser(User user) {
		userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public User getById(int id) {
		Optional<User> optional = userRepository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			return user;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}

	public List<User> getAll() {
		List<User> users = userRepository.findAll();
		return users;
	}

	public void givePointsToLoyal(double total, User user) {
		if (user.getIsLoyal() == 1) {
			int roundedTotal = (int) Math.round(total);
			if (roundedTotal > 700) {
				user.setPoints(user.getPoints() + 70);
			} else {
				user.setPoints(user.getPoints() + (roundedTotal / 10));
			}
		} else {
			user.setPoints(user.getPoints());
		}
	}

	private double calculatePercentage(double obtained, double total) {
		return obtained * 100 / total;
	}

	public double getTotalAfterDiscount(double total, User user) {
		double price = 0;
		double pc = 0;
		if (user.getIsLoyal() == 1) {
			int points = user.getPoints();
			if ((points > 0) & (points < 700)) {
				pc = calculatePercentage(points / 10, total);
				price = total - pc;
				user.setPoints(0);
			} else {
				pc = calculatePercentage(70, total);
				price = total - pc;
				user.setPoints(0);
			}
		} else {
			price = total;
		}
		return price;
	}

	public void withdraw(double total) {
		// card transaction here
		System.out.println("Your transaction was successfull");
	}

}