# LV2: 일정 CRUD

## 구현 사항
- (기능) 사용자는 일정을 추가, 조회, 수정, 삭제할 수 있다.
  - `POST /api/schedules`
  - `GET /api/schedules`
  - `GET /api/schedules/{id}`
  - `PUT /api/schedules/{id}`
  - `DELETE /api/schedules/{id}`

- (기술) MySQL 연동
  - DB 접근 기술로는 JDBC Template 사용
  - applications-env.yml 따로 만들어서 접근 정보 은닉화

## 구현 결과

- POST

  <img width="500" alt="스크린샷 2024-12-05 오후 3 02 21" src="https://github.com/user-attachments/assets/51f1706e-5632-48c1-8a35-128bd7303fa3">
- GET all

  <img width="500" alt="스크린샷 2024-12-05 오후 3 02 33" src="https://github.com/user-attachments/assets/bfa56d93-c8b0-42a3-a408-8db266aad538">
- GET

  <img width="500" alt="스크린샷 2024-12-05 오후 3 02 45" src="https://github.com/user-attachments/assets/cde96be8-bb1b-498b-b4c1-55dd1414e77f">
- PUT

  <img width="500" alt="스크린샷 2024-12-05 오후 3 03 03" src="https://github.com/user-attachments/assets/fcbb7baf-f97d-455c-8184-11fcb774ec4e">
- DELETE

  <img width="500" alt="스크린샷 2024-12-05 오후 3 03 14" src="https://github.com/user-attachments/assets/2d535d83-91cd-4027-9d17-7c55ed2e137f">


## ERD 설계

```mermaid
erDiagram
Task {
	bigint id PK "고유 식별자"
	varchar author "작성자명"
	varchar password "비밀번호"
	varchar content "내용"
	datetime created_at "등록 시간"
	datetime updated_at "수정 시간"
}
```
