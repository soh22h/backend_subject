package org.library.service;

import lombok.extern.log4j.Log4j2;
import org.library.dao.BookMapper;
import org.library.dto.Book;
import org.library.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;

    public List<Book> getAllBooks() {
        log.info("getAllbooks");
        return bookMapper.getBookList();
    }

    public Book read(Integer bno) {
        log.info("read: " + bno);
        return bookMapper.read(bno);
    }

    public boolean borrow(BookDTO book) {
        log.info("borrow: " + book);
        return bookMapper.update(book) == 1;
    }
}
