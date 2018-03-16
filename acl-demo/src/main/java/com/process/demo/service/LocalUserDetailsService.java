package com.process.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.process.demo.model.CurrentUser;
import com.process.demo.model.User;
import com.process.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class LocalUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
        
//        return new org.springframework.security.core.userdetails.User(
//        		  user.getEmail(), user.getPassword(), true, true, true, true, getAuthorities(user.getRoleId()));
//        		user.getRoleId(), user.getPassword(), true, true, true, true, getAuthorities(ROLE_ADMIN));
        return new CurrentUser(user);
    }
    
//    private List<SimpleGrantedAuthority> getAuthorities(String role) {
//        List<SimpleGrantedAuthority> authList = new ArrayList<>();
//        authList.add(new SimpleGrantedAuthority(ROLE_USER));
// 
//        //you can also add different roles here
//        //for example, the user is also an admin of the site, then you can add ROLE_ADMIN
//        //so that he can view pages that are ROLE_ADMIN specific
//        if (role != null && role.trim().length() > 0) {
//            if (role.equals("procedure_owner")) {
//                authList.add(new SimpleGrantedAuthority(ROLE_ADMIN));
//            }
//        }
// 
//        return authList;
//    }
//    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
//        return Arrays.asList(new SimpleGrantedAuthority(role));
//    }

}