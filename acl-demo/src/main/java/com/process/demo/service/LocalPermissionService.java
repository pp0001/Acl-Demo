package com.process.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.process.demo.model.LocalEntity;
import com.process.demo.model.User;
import com.process.demo.repository.UserRepository;

@Service
@Transactional
public class LocalPermissionService {

    @Autowired
    private MutableAclService aclService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void addPermissionForUser(LocalEntity targetObj, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(targetObj, permission, sid);
    }

    public void addPermissionForAuthority(LocalEntity targetObj, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(targetObj, permission, sid);
    }
    
    private void addPermissionForSid(LocalEntity targetObj, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        List<Sid> listSid = java.util.Arrays.asList(sid);
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());
                MutableAcl acl = null;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi, listSid);
                    
                    for(int i = 0; i< acl.getEntries().size(); i++ ) {
                        if (acl.getEntries().get(i).getSid().equals(sid) && !acl.getEntries().get(i).getPermission().equals(permission)) {
                            acl.deleteAce(i);
                            acl.insertAce(acl.getEntries().size(), permission, sid, true);
                        };
                    }
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                    acl.insertAce(acl.getEntries().size(), permission, sid, true);
                    
                  //add entry for other role 
                    List<User> users = userRepository.findAll();
                    for(User user: users) {
                        if(sid.toString().indexOf(user.getEmail()) == -1) {
                            if(user.getRoleId().equals("admin")) {
                                acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, new PrincipalSid(user.getEmail()), true);
                            } else {
                                acl.insertAce(acl.getEntries().size(), BasePermission.READ, new PrincipalSid(user.getEmail()), true);
                            }
                        }
                    }
                    
                  //add entry for other role                   
//                    if(permission == CustomPermission.OWNER_CRU) {
//                        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, new GrantedAuthoritySid("ROLE_ADMIN"), true);
//                        acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
//                    }else if(permission == BasePermission.ADMINISTRATION) {
//                        acl.insertAce(acl.getEntries().size(), CustomPermission.OWNER_CRU, new GrantedAuthoritySid("ROLE_PO"), true);
//                        acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
//                    }
                    // change object identity owner
                    acl.setOwner(acl.getEntries().get(0).getSid()); 
                    
                }
                aclService.updateAcl(acl);
            }
        });
    }
    

}
