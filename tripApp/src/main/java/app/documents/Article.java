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
	private List<ArticleSection> sections;
	
	public Article() {}

	public Article(Integer articleId, Integer userId, String date, String title, List<ArticleSection> sections) {
		super();
		this.articleId = articleId;
		this.userId = userId;
		this.date = date;
		this.title = title;
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

	public List<ArticleSection> getSections() {
		return sections;
	}

	public void setSections(List<ArticleSection> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "Article [articleId=" + articleId + ", userId=" + userId + ", date=" + date
				+ ", title=" + title + "]";
	}
}
