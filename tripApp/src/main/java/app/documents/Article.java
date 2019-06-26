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
	private String description;
	private List<ArticleLike> likes;
	private List<ArticleSection> sections;
	private List<ArticleComment> comments;
	
	public Article() {}

	public Article(Integer articleId, Integer userId, String date, String title, List<ArticleLike> likes, String description, List<ArticleSection> sections, List<ArticleComment> comments) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ArticleLike> getLikes() {
		return likes;
	}

	public void setLikes(List<ArticleLike> likes) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (articleId == null) {
			if (other.articleId != null)
				return false;
		} else if (!articleId.equals(other.articleId))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (sections == null) {
			if (other.sections != null)
				return false;
		} else if (!sections.equals(other.sections))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
