### 인프런 실전! 스프링 데이터 JPA 강의 정리

* 예제 도메인 모델과 동작 확인
    * 엔티티 모델 정의
    * JPA Entity 모델링 중 MappedBy는 FK가 없는 쪽에 설정하는것이 좋다.
    *  @NoArgsConstructor AccessLevel.PROTECTED: 기본 생성자 막고 싶은데, JPA 스팩상
       PROTECTED로 열어두어야 함
    * Member 와 Team은 양방향 연관관계, Member.team이 연관관계의 주인이다. 따라서 Team.member는 연관관계의 주인이 아니다. <br>
       그러므로 Member.team이 데이터베이스 외래키 값을 변경가능하고 반대편은 읽기만 가능하다.