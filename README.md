# Lv4: 페이지네이션

## 구현 사항

- 모든 일정 조회시 페이지네이션 기능 추가 (아래는 변경된 DTO)
  ```java
  @Getter
  public class PageResponse<T> {

	private List<T> content;
	private int currentPage;
	private int pageSize;
	private long totalElements;
	private int totalPages;

	public PageResponse(List<T> content, int currentPage, int pageSize, long totalElements) {
	  this.content = content;
	  this.currentPage = currentPage;
	  this.pageSize = pageSize;
	  this.totalElements = totalElements;
	  this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
	}
  }
  ```

## 구현 결과

![ezgif-7-e02664120d](https://github.com/user-attachments/assets/edd7d9d6-bb92-40ec-a24d-8cb9b263db69)
