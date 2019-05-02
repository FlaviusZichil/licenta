package app.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article_comment")
public class ArticleComment {

	private Integer userId;
	private String date;
	private String content;
	
	public ArticleComment() {}
	
	public ArticleComment(Integer userId, String date, String content) {
		super();
		this.userId = userId;
		this.date = date;
		this.content = content;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "ArticleComment [ user=" + userId + ", date=" + date + ", content=" + content
				+ "]";
	}
}
