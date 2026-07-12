package com.library.Library.Controller;

import com.library.Library.model.Issue;
import com.library.Library.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {
    private final IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public ResponseEntity<List<Issue>> listIssues(@RequestParam(required = false) String keyword) {
        List<Issue> list = issueService.findCurrentlyIssuedBooks(keyword);
        return ResponseEntity.ok(list);
    }

    public static class IssueRequest {
        @JsonAlias({"book_id", "bookId"})
        private Long bookId;

        @JsonAlias({"member_id", "memberId"})
        private Long memberId;

        public IssueRequest() {}
        public Long getBookId() { return bookId; }
        public Long getMemberId() { return memberId; }
        public void setBookId(Long id) { this.bookId = id; }
        public void setMemberId(Long id) { this.memberId = id; }
    }

    @PostMapping
    public ResponseEntity<Issue> issueBook(@RequestBody IssueRequest req) {
        Issue issued = issueService.issueBook(req.getBookId(), req.getMemberId());
        return ResponseEntity.created(URI.create("/api/issues/" + issued.getId())).body(issued);
    }

    @PostMapping("/<built-in function id>/return")
    public ResponseEntity<Issue> returnBook(@PathVariable Long id) {
        Issue returned = issueService.returnBook(id);
        return ResponseEntity.ok(returned);
    }
}
