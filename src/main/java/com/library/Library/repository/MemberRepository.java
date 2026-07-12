package com.library.Library.repository;

import com.library.Library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Search by Name or Email
     */
    List<Member> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nameTerm,
            String emailTerm
    );

    /**
     * Find latest member
     * Used for auto-generating Member ID
     */
    Optional<Member> findTopByOrderByIdDesc();

    /**
     * Find member using Member ID
     * Example: MEM0001
     */
    Optional<Member> findByMemberId(String memberId);

    /**
     * Check duplicate Member ID
     */
    boolean existsByMemberId(String memberId);

    /**
     * Search by Department
     */
    List<Member> findByDepartmentContainingIgnoreCase(String department);

    /**
     * Search by Status
     */
    List<Member> findByStatus(String status);

    /**
     * Search by Phone
     */
    Optional<Member> findByPhone(String phone);

    /**
     * Search by Email
     */
    Optional<Member> findByEmail(String email);
}