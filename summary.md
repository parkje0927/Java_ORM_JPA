### 데이터베이스 방언
- JPA 는 특정 데이터베이스에 종속 X
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다름
    + 가변 문자 : MySQL 은 VARCHAR, Oracle 은 VARCHAR2
    + 문자열을 자르는 함수 : SQL 표준은 SUBSTRING(), Oracle 은 SUBSTR()
    + 페이징 : MySQL 은 LIMIT,  Oracle ROWNUM
- 방언 : SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능 / 여기서는 h2.Dialect 

