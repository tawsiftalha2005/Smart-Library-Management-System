package service;

import model.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberService {

    private ArrayList<Member> members = new ArrayList<>();

    // Add Member
    public boolean addMember(Member member) {
        if (member == null || searchMemberById(member.getId()) != null) {
            System.out.println("A member with this ID already exists.");
            return false;
        }
        members.add(member);
        System.out.println("Member added successfully!");
        return true;
    }

    // View Members
    public void viewMembers() {

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-30s %-15s%n",
                "ID", "Name", "Email", "Phone");
        System.out.println("--------------------------------------------------------------------------");

        for (Member member : members) {
            System.out.println(member);
        }

        System.out.println("--------------------------------------------------------------------------");
    }

    // Search Member
    public Member searchMemberById(int id) {

        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }

        return null;
    }

    // Delete Member
    public boolean deleteMember(int id) {

        Member member = searchMemberById(id);

        if (member != null) {
            members.remove(member);
            return true;
        }

        return false;
    }

    public boolean updateMember(int id, String name, String email, String phone) {
        Member member = searchMemberById(id);
        if (member == null) return false;
        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);
        return true;
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // Total Members
    public int totalMembers() {
        return members.size();
    }
}
