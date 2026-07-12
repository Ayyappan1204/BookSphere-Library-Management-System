package com.library.Library.service;

import com.library.Library.model.Book;
import com.library.Library.model.Issue;
import com.library.Library.model.Member;
import com.library.Library.repository.BookRepository;
import com.library.Library.repository.IssueRepository;
import com.library.Library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository,
                        BookRepository bookRepository,
                        MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // ===========================
    // ISSUE BOOK
    // ===========================
    @Transactional
    public Issue issueBook(Long bookId, Long memberId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id : " + bookId));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book is out of stock.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found with id : " + memberId));

        // Reduce quantity
        book.setQuantity(book.getQuantity() - 1);

        if (book.getQuantity() == 0) {
            book.setAvailable(false);
        }

        bookRepository.save(book);

        Issue issue = new Issue();

        issue.setBook(book);
        issue.setMember(member);

        issue.setIssueDate(LocalDate.now());

        // Due after 14 days
        issue.setDueDate(LocalDate.now().plusDays(14));

        issue.setStatus("ISSUED");

        return issueRepository.save(issue);
    }

    // ===========================
    // RETURN BOOK
    // ===========================
    @Transactional
    public Issue returnBook(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new RuntimeException("Issue not found."));

        if ("RETURNED".equals(issue.getStatus())) {
            throw new RuntimeException("Book already returned.");
        }

        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");

        Issue returnedIssue = issueRepository.save(issue);

        Book book = returnedIssue.getBook();

        book.setQuantity(book.getQuantity() + 1);
        book.setAvailable(true);

        bookRepository.save(book);

        return returnedIssue;
    }

    // ===========================
    // CURRENT ISSUES
    // ===========================
    public List<Issue> findCurrentlyIssuedBooks(String keyword) {

        List<Issue> issues = issueRepository.findByStatus("ISSUED");

        // Automatically update overdue status
        issues.forEach(issue -> {

            if (issue.getDueDate() != null
                    && LocalDate.now().isAfter(issue.getDueDate())
                    && !"RETURNED".equals(issue.getStatus())) {

                issue.setStatus("OVERDUE");
                issueRepository.save(issue);
            }
        });

        if (keyword != null && !keyword.trim().isEmpty()) {

            String key = keyword.toLowerCase();

            return issues.stream()

                    .filter(issue ->

                            issue.getBook().getTitle().toLowerCase().contains(key)

                                    ||

                                    issue.getMember().getName().toLowerCase().contains(key)

                    )

                    .collect(Collectors.toList());
        }

        return issues;
    }

}