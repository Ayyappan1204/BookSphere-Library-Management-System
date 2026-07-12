package com.library.Library.Controller;

import com.library.Library.model.Book;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.IssueRepository;
import com.library.Library.repository.MemberRepository;
import com.library.Library.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    @Autowired
    public HomeController(BookService bookService,
                          BookRepository bookRepository,
                          MemberRepository memberRepository,
                          IssueRepository issueRepository) {

        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.issueRepository = issueRepository;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {

        model.addAttribute("totalBooks", bookRepository.count());

        model.addAttribute("totalMembers", memberRepository.count());

        model.addAttribute("issuedBooks",
                issueRepository.countByStatus("ISSUED"));

        model.addAttribute("availableBooks",
                bookRepository.sumAvailableBooks());

        return "index";
    }

    @GetMapping("/books")
    public String listBooks(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute("books",
                bookService.findBooks(keyword));

        model.addAttribute("keyword", keyword);

        return "books";
    }

    @GetMapping("/addbook")
    public String showAddBookForm(Model model) {

        model.addAttribute("book", new Book());

        return "add-book";
    }

    @PostMapping("/addbook")
    public String addBook(@ModelAttribute Book book) {

        bookService.saveBook(book);

        return "redirect:/books";
    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id,
                                 Model model) {

        Book book = bookService.findBookById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid Book Id : " + id));

        model.addAttribute("book", book);

        return "update-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable("id") long id,
                             @ModelAttribute Book book) {

        book.setId(id);

        bookService.saveBook(book);

        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {

        bookService.deleteBook(id);

        return "redirect:/books";
    }

}