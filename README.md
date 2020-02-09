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
        