### 데이터베이스 방언
- JPA 는 특정 데이터베이스에 종속 X
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다름
    + 가변 문자 : MySQL 은 VARCHAR, Oracle 은 VARCHAR2
    + 문자열을 자르는 함수 : SQL 표준은 SUBSTRING(), Oracle 은 SUBSTR()
    + 페이징 : MySQL 은 LIMIT,  Oracle ROWNUM
- 방언 : SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능 / 여기서는 h2.Dialect 

### 주의
- 엔티티 매니저 팩토리를 하나만 생성해서 애플리케이션 전체에서 공유한다.
- 그래서 엔티티 매니저는 쓰레드간에 공유 X (사용하고 버려야 한다.)
- **JPA 의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.**

### JPQL 소개
- 가장 단순한 조회 방법
  + EntityManager.find()
  + 객체 그래프 탐색(a.getB().getC())
- 나이가 18살 이상인 회원을 모두 검색하고 싶다면? -> JPQL 을 사용해야 한다.
<br>

- JPA 를 사용하면 엔티티 객체를 중심으로 개발
- 문제는 검색 쿼리
- 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
- 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 DB 에서 불러오려면 결국 검색 조건이 포함된 SQL 이 필요
<br>

- JPA 는 SQL 을 추상화한 JPQL 이라는 객체 지향 쿼리 언어 제공
- SQL 과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
- JPQL 은 엔티티 객체를 대상으로 쿼리 (Dialect 를 바꿔도 코드를 변경할 필요가 없음)
- SQL 은 데이터베이스 테이블을 대상으로 쿼리
<br>

- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- SQL 을 추상화해서 특정 데이터베이스 SQL 에 의존 X
- JPQL 을 한마디로 정희하면 객체 지향 SQL

### 영속성 관리
- 영속성 컨텍스트
- JPA 에서 가장 중요한 2가지
  + 객체와 관계형 데이터베이스 매핑하기(ORM)
  + 영속성 컨텍스트
- 엔티티 매니저 팩토리와 엔티티 매니저
  + 고객의 요청이 올 때마다 엔티티 매니저를 생성하고 내부적으로 DB connection 을 사용해서 db 에 접근
- 영속성 컨텍스트
  + JPA 를 이해하는데 가장 중요한 용어
  + "엔티티를 영구 저장하는 환경" 이라는 뜻
  + EntityManager.persist(entity); => persist 는 엔티티를 영속성 컨텍스트에 저장하는 것을 의미
- 엔티티 매니저 & 영속성 컨텍스트
  + 영속성 컨텍스트는 논리적 개념, 눈에 보이지 않는다. 
  + 엔티티 매니저를 통해 영속성 컨텍스트에 접근
- 엔티티의 생명주기
  + 비영속 : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
  + 영속 : 영속성 컨텍스트에 관리되는 상태
  + 준영속 : 영속성 컨텍스트에 저장되었다가 분리된 상태
  + 삭제 : 삭제된 상태
<br>

- 객체를 생성한 상태(비영속)
```markdown
Member member = new Member();
member.setId("member1");
member.setUserName("회원1");
```
- 객체를 저장한 상태(영속)
```markdown
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
em.persist(member);

//아직 DB 에 저장된 것은 아님 
//DB 는 이후에 커밋해야 저장된다.
```
- 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
  + em.detach(member);
- 객체를 삭제한 상태(삭제) / DB 에서 지움
  + em.remove(member);  
<br>

- 영속성 컨텍스트의 이점
  + 1차 캐시 (애플리케이션과 DB 사이에 무언가 있는 것이다.)
    - 조회를 할 때, 1차 캐시를 먼저 찾는다. 
    - 1차 캐시에 없을 경우, DB 를 조회 한 뒤 -> 1차 캐시에 저장시키고 -> 이를 반환
  + 동일성(identity) 보장
    - 영속 엔티티의 동일성 보장
  + 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
    - 쓰기 지연 SQL 저장소에서 flush 되어 DB 에 INSERT SQL
    - 즉, tx.commit() => 커밋하는 순간 데이터베이스에 INSERT SQL 을 보낸다.
  + 변경 감지(Dirty Checking)
    - setId, setName 으로 변경해준 뒤 tx.commit() 만 하면 변경을 감지하여 update 쿼리가 날아간다.
    - flush() -> 엔티티와 스냅샷(최초로 영속성 컨텍스트에 들어온 상태를 저장해 놓은 것)을 비교 -> UPDATE SQL 생성하여 쓰기 지연 SQL 저장소 -> flush -> commit
  + 지연 로딩(Lazy Loading)***
    - 추후 설명 예정 / 매우 중요

