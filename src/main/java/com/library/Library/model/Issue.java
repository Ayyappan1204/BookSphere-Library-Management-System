package com.library.Library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate issueDate;

    // NEW FIELD
    private LocalDate dueDate;

    private LocalDate returnDate;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"issues"})
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties({"issues"})
    private Member member;

    public Issue() {
    }

    public Issue(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.issueDate = LocalDate.now();

        // Automatically set due date to 14 days
        this.dueDate = this.issueDate.plusDays(14);

        this.status = "ISSUED";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    // NEW GETTER
    public LocalDate getDueDate() {
        return dueDate;
    }

    // NEW SETTER
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {

        // Automatically mark overdue
        if ("ISSUED".equals(status)
                && dueDate != null
                && LocalDate.now().isAfter(dueDate)) {

            return "OVERDUE";
        }

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}