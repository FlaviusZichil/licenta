package app.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article_comment")
public class ArticleComment {

	private Integer commentId;
	private Integer userId;
	private String date;
	private String content;
	
	public ArticleComment() {}

	public ArticleComment(Integer commentId, Integer userId, String date, String content) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.date = date;
		this.content = content;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
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
}
