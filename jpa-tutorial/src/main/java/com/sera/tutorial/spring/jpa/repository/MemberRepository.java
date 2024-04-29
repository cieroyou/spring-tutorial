package com.sera.tutorial.spring.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sera.tutorial.spring.jpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    @Query(value = "SELECT m FROM Member m WHERE m.id = ?1")
    Member findByIdJpql(Long id);

    @Modifying()
    @Query("update Member m set m.age = m.age +1")
    void increaseAgeAll();

    @Modifying
    @Query("update Member m set m.age = m.age +1 WHERE m.id = ?1")
    void increaseAge(Long id);
}
