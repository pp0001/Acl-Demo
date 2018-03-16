package com.process.demo.service;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public class CustomPermission extends BasePermission {  

	private static final long serialVersionUID = 1L;
	
	protected CustomPermission(int mask, char code) {  
	    super(mask, code);  
	  }  
	  protected CustomPermission(int mask) {  
	    super(mask);  
	  }  
	  public static final Permission OWNER_CRU = new CustomPermission(1 << 5, 'O'); // 32  
} 
