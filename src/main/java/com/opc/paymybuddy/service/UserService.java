package com.opc.paymybuddy.service;

import com.opc.paymybuddy.dto.UserDto;
import com.opc.paymybuddy.model.User;

import java.util.List;


public interface UserService {

    List<User> findAll();
    long count();
    User findByEmail(String email);

    void addUser(UserDto newUser) throws Exception;





    /*
    public List<User> findBuddyByUser(User relation);
    public User addBuddy(User newBuddy);
*/
}
