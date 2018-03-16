package com.process.demo.config;

import net.sf.ehcache.CacheManager;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.acls.AclEntryVoter;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.process.demo.service.CustomPermission;
import com.process.demo.service.CustomPermissionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@Configuration
// @EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    // method security config wired in aclPermissionEvaluator
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        final DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator());
        return expressionHandler;
    }

    // ======= ACL configurations =======

    /**
     * AclPermissionEvaluator needs a aclService
     * @return
     */
    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator() {
        final AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService());
        aclPermissionEvaluator.setPermissionFactory(customPermissionFactory());
        return aclPermissionEvaluator;
    }

    /**
     * Define JDBC ACL service
     * These 2 simple SQL queries are MySQL specific, so if need supporting multiple databases,
     * you might want to put these in configuration, not in code.
     * @return
     */
    @Bean
    public JdbcMutableAclService aclService() {
        final JdbcMutableAclService service = new JdbcMutableAclService(dataSource(), lookupStrategy(), aclCache());
        // Those two line for MySQL only
        service.setClassIdentityQuery("SELECT @@IDENTITY");
        service.setSidIdentityQuery("SELECT @@IDENTITY");
        return service;
    }

    // lookup strategy
    @Bean
    public LookupStrategy lookupStrategy() {
        BasicLookupStrategy basicLookupStrategy = 
        		new BasicLookupStrategy(dataSource(), aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
        //  add custom permission
        basicLookupStrategy.setPermissionFactory(customPermissionFactory());
        return basicLookupStrategy;
    }

    // cache
    @Bean
    public AclCache aclCache() {
        final EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        final EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        cacheManager.setAcceptExisting(true);
        cacheManager.setCacheManagerName(CacheManager.getInstance().getName());
        cacheManager.afterPropertiesSet();

        factoryBean.setName("aclCache");
        factoryBean.setCacheManager(cacheManager.getObject());
        factoryBean.setMaxBytesLocalHeap("16M");
        factoryBean.setMaxEntriesLocalHeap(0L);
        factoryBean.afterPropertiesSet();
        return new EhCacheBasedAclCache(factoryBean.getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());

    }

    // which deals with access administrative methods
    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    // this allows us to actually customize the decision to grant a permission (or not) based on the ACL entry
    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    // data source
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
    
    // custom permission
    @Bean
    public CustomPermissionFactory customPermissionFactory() {  
    	return new CustomPermissionFactory();
    } 
    
    // custom entry voter
    @Bean 
    public AclEntryVoter aclEntryVoter() {
    	Permission[] requirePermission = new Permission[1];
    	requirePermission[0] = CustomPermission.OWNER_CRU;
    	AclEntryVoter aclEntryVoter = new AclEntryVoter(aclService(), "OWNER_CRU", requirePermission);
    	return aclEntryVoter;
    }
    
    // custom decision manager
	@Bean
    public AffirmativeBased aclDecisionManager() {
    	List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
    	decisionVoters.add(0, aclEntryVoter());
    	AffirmativeBased aclDecisionManager = new AffirmativeBased(decisionVoters);
    	return aclDecisionManager;
    }
}







