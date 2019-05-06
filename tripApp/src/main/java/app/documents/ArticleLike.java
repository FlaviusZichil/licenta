package app.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article_like")
public class ArticleLike {
	
	private Integer userId;
	private Integer articleId;
	
	public ArticleLike() {}
	
	public ArticleLike(Integer userId, Integer articleId) {
		super();
		this.userId = userId;
		this.articleId = articleId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	@Override
	public String toString() {
		return "ArticleLike [userId=" + userId + ", articleId=" + articleId + "]";
	}
}
