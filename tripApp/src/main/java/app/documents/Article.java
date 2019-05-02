package app.documents;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article")
public class Article {
	
	@Id
	private Integer articleId;
	private Integer userId;
	private String date;
	private String title;
	private Integer likes;
	private String description;
	private List<ArticleSection> sections;
	private List<ArticleComment> comments;
	
	public Article() {}

	public Article(Integer articleId, Integer userId, String date, String title, Integer likes, String description, List<ArticleSection> sections, List<ArticleComment> comments) {
		super();
		this.articleId = articleId;
		this.userId = userId;
		this.date = date;
		this.title = title;
		this.likes = likes;
		this.description = description;
		this.sections = sections;
		this.comments = comments;
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

	public List<ArticleComment> getComments() {
		return comments;
	}

	public void setComments(List<ArticleComment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Article [articleId=" + articleId + ", userId=" + userId + ", date=" + date + ", title=" + title
				+ ", likes=" + likes + ", description=" + description + ", sections=" + sections + ", comments="
				+ comments + "]";
	}
}
