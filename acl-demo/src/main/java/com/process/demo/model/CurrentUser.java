package com.process.demo.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_PO = "ROLE_PO";
	
	private User user;
	
	public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), getAuthorities(user.getRoleId()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    public Long getId() {
        return user.getId();
    }

    public String getRole() {
        return user.getRoleId();
    }
    
    private static List<SimpleGrantedAuthority> getAuthorities(String role) {
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(ROLE_USER));

        if (role != null && role.trim().length() > 0) {
            if (role.equals("procedure_owner")) {
                authList.add(new SimpleGrantedAuthority(ROLE_PO));
            } else if (role.equals("admin")) {
            	authList.add(new SimpleGrantedAuthority(ROLE_ADMIN));
            }
        }
 
        return authList;
    }
    
    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
}
}
