package com.yushan.engagement_service.dto.report;

import lombok.Data;

@Data
public class ReportSearchRequestDTO {
    private String status;
    private String reportType;
    private String search;
    private String sort = "createdAt";
    private String order = "desc";
    private int page = 0;
    private int size = 10;
}