package me.weekbelt.wetube.modules.member.repository;

import me.weekbelt.wetube.modules.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
