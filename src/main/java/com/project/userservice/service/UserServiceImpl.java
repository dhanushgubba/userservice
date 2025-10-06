package com.project.userservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.userservice.model.Role;
import com.project.userservice.model.User;
import com.project.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public User register(User user) {
		user.setRole(Role.USER);
		return userRepository.save(user);
	}

	@Override
	public User login(String email, String password) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		if(user!=null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	@Override
	public List<User> getAllUsers(Role role) {
		// TODO Auto-generated method stub
		return userRepository.findByRole(role);
	}

	@Override
	public User promoteToAdmin(Long userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow();
		user.setRole(Role.ADMIN);
		return userRepository.save(user);
	}

	@Override
	public User promoteToSuperAdmin(Long userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow();
		user.setRole(Role.SUPER_ADMIN);
		return userRepository.save(user);
	}

	@Override
	public String updateUser(User updatedUser) {
	    if (updatedUser.getId() == null) {
	        throw new IllegalArgumentException("User ID must not be null");
	    }

	    User existingUser = userRepository.findById(updatedUser.getId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    existingUser.setName(updatedUser.getName());
	    existingUser.setEmail(updatedUser.getEmail());
	    existingUser.setAddress(updatedUser.getAddress());
	    existingUser.setPhone(updatedUser.getPhone());
	    existingUser.setPostalcode(updatedUser.getPostalcode());
	    existingUser.setPassword(updatedUser.getPassword());
	    existingUser.setRole(updatedUser.getRole());

	    userRepository.save(existingUser);
	    return "User updated successfully!";
	}


	@Override
	public String deleteUser(Long id) {
		// TODO Auto-generated method stub
		Optional<User> obj = userRepository.findById(id);
		String message = null;
		if(obj.isPresent()) {
			User u = obj.get();
			userRepository.delete(u);
			message = "User Deleted Successfully";
		}else {
			message = "User ID not found";
		}
		return message;
		
	}

	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllMembers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public List<User> getAllAdmins(Role role) {
		// TODO Auto-generated method stub
		return userRepository.findByRole(role);
	}

	@Override
	public Long countAllUsers(Role role) {
		// TODO Auto-generated method stub
		if(role == Role.USER) {
			return (long) userRepository.findByRole(Role.USER).size();
		}
		return 0L;
	}

	@Override
	public Long countAllAdmins(Role role) {
		// TODO Auto-generated method stub
		if(role == Role.ADMIN) {
			return (long) userRepository.findByRole(Role.ADMIN).size();
		}
		return 0L;
	}
}
