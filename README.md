### 인프런 실전! 스프링 데이터 JPA 강의 정리

* 예제 도메인 모델과 동작 확인
    * 엔티티 모델 정의
    * JPA Entity 모델링 중 MappedBy는 FK가 없는 쪽에 설정하는것이 좋다.
    *  @NoArgsConstructor AccessLevel.PROTECTED: 기본 생성자 막고 싶은데, JPA 스팩상
       PROTECTED로 열어두어야 함
    * Member 와 Team은 양방향 연관관계, Member.team이 연관관계의 주인이다. 따라서 Team.member는 연관관계의 주인이 아니다. <br>
       그러므로 Member.team이 데이터베이스 외래키 값을 변경가능하고 반대편은 읽기만 가능하다.
* 공통 인터페이스 설정
    * JavaConfig 설정 : 스프링 부트 사용시 생략 가능
    * 스프링 부트 사용시 @SpringBootApplication 위치를 지정
    * 스프링 데이터 JPA가 구현 클래스 대신 생성
        * org.springframework.data.repository.Repository 를 구현한 클래스는 스캔 대상
        * @Repository 애노테이션 생략 가능
            * 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리
            * JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리
* 공통 인터페이스 분석
    * T findOne(ID) -> Optional<T> findById(Id) 변경
    * 제네릭 타입
        * T : 엔티티
        * ID : 엔티티의 식별자 타입
        * S : 엔티티와 그 자식 타입
    * 주요 메서드
        * save(S) : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.
        * delete(T) : 엔티티 하나를 삭제한다. 내부에서 EntityManager.remove() 호출
        * findById(ID) : 엔티티 하나를 조회한다. 내부에서 EntityManager,find() 호출
        * getOne(ID) : 엔티티를 프록시로 조회한다. 내부에서 EntityManager.getReference() 호출
        * findAll() : 모든 엔티티를 조회한다. 정렬(Sort)나 페이징(Pagable) 조건을 파라미터로 제공할 수 있다.
