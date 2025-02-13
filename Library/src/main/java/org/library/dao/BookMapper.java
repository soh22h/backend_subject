package org.library.dao;

import org.apache.ibatis.annotations.Mapper;
import org.library.dto.Book;
import org.library.dto.BookDTO;

import java.util.List;

@Mapper
public interface BookMapper {
    public List<Book> getBookList();
    public Book read(Integer bno);
    public int update(BookDTO book);
}
