package app.dto;

public class PromoCodeDTO {
	
	private Integer id;
	private String value;
	private String status;
	
	public PromoCodeDTO() {}
	
	public PromoCodeDTO(Integer id, String value, String status) {
		super();
		this.id = id;
		this.value = value;
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
