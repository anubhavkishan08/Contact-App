package com.project.ContactManager.config;

import com.project.ContactManager.dao.UserRepository;
import com.project.ContactManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

   @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching user from db
        User user=userRepository.getUserByUserName(username);

        if(user==null)
            throw new UsernameNotFoundException("Could not found user..!!!");

        CustomUserDetails customUserDetails=new CustomUserDetails(user);


        return customUserDetails;
    }
}
