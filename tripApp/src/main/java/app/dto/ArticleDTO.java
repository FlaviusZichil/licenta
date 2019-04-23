package app.dto;

import java.util.List;

import app.documents.ArticleSection;
import app.entities.UserEntity;

public class ArticleDTO {

	private Integer articleId;
	private UserEntity user;
	private String date;
	private String title;
	private Integer likes;
	private String description;
	private List<ArticleSection> sections;
	
	public ArticleDTO() {}
	
	public ArticleDTO(Integer articleId, UserEntity user, String date, String title, Integer likes, String description,
			List<ArticleSection> sections) {
		super();
		this.articleId = articleId;
		this.user = user;
		this.date = date;
		this.title = title;
		this.likes = likes;
		this.description = description;
		this.sections = sections;
	}
	
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		return "ArticleDTO [articleId=" + articleId + ", user=" + user + ", date=" + date + ", title=" + title
				+ ", likes=" + likes + ", sections=" + sections + "]";
	}
}
