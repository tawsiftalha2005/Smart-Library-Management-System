package service;

import model.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import utils.FileManager;
import utils.CsvFormat;

public class MemberService {

    private ArrayList<Member> members = new ArrayList<>();

    // Add Member
    public boolean addMember(Member member) {
        if (member == null || searchMemberById(member.getId()) != null) {
            System.out.println("A member with this ID already exists.");
            return false;
        }
        members.add(member);
        saveToFile();
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
            saveToFile();
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
        saveToFile();
        return true;
    }

    public void loadFromFile() {
        members.clear();
        try {
            for (String line : FileManager.readLines(FileManager.MEMBER_FILE)) {
                if (line.isBlank()) continue;
                try {
                    List<String> f = CsvFormat.decode(line);
                    if (f.size() != 4) throw new IllegalArgumentException("Expected 4 fields");
                    members.add(new Member(Integer.parseInt(f.get(0)), f.get(1), f.get(2), f.get(3)));
                } catch (RuntimeException e) { System.err.println("Skipping invalid member row: " + e.getMessage()); }
            }
        } catch (IOException e) { System.err.println("Could not load members: " + e.getMessage()); }
    }

    public void saveToFile() {
        List<String> rows = new ArrayList<>();
        for (Member m : members) rows.add(CsvFormat.encode(String.valueOf(m.getId()), m.getName(), m.getEmail(), m.getPhone()));
        try { FileManager.writeLines(FileManager.MEMBER_FILE, rows); }
        catch (IOException e) { System.err.println("Could not save members: " + e.getMessage()); }
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // Total Members
    public int totalMembers() {
        return members.size();
    }
}
