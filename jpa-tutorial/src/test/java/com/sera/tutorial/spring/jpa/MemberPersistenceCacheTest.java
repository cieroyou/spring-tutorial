package com.sera.tutorial.spring.jpa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.test.context.ActiveProfiles;

import com.sera.tutorial.spring.jpa.entity.Member;
import com.sera.tutorial.spring.jpa.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberPersistenceCacheTest {

	@Autowired
	private MemberRepository memberRepository;
	private static final Member SERA = new Member("세라", 1);

	@PersistenceContext
	private EntityManager entityManager;

	private JpaEntityInformation<Member, ?> entityInformation;

	@BeforeEach
	void setUp() {
		entityInformation = JpaEntityInformationSupport
			.getEntityInformation(Member.class, entityManager);
	}

	/**
	 * Entity 는 동시성 보장이 된다 테스트
	 * 비영속 상태는 member.getId() 가 null 이다.
	 * member.id == SERA.id 이면 둘은 같다고 본다
	 */
	@Test
	public void saveMethodTest() {
		System.out.println(String.format("[Save] SERA 는 %s 이다", getPersistenceStatus(SERA)));
		System.out.println(String.format("[Save] SERA 의 id = %d", SERA.getId()));

		Member member = memberRepository.save(SERA);
		System.out.println(String.format("[Save] SERA 는 %s 이다", getPersistenceStatus(SERA)));
		System.out.println(String.format("[Save] SERA 의 id = %d", SERA.getId()));
		System.out.println(String.format("[Save] member 의 id = %d", member.getId()));

		assertEquals(SERA, member);
	}

	private String getPersistenceStatus(Member member) {
		return entityInformation.isNew(member) ? "비영속상태" : "영속상태";
	}

	@Test
	public void findTest() {
		System.out.println(String.format("[Find] SERA 는 %s 이다", getPersistenceStatus(SERA)));
		System.out.println(String.format("[Find] SERA 의 id = %d", SERA.getId()));
		Member member = memberRepository.save(SERA);
		System.out.println(String.format("[Find] member 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Find] member 의 id = %d", member.getId()));
		Member findMember = memberRepository.findById(member.getId()).get();
		System.out.println(String.format("[Find] findMember 는 %s 이다", getPersistenceStatus(findMember)));
		System.out.println(String.format("[Find] findMember 의 id = %d", findMember.getId()));
		assertEquals(member, findMember);
	}

	/**
	 * 비영속 상태에서 영속상태가 되려면 디비에서 무조건 한번 access 를 해야 한다.
	 * save(SERA) 를 하면 insert 를 하면서 저장된 id 값을 영속성컨텍스트의 캐시의 key 로 넣기 떄문에, findById 에서 쿼리를 하지 않는다.
	 * findByName('세라') 로 찾는 Member 는 캐시에 id 로 찾는게 아니므로, db select where name = '세라' 로 쿼리를 한다.
	 * findByName 은 무조건 where id 가 아니므로, db 로 select 쿼리를 무조건 한다.
	 * findByName 은 캐시의 이점을 전혀 가지지 못한다.
	 */
	@Test
	public void findByNameTest() {
		System.out.println(String.format("[Find] SERA , hashCode = %s", SERA.hashCode()));

		Member member = memberRepository.save(SERA);
		System.out.println(String.format("[Find] SERA 는 %s 이다", getPersistenceStatus(SERA)));
		System.out.println(String.format("[Find] SERA 의 id = %d, hashCode = %s", SERA.getId(), SERA.hashCode()));
		System.out.println(String.format("[Find] member 의 id = %d, hashCode = %s", member.getId(), member.hashCode()));

		Member findById = memberRepository.findById(member.getId()).get();
		Member findBySera = memberRepository.findByName("세라").get();
		Member findBySera2 = memberRepository.findByName("세라").get();
		System.out.println(String.format("[Find] findBySera2 는 %s 이다", getPersistenceStatus(findBySera2)));
		System.out.println(String.format("[Find] findBySera2 의 id = %d", findBySera2.getId()));
		assertEquals(findById, findBySera2);
	}

	/**
	 * 멤버를 생성하고, 멤버를 삭제하고, 멤버를 다시 조회하는 Test
	 * 멤버르 조회할 때는 실제 쿼리가 나가지 않는다. 그 이유는 삭제를 하더라 아직 영속성 컨텍스트 내부에 있기 때문에 실제 쿼리는 나가지 않는다.
	 * member는 영속상태이고, id도 가지고 있다.
	 * 영속상태이고 id도 가지고 있지만, findById 를 할 때 NoSuchElement 익셉션이 발생하며 해당 객체는 없다고 한다.
	 */
	@Test
	@Transactional
	public void deleteTest() {
		Member member = memberRepository.save(SERA);

		System.out.println(String.format("[Find] member 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Find] member 의 id = %d", member.getId()));
		memberRepository.deleteById(member.getId());

		//                entityManager.flush();
		//                entityManager.clear();
		System.out.println(String.format("[Find] member 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Find] member 의 id = %d", member.getId()));
		memberRepository.findById(member.getId()).get();
		//        assertTrue(memberRepository.findById(member.getId()).isEmpty());

	}

	/**
	 * 1L을 가진 멤버를 조회해서 영속화한 내역이 없으므로, 삭제를 하기전에 SELECT 쿼리를 날린 후 값이 있으면 1차 캐시에 적재를 한다.
	 * deleteById 구현부를 보면 find를 한 후 없으면 EmptyResultDataAccessException 를 던지도록 구현되어 있는 것을 확인할 수 있다.
	 * deleteById 는 find 후 객체를 1차캐시에 적재 후, 쓰기지연저장소에 delete 쿠리를 적재하고 flush 될때 delete쿼리가 실행된다.
	 */
	@Test
	public void delete2Test() {
		assertThrows(EmptyResultDataAccessException.class, () -> memberRepository.deleteById(1L));
	}

	@Test
	public void findByIdUsingJpql() {
		Member member = memberRepository.save(SERA);
		System.out.println(String.format("[Find] SERA 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Find] SERA 의 id = %d, hashCode = %s", member.getId(), member.hashCode()));
		Member memberUsingJpql = memberRepository.findByIdJpql(member.getId());
		System.out.println(String.format("[Find] SERA 는 %s 이다", getPersistenceStatus(memberUsingJpql)));
		System.out.println(
			String.format("[Find] SERA 의 id = %d, hashCode = %s", memberUsingJpql.getId(), memberUsingJpql.hashCode()));
		assertEquals(member, memberUsingJpql);
	}

	@Test
	public void findByIdUsingJpqlAndFlush() {
		Member member = memberRepository.save(SERA);
		System.out.println(String.format("[Find] SERA 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Find] SERA 의 id = %d, hashCode = %s", member.getId(), member.hashCode()));
		entityManager.flush();
		entityManager.clear();
		System.out.println(String.format("[Flush] SERA 는 %s 이다", getPersistenceStatus(member)));
		System.out.println(String.format("[Flush] SERA 의 id = %d, hashCode = %s", member.getId(), member.hashCode()));
		Member memberUsingJpql = memberRepository.findByIdJpql(member.getId());
		System.out.println(String.format("[Find] memberUsingJpql 는 %s 이다", getPersistenceStatus(memberUsingJpql)));
		System.out.println(String.format("[Find] memberUsingJpql 의 id = %d, hashCode = %s", memberUsingJpql.getId(),
			memberUsingJpql.hashCode()));
		assertNotEquals(member, memberUsingJpql);
	}

	@Test
	public void updateByJpql() {
		Member sera1 = memberRepository.save(new Member("sera1", 1));
		memberRepository.increaseAge(sera1.getId());
		assertNotEquals(2, sera1.getAge());
	}

	@Test
	public void updateByJpqlThenFlush() {
		Member sera1 = memberRepository.save(new Member("sera1", 1));
		System.out.println(String.format("[Find] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[Find] sera1 의 id = %d", sera1.getId()));
		memberRepository.increaseAge(sera1.getId());
		System.out.println(String.format("[increaseAge] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[increaseAge] sera1 의 id = %d", sera1.getId()));
		entityManager.flush();
		//        entityManager.clear();
		System.out.println(String.format("[flush] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[flush] sera1 의 id = %d", sera1.getId()));
		Member findSera1 = memberRepository.findById(sera1.getId()).get();
		System.out.println(String.format("[Find] findSera1 는 %s 이다", getPersistenceStatus(findSera1)));
		System.out.println(String.format("[Find] findSera1 의 id = %d", findSera1.getId()));
		assertNotEquals(2, sera1.getAge());
		//        assertEquals(2, findSera1.getAge());

		// 둘다 영속상태이고, id가 같다고 해서 같은 객체는 아니다
		assertEquals(sera1, findSera1);
	}

	@Test
	public void updateByJpqlThenFlush2() {
		Member sera1 = memberRepository.save(new Member("sera1", 1));
		System.out.println(String.format("[Find] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[Find] sera1 의 id = %d", sera1.getId()));
		memberRepository.increaseAge(sera1.getId());
		System.out.println(String.format("[increaseAge] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[increaseAge] sera1 의 id = %d", sera1.getId()));
		entityManager.flush();
		//        entityManager.clear();
		System.out.println(String.format("[flush] sera1 는 %s 이다", getPersistenceStatus(sera1)));
		System.out.println(String.format("[flush] sera1 의 id = %d", sera1.getId()));
		//        Member findSera1 = memberRepository.findById(sera1.getId()).get();
		Member findSera1 = memberRepository.findByName("sera1").get();
		System.out.println(String.format("[Find] findSera1 는 %s 이다", getPersistenceStatus(findSera1)));
		System.out.println(String.format("[Find] findSera1 의 id = %d", findSera1.getId()));
		assertNotEquals(2, sera1.getAge());
		assertEquals(1, findSera1.getAge());

		// 둘다 영속상태이고, id가 같다고 해서 같은 객체는 아니다
		assertEquals(sera1, findSera1);
	}
}