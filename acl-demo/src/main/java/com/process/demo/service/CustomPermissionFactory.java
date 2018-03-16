package com.process.demo.service;

import java.util.Map;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.model.Permission;

public class CustomPermissionFactory extends DefaultPermissionFactory {  
	
	public CustomPermissionFactory() {  
	    super();  
	    registerPublicPermissions(CustomPermission.class);  
	}  
	  
	public CustomPermissionFactory(Class<? extends Permission> permissionClass) {  
	    super(permissionClass);  
	}  
	 
	public CustomPermissionFactory(Map<String, ? extends Permission> namedPermissions) {  
		super(namedPermissions);  
	}  
}   
