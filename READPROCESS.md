## 목차

- [고민과 해결과정](#고민과-해결과정)
	- [DB 접근 정보 은닉화](#db-접근-정보-은닉화)
	- [정규화 과정에서 기존에 있던 데이터 처리](#정규화-과정에서-기존에-있던-데이터-처리)
- [트러블 슈팅](#트러블-슈팅)
	- [MySQL 드라이버 로딩 실패](#mysql-드라이버-로딩-실패)
	- [MySQL 사용자 권한 설정](#mysql-사용자-권한-설정)
	- [n + 1 문제?](#n--1-문제)

<br/>

## 고민과 해결과정

### DB 접근 정보 은닉화

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:4444/scheduler
    username: user
    password: password
```

위와 같은 중요한 정보를 github에 그냥 올릴 수가 없다.

환경 변수를 사용하는 방법도 있고, 프로파일을 이용한 설정을 분리하는 방법이 있는데, 후자를 경험 해보기로 했다.

application-env.yml 이라는 파일을 만들어서 여기로 옮기고, application.yml에는 아래의 정보를 추가했다.

```yaml
spring:
  profiles:
    include:
      - env # application-env.yml 포함함
```

이로써 github에는 db 접근 관련 정보가 올라가지 않는 것이다.

그런데, 팀원끼리 개발할 때.. 이 application-env.yml을 공유할 수는 없을까?

깃헙의 서브모듈을 활용해서 서브모듈을 private으로 하는 방법도 있다고 한다.


<br/>

### 정규화 과정에서 기존에 있던 데이터 처리

<div align="center">
<img width="300" alt="center" src="https://github.com/user-attachments/assets/307f76d4-608b-4009-bd64-ef5e70f19ffb" />
</div>

기존에 Before 상태에 있던 테이블을 After 상태의 테이블로 분리하려 한다.

조금 어려운 말로 password는 author컬럼 만으로도 종속되는데 이런 부분적인 종속관계가 존재하기 때문에, 완전 함수 종속을 만족하도록 제 2정규화를 하는 것이다.

그런데 기존 Schedule 테이블에 10개 정도의 데이터들이 들어가 있었는데 이것들을 전부 삭제하는 건 말이 안되지 않는가

알아보니 이 구조를 바꾸는게 힘들다는 것이 바로 **RDB의 안좋은 점**이며 정규화 과정을 거칠때 데이터를 백업해두고, 스키마를 변경하고, 새로운 스키마에 맞게 마이그레이션하고,
검증하는 과정을 거치며 이 과정은 생각보다 시간비용이 많이 든다고 한다.

이런 이유로 정규화 과정이 설계 이후 단계에서 일어나는 건 극히 드문일이어야 하며, 테이블 설계하는 작업은 신입한테 잘 안맡긴다고 한다. ㅋㅋㅋㅋ

<br/>


## 트러블 슈팅

### MySQL 드라이버 로딩 실패

JdbcTemplate을 초기화 하는 과정에서 에러가 났다.

<img width="1000" src="https://github.com/user-attachments/assets/bf4fd335-900a-4c46-9fb5-ebbc6061eded">

<pre>
Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException

: Error creating bean with name 'apiScheduleController' defined in file [~~/ApiScheduleController.class]: Unsatisfied dependency expressed through constructor parameter 0

: Error creating bean with name 'scheduleService' defined in file [~~/ScheduleService.class]: Unsatisfied dependency expressed through constructor parameter 0

<strong>: Error creating bean with name 'jdbcTemplateScheduleRepository' defined in file [~~/JdbcTemplateScheduleRepository.class]: Unsatisfied dependency expressed through constructor parameter 0</strong>

: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSourceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0

: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Cannot load 

driver class: com.mysql.cj.jdbc.Driver
</pre>

위에서부터 아래로 읽다가 내가 작성하지 않은 클래스가 나오기 직전의 부분이 보통 문제다.

apiScheduleController(내가 작성) -> scheduleService(내가 작성) -> jdbcTemplateScheduleRepository(내가 작성) ->
dataSourceScriptDatabaseInitializer(내가 안 작성)

저 jdbcTemplateScheduleRepository 가 문제라고 한다. 확인해보자

<img width="500" src="https://github.com/user-attachments/assets/203f2fbe-3c34-4b5b-85ed-dc3ccf08d42d">

오 세상에.. 저 template에 `template = new JdbcTemplate(datasource)`로 초기화를 시켜주어야 하는데, 습관처럼 저렇게 JdbcTemplate
빈을 바로 찾으려 했다.

아래와 같이 바꿔주었다.

<img width="450" src="https://github.com/user-attachments/assets/265eac4c-52e4-4e98-82e8-f23efa988d03">

이제 되겠지? 했지만, 다시 에러가 발생했다.

<img width="1000" src="https://github.com/user-attachments/assets/54432c92-27e8-483e-a758-4ea64de74663">

여전히 jdbcTemplateScheduleRepository 빈을 생성하는데 문제가 생긴다고 한다.

자동주입을 하면서 어떤 문제가 생긴건가? 해서 수동주입으로 바꿔봤다.

<img width="400" src="https://github.com/user-attachments/assets/66c0dd82-a3d2-4ddd-a6db-0b3dcbd722aa">

<img width="500" src="https://github.com/user-attachments/assets/f26e5a59-0e9d-44a7-9b51-3f2cf7746aad">

<pre>
Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException

: Error creating bean with name 'jdbcTemplateConfig' defined in file [~~/JdbcTemplateConfig.class]: Unsatisfied dependency expressed through constructor parameter 0

: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]
<strong>: Factory method 'dataSource' threw exception with message: Cannot load driver class: com.mysql.cj.jdbc.Driver</strong>
</pre>

여전히 같은 에러가 발생한다. 이제 그 아래에 있는 메시지에 집중해야 할 때다.

"Cannot load driver class: com.mysql.cj.jdbc.Driver" 처음에 발견하지 못했던 메시지를 발견했다.

빈 등록하는 내 코드 자체에는 문제가 없지만, 저 드라이버를 로드하지 못한다고 한다.

저 드라이버 의존관계를 명시한 build.gradle을 확인해보자

<img width="500" src="https://github.com/user-attachments/assets/73b25d89-74c6-4bbb-a69f-0934e73c8a26">

저 드라이버에 버전을 명시하지 않았다.

의존성을 선언할 때에는 라이브러리의 정확한 버전을 명시하는게 기본인데, 스프링 부트는 자체적으로 많은 의존성들의 버전을 관리해서 버전을 명시하지 않아도 되는 것들이 많았다.

그래서 습관처럼 MySQL 드라이버도 버전을 명시하지 않았는데, Spring Boot의 의존성 관리 시스템이 이 드라이버의 적절한 버전을 자동으로 해결하지 못해서 발생한 거였다.

```java
runtimeOnly 'mysql:mysql-connector-java:8.0.28'
```

이렇게 버전까지 명시하니 정상적으로 스프링부트가 실행되는 것을 확인할 수 있었다.

<br/>

### MySQL 사용자 권한 설정

MySQL 서버에서 scheduler스키마를 생성하여 이 안에 Schedule 테이블을 생성해두고, 이제 인텔리제이에서 연동하려고 하니 내가 만든 스키마가 뜨지를 않았다.

<img width="200" src="https://github.com/user-attachments/assets/01f99b2d-1adc-45da-a31a-b1fa8aaa3fde">

root 사용자로 접근하는 게 아닌 userA라는 사용자를 만들어 이 사용자로 접근하게 하려고 했는데, 이 사용자에게는 scheduler 스키마의 테이블들에 접근이 제한되어 있던
것이다.

```sql
GRANT ALL PRIVILEGES ON scheduler.* TO 'userA'@'%';

FLUSH PRIVILEGES;
```

위의 명령을 통해 userA에게 scheduler 스키마의 모든 테이블에 권한을 열어두었다.

<img width="278" src="https://github.com/user-attachments/assets/0ed7d44c-f492-4f0b-a18d-54cbab5cb52d">

참고로 권한을 확인하거나 유저들 목록을 확인하는 명령어는 아래와 같다.

```sql
SHOW GRANTS FOR 'userA'@'localhost';
SELECT user, host
FROM mysql.user;
```

<img width="200" src="https://github.com/user-attachments/assets/9bf08d2b-d2af-4b32-9d9c-45094d872cfa">

권한을 열어두고 새로고침 해보니 위와같이 스키마와 연동할 수 있게 되었다.

<br/>

### n + 1 문제?

```java
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

	@Override
	public PageResponse<Schedule> findAll(int page, int size) {
		String rowCountSql = "SELECT count(*) FROM schedule";
		int total = template.queryForObject(rowCountSql, Integer.class);

		int offset = page * size;
		String sql = """
			SELECT id, member_id, content, created_at, updated_at
			FROM schedule
			ORDER BY updated_at DESC
			LIMIT ? OFFSET ?
			""";
		List<Schedule> schedules = template.query(
			sql,
			new Object[]{size, offset},
			scheduleRowMapper()
		);

		return new PageResponse<>(schedules, page, size, total);
	}

	private RowMapper<Schedule> scheduleRowMapper() {
		return (rs, rowNum) -> {
			//여기 집중
			Member member = memberRepository.findById(rs.getLong("member_id")).orElse(null);
			return new Schedule(
				rs.getLong("id"),
				member,
				rs.getString("content"),
				rs.getTimestamp("created_at").toLocalDateTime(),
				rs.getTimestamp("updated_at").toLocalDateTime()
			);
		};
	}
}
```

위는 Schedule 테이블로부터 모든 데이터를 가져오는 함수와, 이걸 Schedule 객체로 매핑하는 함수이다.

Schedule은 Member를 바라보고 있기 때문에, 해당 멤버들을 연결시켜주기 위해서 n개의 Schedule 데이터를 불러오면 n번의 쿼리를 추가로 날리고 있는 셈이었다.

실제로 debug 모드를 켜고 확인해보니 아래와 같은 쿼리가 계속 날라갔다.

<img width="150" src="https://github.com/user-attachments/assets/bb6d6386-b246-4594-9f0d-882de151e468">

저 member를 캐싱하는 방법도 있겠지만, 가장 간단한 방법으로 JOIN문을 통해서 해결해보았다.

```java
public Optional<Schedule> findById(Long id) {
	String sql = """
		SELECT s.id, s.member_id, s.content, s.created_at, s.updated_at,
		       m.name as member_name, m.email as member_email, m.password as member_password, m.created_at as member_created_at, m.updated_at as member_updated_at
		FROM schedule s
		JOIN member m ON s.member_id = m.id
		WHERE s.id = ?
		""";
	Schedule schedule = template.queryForObject(sql, scheduleRowMapper(), id);
	return Optional.ofNullable(schedule);
}
```

1개의 쿼리만 날아가는 것이 확인 가능하다.

<img width="1000" src="https://github.com/user-attachments/assets/b640d25c-fbca-4bd0-800b-c40ca574180c">

JPA는 어떻게 이 문제를 해결할지 궁금해졌다. 추후에 좀 더 학습해보자

내가 봤던 n+1문제는 1:n관계에서 1을 불러올때 그와 연관된 n을 n번의 쿼리로 불러오는
거였는데, 이렇게 n을 불러올때 1을 n번의 쿼리로 불러오는 것도 n+1 문제일 수도 있겠구나 생각이 들었다. 
