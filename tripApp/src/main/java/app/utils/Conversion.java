package app.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.documents.Article;
import app.documents.ArticleComment;
import app.documents.ArticleLike;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.dto.ArticleLikeDTO;

@Component
public class Conversion {
	
	@Autowired
	private ArticleUtils articleUtils;
	
	@Autowired
	private UserUtils userUtils;

	public ArticleDTO convertFromArticleToArticleDTO(Article article) {
		List<ArticleCommentDTO> commentsDTO = new ArrayList<>();
		List<ArticleLikeDTO> likesDTO = new ArrayList<>();
		
		for(ArticleComment comment : article.getComments()) {
			commentsDTO.add(convertFromArticleCommentToArticleCommentDTO(comment));
		}
		
		for(ArticleLike like : article.getLikes()) {
			likesDTO.add(convertFromArticleLikeToArticleLikeDTO(like));
		}
		
		Collections.reverse(commentsDTO);
		
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), userUtils.getUserById(article.getUserId()), article.getDate(), article.getTitle(), likesDTO, 
											   article.getDescription(), article.getSections(), commentsDTO);
		return articleDTO;
	}
	
	public ArticleCommentDTO convertFromArticleCommentToArticleCommentDTO(ArticleComment comment) {
		LocalDate articleDate = LocalDate.parse(comment.getDate());
		Period period = Period.between(LocalDate.now(), articleDate);
	    int days = Math.abs(period.getDays());
	    
		ArticleCommentDTO commentDTO = new ArticleCommentDTO(comment.getCommentId(), userUtils.getUserById(comment.getUserId()), comment.getDate(), comment.getContent(), days);
		return commentDTO;
	}
	
	public ArticleLikeDTO convertFromArticleLikeToArticleLikeDTO(ArticleLike like) {
		ArticleLikeDTO articleLikeDTO = new ArticleLikeDTO(userUtils.getUserById(like.getUserId()), articleUtils.getArticleById(like.getArticleId()));
		return articleLikeDTO;
	}
}
