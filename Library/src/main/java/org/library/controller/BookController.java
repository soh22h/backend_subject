package org.library.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.library.dto.BookDTO;
import org.library.service.BookService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Log4j2
@Controller
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @GetMapping("/list")
    public void list(@RequestParam(value = "lang", required = false) String lang, Model model) {
        if (lang != null) {
            Locale locale = new Locale(lang.split("-")[0], lang.split("-")[1]);
            LocaleContextHolder.setLocale(locale);
            log.info(locale);
        }
        model.addAttribute("bList", bookService.getAllBooks());
    }

    @GetMapping({"/read", "/result"})
    public void read(@RequestParam("bno") Integer bno, Model model) {
        System.out.println("Book read: " + bno);
        model.addAttribute("book", bookService.read(bno));
    }

    @PostMapping("/borrow")
    public String borrow(BookDTO book, RedirectAttributes rattr) {
        log.info("Book borrow: {} {}", book.getBorrowerId(), book.getBno());
        boolean isBorrowed = bookService.borrow(book);
        rattr.addFlashAttribute("result", isBorrowed ? "success" : "fail");
        rattr.addAttribute("bno", book.getBno());
        return "redirect:/book/result";
    }
}
