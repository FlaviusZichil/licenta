package app.dto;

import app.entities.UserEntity;

public class ArticleCommentDTO {

	private Integer commentId;
	private UserEntity user;
	private String date;
	private String content;
	private Integer daysSincePosted;
	
	public ArticleCommentDTO() {}

	public ArticleCommentDTO(Integer commentId, UserEntity user, String date, String content, Integer daysSincePosted) {
		super();
		this.commentId = commentId;
		this.user = user;
		this.date = date;
		this.content = content;
		this.daysSincePosted = daysSincePosted;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
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

	public Integer getDaysSincePosted() {
		return daysSincePosted;
	}

	public void setDaysSincePosted(Integer daysSincePosted) {
		this.daysSincePosted = daysSincePosted;
	}

	@Override
	public String toString() {
		return "ArticleCommentDTO [commentId=" + commentId + ", user=" + user + ", date=" + date + ", content="
				+ content + ", daysSincePosted=" + daysSincePosted + "]";
	}
}
