package app.models;

import app.dto.ArticleDTO;

public class ArticleDetailsViewModel {
	
	private ArticleDTO articleDTO;

	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}

	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}

	@Override
	public String toString() {
		return "ArticleDetailsViewModel [articleDTO=" + articleDTO + "]";
	}
}
