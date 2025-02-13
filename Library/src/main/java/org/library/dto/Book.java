package org.library.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {
    private Integer bno; // 번호 (자동 증가)
    private String title; // 도서 이름
    private String author; // 저자
    private String publisher; // 출판사
    private String description; // 내용 요약
    private String isbn; // ISBN
    private Boolean availability; // 대출 가능 여부 (0: 불가, 1: 가능)
    private String borrowerId; // 대출자 아이디
    private LocalDateTime startDate; // 도서 대출일
    private LocalDateTime endDate; // 도서 반납일
}