#### 플러시
- 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
- 플러시 발생 시,
  + 변경 감지
  + 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
  + 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록, 수정, 삭제 쿼리)
- 영속성 컨텍스트를 플러시하는 방법
  + em.flush() : 직접 호출
  + 트랜잭션 커밋 : 플러시 자동 호출
  + JPQL 쿼리 실행 : 플러시 자동 호출
- 플러시 할 경우, 1차 캐시가 지워지는 게 아니고 쓰기 지연 SQL 저장소에 있는 것들이 데이터베이스에 반영이 되는 과정이라고 이해하자
- 플러시 모드 옵션
  + FlushModeType.AUTO : 커밋이나 쿼리를 실행할 때 플러시(기본값) => 가급적 이것을 사용
  + FlushModeType.COMMIT : 커밋할 때만 플러시
- 플러시는!
  + 영속성 컨텍스트를 비우지 않음
  + 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
  + 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화하면 됨
  
### 준영속 상태
- em.persist 혹은 1차 캐시에 올라온 상태가 되면 영속 상태가 된다. 
- 영속 -> 준영속
- 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)되는 것이 준영속 상태
- 영속성 컨텍스트가 제공하는 기능을 사용 못함(update, dirty-checking 기능 사용 못함)
<br>

- 준영속 상태로 만드는 방법
  + em.detach(entity) : 특정 엔티티만 준영속 상태로 전환 
  + em.clear() : 영속성 컨텍스트를 완전히 초기화
  + em.close() : 영속성 컨텍스트를 종료

### 정리
- JPA 에서 가장 중요한 2가지 
  + 객체와 관계형 데이터베이스 매핑하기(ORM)
  + 영속성 컨텍스트
    - "엔티티를 영구 저장하는 환경"
    - 엔티티 매니저를 통해서 영속성 컨텍스트 접근
    - 1차 캐시가 영속성 컨텍스트라고 보면 된다. 
    - 스프링에서는 또 다른 설명 추가 예정
  
### 엔티티 매핑
- 엔티티 매핑 소개
  + 객체와 테이블 매핑 : @Entity, @Table
  + 필드와 컬럼 매핑 : @Column
  + 기본 키 매핑 : @Id
  + 연관관계 매핑 : @ManyToOne, @JoinColumn
- 객체와 테이블 매핑
  + @Entity
    - @Entity 가 붙은 클래스는 JPA 가 관리하는 엔티티라고 한다. 
    - JPA 를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
    - 기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)
    - final 클래스, enum, interface, inner 클래스 사용 X
    - 저장할 필드에 final 사용 X
  + @Entity 속성 정리
    - 속성 : name
      - JPA 에서 사용할 엔티티 이름을 지정한다. 
      - 기본값 : 클래스 이름을 그대로 사용, 가급적 기본값을 사용 
  + @Table
    - 엔티티와 매핑할 테이블 지정
<br>

- 데이터베이스 스키마 자동 생성
  + DDL 을 애플리케이션 실행 시점에 자동 생성
  + 테이블 중심 -> 객체 중심
  + 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
  + 이렇게 **생성된 DDL 은 개발 장비에서만 사용**
  + 생성된 DDL 은 운영 서버에서는 사용하지 않거나, 적절히 다듬은 후 사용
  <br>
  
  + hibernate.hbm2ddl.auto 의 value
    - create : 기존 테이블 삭제 후 다시 생성(drop + create)
    - create-drop : create 과 같으나 종료 시점에 테이블 drop
    - update : 변경분만 반영(운영 DB 에는 사용하면 안 됨)
    - validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
    - none : 사용하지 않음
  + 방언에 따라 다르게 query 가 나간다. (예-varchar / varchar2)
  + **운영 장비에는 절대 create, create-drop, update 사용하면 안된다.**
  + 개발 초기 단계는 create 또는 update
  + 테스트 서버는 update 또는 validate
  + 스테이징과 운영 서버는 validate 또는 none
  <br>
  
  + DDL 생성 기능
    - @Column(unique 제약 조건) : 데이터베이스에 영향을 준다. 
    - @Column(length 제약 조건) 
    - DDL 을 자동 생성할 때만 사용되고 JPA 의 실행 로직에는 영향을 주지 않는다. 
<br> 

