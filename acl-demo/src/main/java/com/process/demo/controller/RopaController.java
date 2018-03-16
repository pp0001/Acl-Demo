package com.process.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.process.demo.model.Ropa;
import com.process.demo.model.User;
import com.process.demo.repository.RopaRepository;
import com.process.demo.repository.UserRepository;
import com.process.demo.service.CustomPermission;
import com.process.demo.service.CustomService;
import com.process.demo.service.CustomServiceImpl;
import com.process.demo.service.LocalPermissionService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/ropa")
public class RopaController {

    @Autowired
    private RopaRepository ropaRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MutableAclService aclService;
    
    @Autowired
    private LocalPermissionService permissionService;
    
    @Autowired
    private CustomServiceImpl CustomServiceImpl;
    
    @RequestMapping
    public ModelAndView list() {
        Iterable<Ropa> ropas = this.ropaRepository.findAll();
        return new ModelAndView("tl/ropas", "ropas", ropas);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PostAuthorize("hasPermission(returnObject, 'READ') "
    		+ "or hasPermission(returnObject, 'ADMINISTRATION') "
    		+ "or hasPermission(returnObject, 'OWNER_CRU')")
    public Ropa findOne(@PathVariable("id") final Long id) {
        return ropaRepository.findOne(id);
    }
    
    @PreAuthorize("hasPermission(#ropa, 'WRITE') "
    		+ "or hasPermission(#ropa, 'ADMINISTRATION') "
    		+ "or hasPermission(#ropa, 'OWNER_CRU')")
    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") final Ropa ropa) {
        return new ModelAndView("tl/edit", "ropa", ropa);
    }
    
    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable("id") final Long id) {
    	Ropa deletedRopa = ropaRepository.findOne(id);
    	if(CustomServiceImpl.delete(deletedRopa)) {
    		ropaRepository.delete(deletedRopa);
    		//update acl_object_identity
    		final ObjectIdentity oi = new ObjectIdentityImpl(deletedRopa.getClass(), deletedRopa.getId());
    		MutableAcl acl = (MutableAcl) aclService.readAclById(oi);
    		aclService.deleteAcl(oi, true);
    	}
        return new ModelAndView("redirect:/ropa");
    }
	
    @PostAuthorize("hasPermission(#ropa, 'CREATE') "
    		+ "or hasPermission(#ropa, 'ADMINISTRATION') "
    		+ "or hasPermission(#ropa, 'OWNER_CRU')")
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid Ropa ropa, Authentication authentication) {
    	ropa = ropaRepository.save(ropa);
        System.out.println(ropa.toString());
        Set<User> users = ropa.getOwner();
        for(User user: users) {
            if(user.getRoleId().equals("procedure_owner")) {
                permissionService.addPermissionForUser(ropa, CustomPermission.OWNER_CRU, user.getEmail());
            }else if(user.getRoleId().equals("admin")) {
                permissionService.addPermissionForUser(ropa, BasePermission.ADMINISTRATION, user.getEmail());
            }
        }
//        Iterator<User> ropaIterator = ropa.getOwner().iterator();
//        while(ropaIterator.hasNext()) {
//            if(ropaIterator.next().getRoleId().equals("procedure_owner")) {
//            	permissionService.addPermissionForUser(ropa, CustomPermission.OWNER_CRU, ropaIterator.next().getEmail());
//            } else if(ropaIterator.next().getRoleId().equals("admin")) {
//            	permissionService.addPermissionForUser(ropa, BasePermission.ADMINISTRATION, ropaIterator.next().getEmail());
//            }
//        }
        	
        return new ModelAndView("redirect:/ropa?message=Ropa with id " + ropa.getId());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PO')")
//    @PreAuthorize("hasPermission(#ropa, 'CREATE') "
//    		+ "or hasPermission(#ropa, 'ADMINISTRATION') "
//    		+ "or hasPermission(returnObject, 'OWNER_CRU')")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute final Ropa ropa) {
        return "tl/ropa";
    }

}