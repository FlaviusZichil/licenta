package app.dto;

import app.entities.UserEntity;

public class ArticleCommentDTO {

	private UserEntity user;
	private String date;
	private String content;
	
	public ArticleCommentDTO() {}
	
	public ArticleCommentDTO(UserEntity user, String date, String content) {
		super();
		this.user = user;
		this.date = date;
		this.content = content;
	}

	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
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
		return "ArticleCommentDTO [user=" + user + ", date=" + date + ", content=" + content + "]";
	}	
}
