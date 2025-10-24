package com.yushan.engagement_service.dto.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PageResponseDTO class.
 * Tests all constructors, static factory methods, getters/setters, and edge cases.
 */
@DisplayName("PageResponseDTO Tests")
class PageResponseDTOTest {

    @Test
    @DisplayName("Default constructor should create instance with default values")
    void testDefaultConstructor() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>();
        
        assertNotNull(pageResponse.getContent());
        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(0, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getTotalPages());
        assertEquals(0, pageResponse.getCurrentPage());
        assertEquals(0, pageResponse.getSize());
        assertFalse(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
        assertFalse(pageResponse.isHasNext());
        assertFalse(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Constructor with parameters should calculate pagination metadata correctly")
    void testConstructorWithParameters() {
        List<String> content = Arrays.asList("item1", "item2", "item3");
        long totalElements = 25L;
        int currentPage = 1;
        int size = 10;
        
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(content, totalElements, currentPage, size);
        
        assertEquals(content, pageResponse.getContent());
        assertEquals(25L, pageResponse.getTotalElements());
        assertEquals(1, pageResponse.getCurrentPage());
        assertEquals(10, pageResponse.getSize());
        assertEquals(3, pageResponse.getTotalPages()); // Math.ceil(25/10) = 3
        assertFalse(pageResponse.isFirst()); // currentPage = 1, not 0
        assertFalse(pageResponse.isLast()); // currentPage = 1, not >= 2
        assertTrue(pageResponse.isHasNext()); // not last page
        assertTrue(pageResponse.isHasPrevious()); // not first page
    }

    @Test
    @DisplayName("Constructor should handle null content by creating empty list")
    void testConstructorWithNullContent() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(null, 10L, 0, 5);
        
        assertNotNull(pageResponse.getContent());
        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(10L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getCurrentPage());
        assertEquals(5, pageResponse.getSize());
        assertEquals(2, pageResponse.getTotalPages()); // Math.ceil(10/5) = 2
        assertTrue(pageResponse.isFirst()); // currentPage = 0
        assertFalse(pageResponse.isLast()); // currentPage = 0, not >= 1
        assertTrue(pageResponse.isHasNext()); // not last page
        assertFalse(pageResponse.isHasPrevious()); // first page
    }

    @Test
    @DisplayName("Constructor should handle first page correctly")
    void testFirstPage() {
        List<String> content = Arrays.asList("item1", "item2");
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(content, 20L, 0, 10);
        
        assertTrue(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
        assertTrue(pageResponse.isHasNext());
        assertFalse(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Constructor should handle last page correctly")
    void testLastPage() {
        List<String> content = Arrays.asList("item1", "item2");
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(content, 20L, 1, 10);
        
        assertFalse(pageResponse.isFirst());
        assertTrue(pageResponse.isLast());
        assertFalse(pageResponse.isHasNext());
        assertTrue(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Constructor should handle single page correctly")
    void testSinglePage() {
        List<String> content = Arrays.asList("item1", "item2", "item3");
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(content, 3L, 0, 10);
        
        assertTrue(pageResponse.isFirst());
        assertTrue(pageResponse.isLast());
        assertFalse(pageResponse.isHasNext());
        assertFalse(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Constructor should handle empty result set correctly")
    void testEmptyResultSet() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(Collections.emptyList(), 0L, 0, 10);
        
        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(0L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getTotalPages());
        assertTrue(pageResponse.isFirst());
        assertTrue(pageResponse.isLast());
        assertFalse(pageResponse.isHasNext());
        assertFalse(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Static factory method should create instance correctly")
    void testStaticFactoryMethod() {
        List<String> content = Arrays.asList("item1", "item2");
        PageResponseDTO<String> pageResponse = PageResponseDTO.of(content, 15L, 0, 5);
        
        assertEquals(content, pageResponse.getContent());
        assertEquals(15L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getCurrentPage());
        assertEquals(5, pageResponse.getSize());
        assertEquals(3, pageResponse.getTotalPages()); // Math.ceil(15/5) = 3
    }

    @Test
    @DisplayName("Getters and setters should work correctly")
    void testGettersAndSetters() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>();
        List<String> content = Arrays.asList("item1", "item2", "item3");
        
        pageResponse.setContent(content);
        pageResponse.setTotalElements(30L);
        pageResponse.setTotalPages(3);
        pageResponse.setCurrentPage(1);
        pageResponse.setSize(10);
        pageResponse.setFirst(false);
        pageResponse.setLast(false);
        pageResponse.setHasNext(true);
        pageResponse.setHasPrevious(true);
        
        assertEquals(content, pageResponse.getContent());
        assertEquals(30L, pageResponse.getTotalElements());
        assertEquals(3, pageResponse.getTotalPages());
        assertEquals(1, pageResponse.getCurrentPage());
        assertEquals(10, pageResponse.getSize());
        assertFalse(pageResponse.isFirst());
        assertFalse(pageResponse.isLast());
        assertTrue(pageResponse.isHasNext());
        assertTrue(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("setContent should handle null by creating empty list")
    void testSetContentWithNull() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>();
        pageResponse.setContent(null);
        
        assertNotNull(pageResponse.getContent());
        assertTrue(pageResponse.getContent().isEmpty());
    }

    @Test
    @DisplayName("getContent should return defensive copy")
    void testGetContentDefensiveCopy() {
        List<String> originalContent = new ArrayList<>(Arrays.asList("item1", "item2"));
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(originalContent, 10L, 0, 5);
        
        List<String> retrievedContent = pageResponse.getContent();
        retrievedContent.add("item3");
        
        // Original content should not be modified
        assertEquals(2, originalContent.size());
        assertEquals(2, pageResponse.getContent().size());
        assertNotSame(originalContent, retrievedContent);
    }

    @Test
    @DisplayName("setContent should create defensive copy")
    void testSetContentDefensiveCopy() {
        List<String> originalContent = new ArrayList<>(Arrays.asList("item1", "item2"));
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>();
        
        pageResponse.setContent(originalContent);
        originalContent.add("item3");
        
        // PageResponse content should not be modified
        assertEquals(2, pageResponse.getContent().size());
        assertNotSame(originalContent, pageResponse.getContent());
    }

    @Test
    @DisplayName("PageResponseDTO should work with different generic types")
    void testGenericTypes() {
        // Test with Integer
        List<Integer> intContent = Arrays.asList(1, 2, 3);
        PageResponseDTO<Integer> intPageResponse = new PageResponseDTO<>(intContent, 10L, 0, 5);
        assertEquals(intContent, intPageResponse.getContent());
        
        // Test with custom object
        List<TestObject> objectContent = Arrays.asList(new TestObject("test1"), new TestObject("test2"));
        PageResponseDTO<TestObject> objectPageResponse = new PageResponseDTO<>(objectContent, 5L, 0, 2);
        assertEquals(objectContent, objectPageResponse.getContent());
    }

    @Test
    @DisplayName("Total pages calculation should handle edge cases")
    void testTotalPagesCalculation() {
        // Test exact division
        PageResponseDTO<String> exactDivision = new PageResponseDTO<>(Arrays.asList("item1"), 10L, 0, 5);
        assertEquals(2, exactDivision.getTotalPages()); // Math.ceil(10/5) = 2
        
        // Test remainder
        PageResponseDTO<String> withRemainder = new PageResponseDTO<>(Arrays.asList("item1"), 11L, 0, 5);
        assertEquals(3, withRemainder.getTotalPages()); // Math.ceil(11/5) = 3
        
        // Test zero total elements
        PageResponseDTO<String> zeroElements = new PageResponseDTO<>(Collections.emptyList(), 0L, 0, 5);
        assertEquals(0, zeroElements.getTotalPages()); // Math.ceil(0/5) = 0
    }

    @Test
    @DisplayName("Edge case: current page beyond total pages")
    void testCurrentPageBeyondTotalPages() {
        List<String> content = Arrays.asList("item1");
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(content, 5L, 10, 5);
        
        assertEquals(10, pageResponse.getCurrentPage());
        assertEquals(1, pageResponse.getTotalPages());
        assertFalse(pageResponse.isFirst());
        assertTrue(pageResponse.isLast()); // currentPage >= totalPages - 1
        assertFalse(pageResponse.isHasNext());
        assertTrue(pageResponse.isHasPrevious());
    }

    @Test
    @DisplayName("Edge case: negative values")
    void testNegativeValues() {
        PageResponseDTO<String> pageResponse = new PageResponseDTO<>(Arrays.asList("item1"), -5L, -1, -2);
        
        assertEquals(-5L, pageResponse.getTotalElements());
        assertEquals(-1, pageResponse.getCurrentPage());
        assertEquals(-2, pageResponse.getSize());
        assertEquals(3, pageResponse.getTotalPages()); // Math.ceil(-5/-2) = 3
        assertFalse(pageResponse.isFirst()); // currentPage = -1, not 0
        assertFalse(pageResponse.isLast()); // currentPage = -1, not >= 2
    }

    // Helper class for testing generic types
    private static class TestObject {
        private String value;
        
        public TestObject(String value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestObject that = (TestObject) obj;
            return value != null ? value.equals(that.value) : that.value == null;
        }
        
        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}
