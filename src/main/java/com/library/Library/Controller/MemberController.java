package com.library.Library.Controller;

import com.library.Library.model.Member;
import com.library.Library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // ===========================
    // MEMBER LIST + SEARCH
    // ===========================
    @GetMapping
    public String listMembers(@RequestParam(required = false) String keyword,
                              Model model) {

        model.addAttribute("members", memberService.findMembers(keyword));
        model.addAttribute("keyword", keyword);

        return "members/list";
    }

    // ===========================
    // SHOW ADD MEMBER PAGE
    // ===========================
    @GetMapping("/add")
    public String showAddMemberForm(Model model) {

        Member member = new Member();

        // Display only (actual ID is generated in MemberService)
        member.setMemberId("Auto Generated");

        model.addAttribute("member", member);

        return "members/add";
    }

    // ===========================
    // SAVE MEMBER
    // ===========================
    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member) {

        memberService.saveMember(member);

        return "redirect:/members";
    }

    // ===========================
    // SHOW UPDATE PAGE
    // ===========================
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id,
                                 Model model) {

        Member member = memberService.findMemberById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid Member ID : " + id));

        model.addAttribute("member", member);

        return "members/edit";
    }

    // ===========================
    // UPDATE MEMBER
    // ===========================
    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable Long id,
                               @ModelAttribute Member member) {

        Member existingMember = memberService.findMemberById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid Member ID : " + id));

        member.setId(id);

        // Preserve generated Member ID
        member.setMemberId(existingMember.getMemberId());

        // Preserve Membership Date
        member.setMembershipDate(existingMember.getMembershipDate());

        memberService.saveMember(member);

        return "redirect:/members";
    }

    // ===========================
    // DELETE MEMBER
    // ===========================
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {

        memberService.deleteMember(id);

        return "redirect:/members";
    }

}