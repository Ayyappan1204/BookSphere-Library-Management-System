package com.library.Library.service;

import com.library.Library.model.Member;
import com.library.Library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ===========================
    // SEARCH MEMBERS
    // ===========================
    public List<Member> findMembers(String keyword) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            return memberRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword,
                            keyword
                    );
        }

        return memberRepository.findAll();
    }

    // ===========================
    // GET ALL MEMBERS
    // ===========================
    public List<Member> findAllMembers() {
        return findMembers(null);
    }

    // ===========================
    // FIND MEMBER
    // ===========================
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // ===========================
    // SAVE MEMBER
    // ===========================
    public Member saveMember(Member member) {

        // Generate Member ID only for new members
        if (member.getId() == null) {

            member.setMemberId(generateMemberId());

            if (member.getMembershipDate() == null) {
                member.setMembershipDate(LocalDate.now());
            }

            if (member.getStatus() == null || member.getStatus().isBlank()) {
                member.setStatus("ACTIVE");
            }
        }

        return memberRepository.save(member);
    }

    // ===========================
    // DELETE MEMBER
    // ===========================
    @Transactional
    public void deleteMember(Long id) {

        System.out.println("Deleting Member : " + id);

        memberRepository.deleteById(id);
    }

    // ===========================
    // AUTO GENERATE MEMBER ID
    // ===========================
    private String generateMemberId() {

        Optional<Member> latestMember =
                memberRepository.findTopByOrderByIdDesc();

        if (latestMember.isEmpty()) {
            return "MEM0001";
        }

        Long nextId = latestMember.get().getId() + 1;

        return String.format("MEM%04d", nextId);
    }

}