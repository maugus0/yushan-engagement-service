package com.yushan.engagement_service.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * Custom Method Security Expression Handler
 * 
 * This class provides custom expression evaluation for method-level security
 * by using our custom SecurityExpressionRoot
 */
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication);
        return root;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