- 필드와 컬럼 매핑
  + @Column
    - name : 필드와 매핑할 테이블의 컬럼 이름
    - insertable, updatable : 등록, 변경 가능 여부
    - nullable(DDL) : null 값의 허용 여부를 설정한다. false 로 설정하면 DDL 생성 시 not null 제약조건이 붙는다.
    - unique(DDL) : @Table 의 uniqueConstraints 와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용한다. 
    - columnDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 줄 수 있다. ex) varchar(100) default 'EMPTY'
    - length(DDL) : 문자 길이 제약 조건, String 타입에만 사용한다. 
    - precision, scale(DDL) : BigDecimal 타입에서 사용한다. (BigInteger 도 사용할 수 있다.) <br>
                              precision 은 소수점을 포함한 전체 자릿수를, scale 은 소수의 자릿수 <br>
                              참고로 double, float 타입에는 적용되지 않는다. 정밀한 소수를 다루어야 할 때만 사용한다. 
  + @Enumerated
    - 주의! ORDINAL 사용 X
    - enum type 이 추가되면(즉, user, admin 에 guest 가 추가되면) 순서가 바뀔 수도 있다. 굉장히 위험하다.
    - ORDINAL : enum 의 순서를 데이터베이스에 저장
    - STRING : enum 의 이름을 데이터베이스에 저장
  + @ Temporal(DATE, TIME, TIMESTAMP 중 하나를 지정)
    - private LocalDate localDate;
    - private LocalDateTime localDateTime;
    - 위 2가지 방법처럼 사용하면 알아서 생성해준다. 최신버전은 이를 지원하므로 이렇게 사용한다.
  + @Lob
    - 데이터베이스 BLOB, CLOB 매핑
    - @Lob 에는 지정할 수 있는 속성이 없다. 
    - 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑
      - CLOB : String, char[], java.sql.CLOB
      - BLOB : byte[], java.sql.BLOB
  + @Transient
    - 필드 매핑 X
    - 데이터베이스에 저장 X, 조회 X
    - 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용 
<br>

- 기본 키 매핑
```markdown
- @Id : 직접 할당
  
- @GeneratedValue : 자동 생성
    - IDENTITY
        + auto_increment
        + insert sql 을 실행한 이후에 ID 값을 알 수 있다. => em.persist() 시점에 실제 DB insert 날아간다.
    - SEQUENCE
        + 주로 Oracle 에서 사용
        + sequence 에서 값을 가져와서 set
        + class 마다 @SequenceGenerator 설정을 통해 다르게 설정값을 줄 수 있다.
        + call next value for 관련 query 가 날아간다. 즉 pk 를 가져온 뒤 -> 영속성 컨텍스트에 넣고 -> tx.commit 때 insert
        + @SequenceGenerator 설정에서 allocationSize = 50 과 같이 설정을 해서 50개를 미리 가져올 수 있다(1, 51 까지 next value 를 가져와서 설정해둠).
        + 동시성 문제도 해결한다.
    - TABLE
        + 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략 / 모든 데이터베이스에 적용 가능 but 성능 이슈
    - AUTO
        + 방언에 맞춰서 위 3개 중 자동으로 set 된다.
```

- 기본 키 제약 조건 : Not null, unique, 변하면 안됨
- 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자
- 권장 : Long 형 + 대체키 + 키 생성 전략 사용 -> 즉, auto_increment 나 uuid
- 주민번호와 같이 비즈니스 로직을 가져오지 말자

### 프록시
- 프록시 기초
  - em.find() vs em.getReference()
  - em.find() : 데이터베이스를 통해서 실제 엔티티 객체 조회
  - em.getReference() : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
- 프록시 특징
  - 실제 클래스를 상속 받아서 만들어짐
  - 실제 클래스와 겉모양이 같다.
  - 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 된다.
  - 프록시 없으면 영속성 컨텍스트에 초기화를 요청에서 실제 엔티티를 달라고 한다. -> 실제 엔티티 생성
  - 프록시 객체는 처음 사용할 때 한번만 초기화된다.
  - 초기화할 때 프록시 객체가 실제 엔티티로 바뀌는 게 아니라 실제 엔티티에 접근 가능한 것이다!!!
  - 타입 체크할 때는 instanceof 사용하기!!!
  - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference() 를 호출해도 실제 엔티티 반환
  - (***) 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제가 발생한다. org.hibernate.LazyInitializationException
- 프록시 확인
  - 프록시 인스턴스의 초기화 여부 확인 PersistenceUnitUtil.isLoaded(Object entity)
  - 프록시 클래스 확인 방법
  - 프록시 강제 초기화