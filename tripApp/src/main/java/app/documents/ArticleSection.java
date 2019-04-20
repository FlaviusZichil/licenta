package app.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article_section")
public class ArticleSection {
	
	private String sectionTitle;
	private String sectionContent;
	
	public ArticleSection() {}
	
	public ArticleSection(String sectionTitle, String sectionContent) {
		super();
		this.sectionTitle = sectionTitle;
		this.sectionContent = sectionContent;
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
		return "ArticleSection [sectionTitle=" + sectionTitle + ", sectionContent="
				+ sectionContent + "]";
	}
}
