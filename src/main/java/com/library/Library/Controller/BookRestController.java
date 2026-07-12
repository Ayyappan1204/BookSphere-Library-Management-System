package com.library.Library.Controller;

import com.library.Library.model.Book;
import com.library.Library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> listBooks(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(bookService.findBooks(keyword));
    }

    @GetMapping("/<built-in function id>")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookService.findBookById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book saved = bookService.saveBook(book);
        return ResponseEntity.created(URI.create("/api/books/" + saved.getId())).body(saved);
    }

    @PutMapping("/<built-in function id>")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        Book saved = bookService.saveBook(book);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/<built-in function id>")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/<built-in function id>/checkin")
    public ResponseEntity<Book> checkIn(@PathVariable Long id) {
        Book updated = bookService.checkInBook(id);
        return ResponseEntity.ok(updated);
    }
}
