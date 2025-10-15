package com.project.userservice.controller;

import com.project.userservice.model.Role;
import com.project.userservice.model.User;
import com.project.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // React frontend
public class UserController {

    @Autowired
    private UserService userService;

    // Register
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // Login
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }

    // Get all users (ADMIN or SUPER_ADMIN only in real app)
    @GetMapping("/all")
    public List<User> getAllUsers() {
    	return userService.getAllUsers(Role.USER);
    }

    // Promote to Admin (SUPER_ADMIN only)
    @PutMapping("/{id}/promote-admin")
    public User promoteToAdmin(@PathVariable Long id) {
        return userService.promoteToAdmin(id);
    }

    // Promote to SuperAdmin (SUPER_ADMIN only)
    @PutMapping("/{id}/promote-superadmin")
    public User promoteToSuperAdmin(@PathVariable Long id) {
        return userService.promoteToSuperAdmin(id);
    }
    
    
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
    	userService.addUser(user);
    	return "User Added Successfully";
    }
    
    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return userService.updateUser(updatedUser);   
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    @GetMapping("/allusers")
    public List<User> getAllMembers() {
    	return userService.getAllMembers();
    }
    
    @GetMapping("/alladmins")
    public List<User> getAllAdmins() {
    	return userService.getAllAdmins(Role.ADMIN);
    }
    @GetMapping("/getcount")
    public Long getCountofUsers() {
    	return userService.countAllUsers(Role.USER);
    } 
    @GetMapping("/getcountadmins")
    public Long getCountofAdmins() {
    	return userService.countAllAdmins(Role.ADMIN);
    }
}
