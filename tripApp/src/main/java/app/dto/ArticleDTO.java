package app.dto;

import java.util.List;
import app.documents.ArticleSection;
import app.entities.UserEntity;

public class ArticleDTO {

	private Integer articleId;
	private UserEntity user;
	private String date;
	private String title;
	private String description;
	private List<ArticleLikeDTO> likesDTO;
	private List<ArticleSection> sections;
	private List<ArticleCommentDTO> commentsDTO;
	public ArticleDTO() {}
	
	public ArticleDTO(Integer articleId, UserEntity user, String date, String title, List<ArticleLikeDTO> likesDTO, String description,
			List<ArticleSection> sections, List<ArticleCommentDTO> commentsDTO) {
		super();
		this.articleId = articleId;
		this.user = user;
		this.date = date;
		this.title = title;
		this.likesDTO = likesDTO;
		this.description = description;
		this.sections = sections;
		this.commentsDTO = commentsDTO;
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

	public List<ArticleLikeDTO> getLikesDTO() {
		return likesDTO;
	}

	public void setLikesDTO(List<ArticleLikeDTO> likesDTO) {
		this.likesDTO = likesDTO;
	}

	public List<ArticleSection> getSections() {
		return sections;
	}
	public void setSections(List<ArticleSection> sections) {
		this.sections = sections;
	}

	public List<ArticleCommentDTO> getCommentsDTO() {
		return commentsDTO;
	}

	public void setCommentsDTO(List<ArticleCommentDTO> commentsDTO) {
		this.commentsDTO = commentsDTO;
	}

	@Override
	public String toString() {
		return "ArticleDTO [articleId=" + articleId + ", user=" + user + ", date=" + date + ", title=" + title
				+ ", description=" + description + ", likesDTO=" + likesDTO + ", sections=" + sections
				+ ", commentsDTO=" + commentsDTO + "]";
	}
}
