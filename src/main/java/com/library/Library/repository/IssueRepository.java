package com.library.Library.repository;

import com.library.Library.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByBook_TitleContainingIgnoreCaseOrMember_NameContainingIgnoreCase(
            String title,
            String member
    );

    List<Issue> findByStatus(String status);

    long countByStatus(String status);

}