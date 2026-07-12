package com.library.Library.service;

import com.library.Library.model.Book;
import com.library.Library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ===========================
    // Search Books
    // ===========================
    public List<Book> findBooks(String keyword) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                            keyword,
                            keyword
                    );
        }

        return bookRepository.findAll();
    }

    // ===========================
    // Get All Books
    // ===========================
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // ===========================
    // Find Book By ID
    // ===========================
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    // ===========================
    // Save Book
    // ===========================
    public Book saveBook(Book book) {

        // Generate Book ID for new books only
        if (book.getId() == null) {

            if (book.getBookId() == null || book.getBookId().isBlank()) {
                book.setBookId(generateBookId());
            }

            book.setAvailable(book.getQuantity() > 0);
        } else {

            // Keep availability updated during edit
            book.setAvailable(book.getQuantity() > 0);
        }

        return bookRepository.save(book);
    }

    // ===========================
    // Delete Book
    // ===========================
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // ===========================
    // Check In Book
    // ===========================
    public Book checkInBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id : " + id));

        book.setQuantity(book.getQuantity() + 1);

        book.setAvailable(true);

        return bookRepository.save(book);
    }

    // ===========================
    // Check Out Book
    // ===========================
    public Book checkOutBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id : " + id));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book is out of stock.");
        }

        book.setQuantity(book.getQuantity() - 1);

        if (book.getQuantity() == 0) {
            book.setAvailable(false);
        }

        return bookRepository.save(book);
    }

    // ===========================
    // Generate Book ID
    // ===========================
    private String generateBookId() {

        Book latestBook = bookRepository.findTopByOrderByIdDesc();

        if (latestBook == null) {
            return "BK0001";
        }

        Long nextId = latestBook.getId() + 1;

        return String.format("BK%04d", nextId);
    }

}