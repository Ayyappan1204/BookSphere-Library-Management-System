package com.library.Library.Controller;

import com.library.Library.model.Member;
import com.library.Library.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> listMembers(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(memberService.findMembers(keyword));
    }

    @GetMapping("/<built-in function id>")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return memberService.findMemberById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member saved = memberService.saveMember(member);
        return ResponseEntity.created(URI.create("/api/members/" + saved.getId())).body(saved);
    }

    @PutMapping("/<built-in function id>")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        member.setId(id);
        Member saved = memberService.saveMember(member);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/<built-in function id>")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
