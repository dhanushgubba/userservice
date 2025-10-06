package com.project.userservice.service;

import java.util.List;

import com.project.userservice.model.Role;
import com.project.userservice.model.User;

public interface UserService {
	User register(User user);
	User login(String email, String password);
	List<User> getAllUsers(Role role);
	List<User> getAllMembers();
	List<User> getAllAdmins(Role role);
	public User addUser(User user);
	public String updateUser(User user);
	public String deleteUser(Long id); 
	public Long countAllUsers(Role role);
	public Long countAllAdmins(Role role);
	User promoteToAdmin(Long userId);
	User promoteToSuperAdmin(Long userId);
	
}
