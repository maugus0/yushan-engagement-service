package com.yushan.engagement_service.security;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomMethodSecurityExpressionHandlerTest {

    private CustomMethodSecurityExpressionHandler handler;

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private MethodInvocation methodInvocation;

    @Mock
    private Authentication authentication;

    @Mock
    private Expression expression;

    @BeforeEach
    void setUp() {
        handler = new CustomMethodSecurityExpressionHandler();
    }

    @Test
    void createEvaluationContext_ShouldCreateContextWithCustomRoot() throws Exception {
        // Arrange
        Method method = TestClass.class.getMethod("testMethod", String.class);
        when(methodInvocation.getMethod()).thenReturn(method);
        when(methodInvocation.getArguments()).thenReturn(new Object[]{"test"});

        // Act
        EvaluationContext context = handler.createEvaluationContext(authentication, methodInvocation);

        // Assert
        assertNotNull(context);
        assertNotNull(context.getRootObject().getValue());
        assertTrue(context.getRootObject().getValue() instanceof CustomSecurityExpressionRoot);
    }

    @Test
    void createEvaluationContext_WithBeanFactory_ShouldSetBeanResolver() throws Exception {
        // Arrange
        handler.setBeanFactory(beanFactory);
        Method method = TestClass.class.getMethod("testMethod", String.class);
        when(methodInvocation.getMethod()).thenReturn(method);
        when(methodInvocation.getArguments()).thenReturn(new Object[]{"test"});

        // Act
        EvaluationContext context = handler.createEvaluationContext(authentication, methodInvocation);

        // Assert
        assertNotNull(context);
        verify(beanFactory, never()).getBean(anyString()); // BeanFactoryResolver doesn't call getBean during setup
    }

    @Test
    void createEvaluationContext_ShouldSetMethodArgumentsAsVariables() throws Exception {
        // Arrange
        Method method = TestClass.class.getMethod("testMethod", String.class);
        when(methodInvocation.getMethod()).thenReturn(method);
        when(methodInvocation.getArguments()).thenReturn(new Object[]{"testArg"});

        // Act
        EvaluationContext context = handler.createEvaluationContext(authentication, methodInvocation);

        // Assert
        assertNotNull(context);
        assertEquals("testArg", context.lookupVariable("arg"));
    }

    @Test
    void createSecurityExpressionRoot_ShouldReturnCustomRoot() {
        // Act
        var result = handler.createSecurityExpressionRoot(authentication, methodInvocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CustomSecurityExpressionRoot);
    }

    @Test
    void setReturnObject_WithCustomRoot_ShouldSetReturnObject() {
        // Arrange
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication);
        EvaluationContext context = mock(EvaluationContext.class);
        org.springframework.expression.TypedValue typedValue = mock(org.springframework.expression.TypedValue.class);
        when(context.getRootObject()).thenReturn(typedValue);
        when(typedValue.getValue()).thenReturn(root);

        // Act
        handler.setReturnObject("testReturn", context);

        // Assert
        assertEquals("testReturn", root.getReturnObject());
    }

    @Test
    void setReturnObject_WithNonCustomRoot_ShouldNotThrowException() {
        // Arrange
        EvaluationContext context = mock(EvaluationContext.class);
        org.springframework.expression.TypedValue typedValue = mock(org.springframework.expression.TypedValue.class);
        when(context.getRootObject()).thenReturn(typedValue);
        when(typedValue.getValue()).thenReturn("notCustomRoot");

        // Act & Assert
        assertDoesNotThrow(() -> handler.setReturnObject("testReturn", context));
    }

    @Test
    void setFilterObject_WithCustomRoot_ShouldSetFilterObject() {
        // Arrange
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication);
        EvaluationContext context = mock(EvaluationContext.class);
        org.springframework.expression.TypedValue typedValue = mock(org.springframework.expression.TypedValue.class);
        when(context.getRootObject()).thenReturn(typedValue);
        when(typedValue.getValue()).thenReturn(root);

        // Act
        handler.setFilterObject("testFilter", context);

        // Assert
        assertEquals("testFilter", root.getFilterObject());
    }

    @Test
    void setFilterObject_WithNonCustomRoot_ShouldNotThrowException() {
        // Arrange
        EvaluationContext context = mock(EvaluationContext.class);
        org.springframework.expression.TypedValue typedValue = mock(org.springframework.expression.TypedValue.class);
        when(context.getRootObject()).thenReturn(typedValue);
        when(typedValue.getValue()).thenReturn("notCustomRoot");

        // Act & Assert
        assertDoesNotThrow(() -> handler.setFilterObject("testFilter", context));
    }

    @Test
    void filter_ShouldReturnFilterTarget() {
        // Arrange
        Object filterTarget = "testTarget";
        EvaluationContext context = mock(EvaluationContext.class);

        // Act
        Object result = handler.filter(filterTarget, expression, context);

        // Assert
        assertEquals(filterTarget, result);
    }

    @Test
    void getExpressionParser_ShouldReturnSpelParser() {
        // Act
        var parser = handler.getExpressionParser();

        // Assert
        assertNotNull(parser);
        assertTrue(parser instanceof org.springframework.expression.spel.standard.SpelExpressionParser);
    }

    @Test
    void setBeanFactory_ShouldSetBeanFactory() {
        // Act
        handler.setBeanFactory(beanFactory);

        // Assert
        // BeanFactory is private, so we can't directly verify it
        // But we can verify it doesn't throw an exception
        assertDoesNotThrow(() -> handler.setBeanFactory(beanFactory));
    }

    @Test
    void createEvaluationContext_WithNoArguments_ShouldHandleEmptyArguments() throws Exception {
        // Arrange
        Method method = TestClass.class.getMethod("noArgMethod");
        when(methodInvocation.getMethod()).thenReturn(method);
        when(methodInvocation.getArguments()).thenReturn(new Object[]{});

        // Act
        EvaluationContext context = handler.createEvaluationContext(authentication, methodInvocation);

        // Assert
        assertNotNull(context);
        assertNotNull(context.getRootObject().getValue());
    }

    @Test
    void createEvaluationContext_WithMultipleArguments_ShouldSetAllVariables() throws Exception {
        // Arrange
        Method method = TestClass.class.getMethod("multiArgMethod", String.class, Integer.class);
        when(methodInvocation.getMethod()).thenReturn(method);
        when(methodInvocation.getArguments()).thenReturn(new Object[]{"test", 123});

        // Act
        EvaluationContext context = handler.createEvaluationContext(authentication, methodInvocation);

        // Assert
        assertNotNull(context);
        assertEquals("test", context.lookupVariable("arg"));
        assertEquals(123, context.lookupVariable("number"));
    }

    // Test helper class
    private static class TestClass {
        public void testMethod(String arg) {}
        public void noArgMethod() {}
        public void multiArgMethod(String arg, Integer number) {}
    }
}
