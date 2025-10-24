package com.itcenter.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for paginated responses
 */
public class PageResponseDto<T> {
    
    private List<T> content;
    
    private int page;
    
    private int size;
    
    private long totalElements;
    
    private int totalPages;
    
    private boolean first;
    
    private boolean last;
    
    private boolean hasNext;
    
    private boolean hasPrevious;
    
    private LocalDateTime timestamp;
    
    // Constructors
    public PageResponseDto() {
        this.timestamp = LocalDateTime.now();
    }
    
    public PageResponseDto(List<T> content, int page, int size, long totalElements, int totalPages) {
        this();
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = page == 0;
        this.last = page == totalPages - 1;
        this.hasNext = page < totalPages - 1;
        this.hasPrevious = page > 0;
    }
    
    // Getters and Setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public boolean isFirst() {
        return first;
    }
    
    public void setFirst(boolean first) {
        this.first = first;
    }
    
    public boolean isLast() {
        return last;
    }
    
    public void setLast(boolean last) {
        this.last = last;
    }
    
    public boolean isHasNext() {
        return hasNext;
    }
    
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    
    public boolean isHasPrevious() {
        return hasPrevious;
    }
    
    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "PageResponseDto{" +
                "content=" + content +
                ", page=" + page +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", first=" + first +
                ", last=" + last +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", timestamp=" + timestamp +
                '}';
    }
}
