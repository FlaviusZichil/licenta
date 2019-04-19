package app.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article_section")
public class ArticleSection {
	
	@Id
	private Integer sectionId;
	private String sectionTitle;
	private String sectionContent;
	
	public ArticleSection() {}
	
	public ArticleSection(Integer sectionId, String sectionTitle, String sectionContent) {
		super();
		this.sectionId = sectionId;
		this.sectionTitle = sectionTitle;
		this.sectionContent = sectionContent;
	}
	public Integer getSectionId() {
		return sectionId;
	}
	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionTitle() {
		return sectionTitle;
	}
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	public String getSectionContent() {
		return sectionContent;
	}
	public void setSectionContent(String sectionContent) {
		this.sectionContent = sectionContent;
	}
	
	@Override
	public String toString() {
		return "ArticleSection [sectionId=" + sectionId + ", sectionTitle=" + sectionTitle + ", sectionContent="
				+ sectionContent + "]";
	}
}
