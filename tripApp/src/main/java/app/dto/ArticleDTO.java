package app.dto;

import java.util.List;

import app.documents.ArticleSection;

public class ArticleDTO {

	private Integer articleId;
	private Integer userId;
	private String date;
	private String title;
	private Integer likes;
	private List<ArticleSection> sections;
	
	public ArticleDTO() {}
	
	public ArticleDTO(Integer articleId, Integer userId, String date, String title, Integer likes,
			List<ArticleSection> sections) {
		super();
		this.articleId = articleId;
		this.userId = userId;
		this.date = date;
		this.title = title;
		this.likes = likes;
		this.sections = sections;
	}
	
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public List<ArticleSection> getSections() {
		return sections;
	}
	public void setSections(List<ArticleSection> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "ArticleDTO [articleId=" + articleId + ", userId=" + userId + ", date=" + date + ", title=" + title
				+ ", likes=" + likes + ", sections=" + sections + "]";
	}
}
