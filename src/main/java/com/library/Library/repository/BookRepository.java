package com.library.Library.repository;

import com.library.Library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author
    );

    Book findTopByOrderByIdDesc();

    @Query("SELECT COALESCE(SUM(b.quantity),0) FROM Book b")
    Long sumAvailableBooks();

}