package app.models;

import java.util.List;

import app.dto.ArticleDTO;

public class AllArticlesViewModel {
	
	private List<ArticleDTO> articlesDTO;

	public List<ArticleDTO> getArticlesDTO() {
		return articlesDTO;
	}

	public void setArticlesDTO(List<ArticleDTO> articles) {
		this.articlesDTO = articles;
	}

	@Override
	public String toString() {
		return "AllArticlesViewModel [articles=" + articlesDTO + "]";
	}
}
