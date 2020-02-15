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