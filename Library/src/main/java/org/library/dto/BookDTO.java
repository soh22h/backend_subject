package org.library.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Integer bno;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String isbn;
    private Boolean availability;
    private String borrowerId;
}