* 쿼리 메소드 기능
    * 쿼리 메소드 기능 3가지
        * 메소드 이름으로 쿼리 생성
        * 메소드 이름으로 JPA NamedQuery 호출
        * @Query 어노테이션을 사용해서 리포지토리 인터페이스에 쿼리 직접 정의 
    * 메소드 이름으로 쿼리 생성
        * 메소드 이름을 분석해서 JPQL 쿼리 실행
            * 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
        * 쿼리 메소드 필터 조건 
            * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
        * 스프링 데이터 JPA가 제공하는 쿼리 메소드 기능
            * 조회 : find...By, read...by, query...by,get...By
            * COUNT : count...By 반환타입 long
            * EXIST : exist...By 반환타입 boolean
            * 삭제 : delete...By, remove...By 반환타입 long
            * DISTINCT : findDistinct, findMemberDistinctBy
            * LIMIT : findFirst3, findFirst, findTop, findTop3
        * 참고 : 이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 변경해야 한다. <br>
        그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생. <br>
        이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.
    * JPA NamedQuery
        * 스프링 데이터 JPA는 선언한 도메인 클래스 + . + 메서드 이름 으로 Named 쿼리를 찾아서 실행.
        * 만약 실행할 Named 쿼리가 없으면 메서드 이름으로 쿼리 생성 전략을 사용한다.
        * 필요하면 전략을 변경할 수 있지만 권장하지 않는다.
        * 참고 : 스프링 데이터 JPA를 사용하면 실무에서 Naemd Query를 직접 등록해서 사용하는 일은 드물다. 대신 @Query를 사용해서 리포지토리 메소드에 쿼리를 직접 정의.
    * @Query, 리포지토리 메소드에 쿼리 정의하기
        * `@org.springframework.data.jpa.repository.Query` 어노테이션을 사용
        * 실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음
        * JPA Named 쿼리 처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있음.
        > 참고 : 실무에서는 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 매우 지저분해진다. 따라서 `@Query` 기능을 자주 사용하게 된다.
    * DTO로 직접 조회
        > 주의! : DTO로 직접 조회 하려면 JPA의 `new` 명령어를 사용해야 한다. 그리고 다음과 괕이 생성자가 맞는 DTO가 필요하다. (JPA와 동일)        
    * 파라미터 바인딩
        * 위치 기반 (사용하지 말것)
        * 이름 기반            
    * 반환 타입
        * 스프링 데이터 JPA는 유연한 반환 타입 지원
        * 조회 결과가 많거나 없으면 ?
            * 컬렉션 
                * 결과 없음 : 빈 컬렉션 반환
            * 단건 조회
                * 결과 없음 : null 반환    
                * 결과가 2건 이상 :  `javax.persistence.NonUniqueResultException` 발생
                > 참고 : 단건으로 지정한 메서드를 호출하면 스프링 데이터 JPA는 내부에서 JPQL의 Query.getSingleResult() 메서드를 호출한다.<br>
                이 메서드를 호출 했을 때 조회 결과가 없으면 `javax.persistence.NoResultException` 예외가 발생하는데 개발자 입장에서 <br>
                다루기 상당히 불편. 스프링 데이터 JPA는 단건 조회시 해당 예외가 발생하면 예외를 무시하고 null을 반환
    * 순수 JPA 페이징과 정렬
    * 스프링 데이터 JPA 페이징과 정렬
        * *페이징과 정렬 파라미터*
            * org.springframework.data.domain.Sort : 정렬 기능
            * org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
        * *특별한 반환 타입*
            * org.springframework.data.domain.Page : 추가 count 쿼리 결과를 포함하는 페이징
            * org.springframework.data.domain.Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1조회)
            * List (자바 컬렉션): 추가 count 쿼리 없이 결과만 반환
        * Pageable 생성
            ```java
              PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
            ```
          * PageRequest 생성자의 첫번째 파라미터는 현재 페이지, 두번째 파라미터는 조회할 데이터수, 정렬 정보 입력가능, 페이지는 0부터 시작
        * Count Query 분리
            * 복잡한 Join Query가 실행될 때 불필요한 Join Query를 Count Query에서 실행하지 않도록 최적화
            ```java
              @Query(value = “select m from Member m”,
               countQuery = “select count(m.username) from Member m”)
              Page<Member> findMemberAllCountBy(Pageable pageable);
            ```
        * Top, First 등의 쿼리도 지원한다.
            * List<Member> findTop3ByXXX();
        * 페이지를 유지하면서 엔티티를 DTO로 변환하기
            ```java
              Page<Member> page = memberRepository.findByAge(10, pageRequest);
              Page<MemberDto> dtoPage = page.map(m -> new MemberDto());
            ```   
        * 정리
            * Page
            * Slice (Count X) 추가로 limit + 1 을 조회한다. 그래서 다음 페이지 여부를 확인(최근 모바일 리스트 생각해보면 됨)
            * List (Count X)
            * 카운트 쿼리 분리(복잡한 SQL에서 사용, 데이터는 Left Join, 카운트는 Left Join 안해도됨)
                * 실무에서 매우 중요함.
            > 참고 : 전체 count 쿼리는 매우 무겁다.
    * 벌크성 수정 쿼리
        * 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용
            * 사용하지 않으면 `org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations` 에러 발생
        * 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화: `@Modifying(clearAutomatically = true)` default false
            * 이 옵션 없이 findById와 같은 조회를 하면 영속성 컨텍스트에 과거 값이 남아서 뭊제가 될 수 있다.
        > 참고 : 벌크 연산은 영속성 컨텍스트를 무시하고 실행, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다. <br> 
          권장 방안 <br>
          1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
          2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다. 
        
    * EntityGraph
        * 연관된 엔티티들을 SQL 한번에 조회하는 방법
            * member -> team은 지연로딩 관계이다. 따라서 다음과 같이 team의 데이터를 조회할때 마다 쿼리가 실행된다. (N+1문제 발생)
            * 연관된 엔티티를 한번에 조회하려면 패치 조인이 필요하다.
        * JPQL 패치 조인
            * 스프링 데이터 JPA는 JPA가 제공하는 엔티티 그래프 기능을 편리하게 사용할수있도록 도와준다. 이 기능을 사용하면 JPQL없이 패치 조인을 할 수 있다. (JPQL + 엔티티 그래프도 가능)
        * EntityGraph 정리
            * 사실상 패치 조인(Fetch Join)의 간편 버전
            * Left Outer Join 사용
        * Named Query 사용
    * JPA Hint & Lock
        * JPA Hint
            * JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체에게 제공되는 힌트)
            * @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
            * 페이징 예제  
            @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true", forCounting=true))
            * forCounting : 반환 타입으로 Page 인터페이스를 적용하면 추가로 호출하는 페이징을 위한 count 쿼리도 쿼리 힌트 적용(기본값 true )
            * Lock
                * org.springframework.data.jpa.repository.Lock 어노테이션을 사용
                * JPA가 제공하는 락은 JPA 책 16.1 트랜잭션과 락 절을 참고
* 확장기능
    * 사용자 정의 리포지토리 구현
        * 스프링 데이터 JPA 리포지토리는 인터페이스만 정의하고 구현체는 스프링이 자동으로 생성
        * 스프링 데이터 JPA가 제공하는 인터페이스를 직접 구현하면 구현해야 하는 기능이 너무 많다.
        * 다양한 이유로 인터페이스의 메서드를 직접 구현하고 싶다면 ?
            * JPA 직접 사용(`EntityManager`)
            * 스프링 JDBC Template 사용
            * MyBatis 사용
            * 데이터베이스 커넥션 직접 사용 등등...
            * QueryDsl 사용
        * 사용자 정의 구현 클래스
            * 규칙 : 리포지토리 인터페이스 이름 + Impl
            * 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록.
        > 참고 : 실무에서는 주로 QueryDSL이나 SpringJdbcTemplate을 함께 사용할 때 사용자 정의 리포지토리 기능 자주 사용  

        > 참고 : 항상 사용자 정의 리포지토리가 필요한 것은 아니다. 그냥 임의의 리포지토리를 만들어도 된다.  
            예를 들면 MemberQueryRepository를 인터페이스가 아닌 클래스로 만들고 스프링 빈으로 등록해서 그냥 직접 사용해도 된다.  
            물론 이 경우 스프링 데이터 JPA와는 아무런 관계 없이 별도로 동작한다.