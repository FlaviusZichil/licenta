package app.dto;

import app.documents.Article;
import app.entities.UserEntity;

public class ArticleLikeDTO {
	
	private UserEntity user;
	private Article article;
	
	public ArticleLikeDTO() {}

	public ArticleLikeDTO(UserEntity user, Article article) {
		super();
		this.user = user;
		this.article = article;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "ArticleLikeDTO [user=" + user + ", article=" + article + "]";
	}
}
