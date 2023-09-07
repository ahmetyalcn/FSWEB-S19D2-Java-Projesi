package com.workintech.auth.service;

import com.workintech.auth.dao.AccountRepository;
import com.workintech.auth.dao.MemberRepository;
import com.workintech.auth.dao.RoleRepository;
import com.workintech.auth.entity.Member;
import com.workintech.auth.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;


    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, MemberRepository memberRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
    }

    public Member register(String email, String password){
        Optional<Member> foundMember = memberRepository.findMemberByEmail(email);
        if(foundMember.isPresent()){
            //throw Exception
            return null;
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role memberRole = roleRepository.findByAuthority("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(memberRole);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setAuthorities(roles);
        return memberRepository.save(member);
    }
}
