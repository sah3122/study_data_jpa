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