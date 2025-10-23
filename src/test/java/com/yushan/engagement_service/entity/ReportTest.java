package com.yushan.engagement_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

class ReportTest {

    private Report report;
    private UUID testUuid;
    private UUID testReporterId;
    private UUID testResolvedBy;
    private Date testCreatedAt;
    private Date testUpdatedAt;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testReporterId = UUID.randomUUID();
        testResolvedBy = UUID.randomUUID();
        testCreatedAt = new Date();
        testUpdatedAt = new Date(testCreatedAt.getTime() + 1000);
        report = new Report();
    }

    @Test
    void testDefaultConstructor() {
        Report newReport = new Report();
        assertNotNull(newReport);
        assertNull(newReport.getId());
        assertNull(newReport.getUuid());
        assertNull(newReport.getReporterId());
        assertNull(newReport.getReportType());
        assertNull(newReport.getReason());
        assertNull(newReport.getStatus());
        assertNull(newReport.getAdminNotes());
        assertNull(newReport.getResolvedBy());
        assertNull(newReport.getCreatedAt());
        assertNull(newReport.getUpdatedAt());
        assertNull(newReport.getContentType());
        assertNull(newReport.getContentId());
    }

    @Test
    void testParameterizedConstructor() {
        Report newReport = new Report(1, testUuid, testReporterId, "SPAM", "Inappropriate content", 
                "PENDING", "Admin notes", testResolvedBy, testCreatedAt, testUpdatedAt, "COMMENT", 123);
        
        assertEquals(1, newReport.getId());
        assertEquals(testUuid, newReport.getUuid());
        assertEquals(testReporterId, newReport.getReporterId());
        assertEquals("SPAM", newReport.getReportType());
        assertEquals("Inappropriate content", newReport.getReason());
        assertEquals("PENDING", newReport.getStatus());
        assertEquals("Admin notes", newReport.getAdminNotes());
        assertEquals(testResolvedBy, newReport.getResolvedBy());
        assertEquals(testCreatedAt.getTime(), newReport.getCreatedAt().getTime());
        assertEquals(testUpdatedAt.getTime(), newReport.getUpdatedAt().getTime());
        assertEquals("COMMENT", newReport.getContentType());
        assertEquals(123, newReport.getContentId());
    }

    @Test
    void testParameterizedConstructorWithNullDates() {
        Report newReport = new Report(1, testUuid, testReporterId, "SPAM", "Inappropriate content", 
                "PENDING", "Admin notes", testResolvedBy, null, null, "COMMENT", 123);
        
        assertEquals(1, newReport.getId());
        assertEquals(testUuid, newReport.getUuid());
        assertEquals(testReporterId, newReport.getReporterId());
        assertEquals("SPAM", newReport.getReportType());
        assertEquals("Inappropriate content", newReport.getReason());
        assertEquals("PENDING", newReport.getStatus());
        assertEquals("Admin notes", newReport.getAdminNotes());
        assertEquals(testResolvedBy, newReport.getResolvedBy());
        assertNull(newReport.getCreatedAt());
        assertNull(newReport.getUpdatedAt());
        assertEquals("COMMENT", newReport.getContentType());
        assertEquals(123, newReport.getContentId());
    }

    @Test
    void testIdGetterAndSetter() {
        report.setId(123);
        assertEquals(123, report.getId());
        
        report.setId(null);
        assertNull(report.getId());
    }

    @Test
    void testUuidGetterAndSetter() {
        report.setUuid(testUuid);
        assertEquals(testUuid, report.getUuid());
        
        report.setUuid(null);
        assertNull(report.getUuid());
    }

    @Test
    void testReporterIdGetterAndSetter() {
        report.setReporterId(testReporterId);
        assertEquals(testReporterId, report.getReporterId());
        
        report.setReporterId(null);
        assertNull(report.getReporterId());
    }

    @Test
    void testReportTypeGetterAndSetter() {
        report.setReportType("SPAM");
        assertEquals("SPAM", report.getReportType());
        
        report.setReportType("  HARASSMENT  ");
        assertEquals("HARASSMENT", report.getReportType());
        
        report.setReportType(null);
        assertNull(report.getReportType());
    }

    @Test
    void testReasonGetterAndSetter() {
        report.setReason("Inappropriate content");
        assertEquals("Inappropriate content", report.getReason());
        
        report.setReason("  Spam content  ");
        assertEquals("Spam content", report.getReason());
        
        report.setReason(null);
        assertNull(report.getReason());
    }

    @Test
    void testStatusGetterAndSetter() {
        report.setStatus("PENDING");
        assertEquals("PENDING", report.getStatus());
        
        report.setStatus("  RESOLVED  ");
        assertEquals("RESOLVED", report.getStatus());
        
        report.setStatus(null);
        assertNull(report.getStatus());
    }

    @Test
    void testAdminNotesGetterAndSetter() {
        report.setAdminNotes("Admin notes");
        assertEquals("Admin notes", report.getAdminNotes());
        
        report.setAdminNotes("  Admin notes with spaces  ");
        assertEquals("Admin notes with spaces", report.getAdminNotes());
        
        report.setAdminNotes(null);
        assertNull(report.getAdminNotes());
    }

    @Test
    void testResolvedByGetterAndSetter() {
        report.setResolvedBy(testResolvedBy);
        assertEquals(testResolvedBy, report.getResolvedBy());
        
        report.setResolvedBy(null);
        assertNull(report.getResolvedBy());
    }

    @Test
    void testCreatedAtGetterAndSetter() {
        report.setCreatedAt(testCreatedAt);
        assertNotNull(report.getCreatedAt());
        assertEquals(testCreatedAt.getTime(), report.getCreatedAt().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = report.getCreatedAt();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testCreatedAt.getTime(), report.getCreatedAt().getTime());
        
        report.setCreatedAt(null);
        assertNull(report.getCreatedAt());
    }

    @Test
    void testUpdatedAtGetterAndSetter() {
        report.setUpdatedAt(testUpdatedAt);
        assertNotNull(report.getUpdatedAt());
        assertEquals(testUpdatedAt.getTime(), report.getUpdatedAt().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = report.getUpdatedAt();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testUpdatedAt.getTime(), report.getUpdatedAt().getTime());
        
        report.setUpdatedAt(null);
        assertNull(report.getUpdatedAt());
    }

    @Test
    void testContentTypeGetterAndSetter() {
        report.setContentType("COMMENT");
        assertEquals("COMMENT", report.getContentType());
        
        report.setContentType("  REVIEW  ");
        assertEquals("REVIEW", report.getContentType());
        
        report.setContentType(null);
        assertNull(report.getContentType());
    }

    @Test
    void testContentIdGetterAndSetter() {
        report.setContentId(456);
        assertEquals(456, report.getContentId());
        
        report.setContentId(0);
        assertEquals(0, report.getContentId());
        
        report.setContentId(null);
        assertNull(report.getContentId());
    }

    @Test
    void testDateImmutabilityInConstructor() {
        Date originalCreatedAt = new Date();
        Date originalUpdatedAt = new Date(originalCreatedAt.getTime() + 1000);
        
        Report newReport = new Report(1, testUuid, testReporterId, "SPAM", "Reason", 
                "PENDING", "Notes", testResolvedBy, originalCreatedAt, originalUpdatedAt, "COMMENT", 123);
        
        // Modify original dates
        originalCreatedAt.setTime(originalCreatedAt.getTime() + 5000);
        originalUpdatedAt.setTime(originalUpdatedAt.getTime() + 5000);
        
        // Entity dates should not be affected
        assertNotEquals(originalCreatedAt.getTime(), newReport.getCreatedAt().getTime());
        assertNotEquals(originalUpdatedAt.getTime(), newReport.getUpdatedAt().getTime());
    }

    @Test
    void testDateImmutabilityInSetters() {
        Date originalTime = new Date();
        report.setCreatedAt(originalTime);
        
        // Modify original date
        originalTime.setTime(originalTime.getTime() + 5000);
        
        // Entity date should not be affected
        assertNotEquals(originalTime.getTime(), report.getCreatedAt().getTime());
    }

    @Test
    void testAllFieldsSet() {
        report.setId(1);
        report.setUuid(testUuid);
        report.setReporterId(testReporterId);
        report.setReportType("SPAM");
        report.setReason("Inappropriate content");
        report.setStatus("PENDING");
        report.setAdminNotes("Admin notes");
        report.setResolvedBy(testResolvedBy);
        report.setCreatedAt(testCreatedAt);
        report.setUpdatedAt(testUpdatedAt);
        report.setContentType("COMMENT");
        report.setContentId(123);
        
        assertEquals(1, report.getId());
        assertEquals(testUuid, report.getUuid());
        assertEquals(testReporterId, report.getReporterId());
        assertEquals("SPAM", report.getReportType());
        assertEquals("Inappropriate content", report.getReason());
        assertEquals("PENDING", report.getStatus());
        assertEquals("Admin notes", report.getAdminNotes());
        assertEquals(testResolvedBy, report.getResolvedBy());
        assertEquals(testCreatedAt.getTime(), report.getCreatedAt().getTime());
        assertEquals(testUpdatedAt.getTime(), report.getUpdatedAt().getTime());
        assertEquals("COMMENT", report.getContentType());
        assertEquals(123, report.getContentId());
    }

    @Test
    void testStringTrimmingBehavior() {
        report.setReportType("  SPAM  ");
        assertEquals("SPAM", report.getReportType());
        
        report.setReason("  Inappropriate content  ");
        assertEquals("Inappropriate content", report.getReason());
        
        report.setStatus("  PENDING  ");
        assertEquals("PENDING", report.getStatus());
        
        report.setAdminNotes("  Admin notes  ");
        assertEquals("Admin notes", report.getAdminNotes());
        
        report.setContentType("  COMMENT  ");
        assertEquals("COMMENT", report.getContentType());
    }
}
