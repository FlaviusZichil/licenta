package app.dto;

public class UsersReportsDTO {

	private UserDTO userDTO;
	private Integer finishedTrips;
	private Integer absences;
	private Integer articlesPosted;
	
	public UsersReportsDTO() {}

	public UsersReportsDTO(UserDTO userDTO, Integer finishedTrips, Integer absences, Integer articlesPosted) {
		super();
		this.userDTO = userDTO;
		this.finishedTrips = finishedTrips;
		this.absences = absences;
		this.articlesPosted = articlesPosted;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public Integer getFinishedTrips() {
		return finishedTrips;
	}

	public void setFinishedTrips(Integer finishedTrips) {
		this.finishedTrips = finishedTrips;
	}

	public Integer getAbsences() {
		return absences;
	}

	public void setAbsences(Integer absences) {
		this.absences = absences;
	}

	public Integer getArticlesPosted() {
		return articlesPosted;
	}

	public void setArticlesPosted(Integer articlesPosted) {
		this.articlesPosted = articlesPosted;
	}

	@Override
	public String toString() {
		return "UsersReportsDTO [userDTO=" + userDTO + ", finishedTrips=" + finishedTrips + ", absences=" + absences
				+ ", articlesPosted=" + articlesPosted + "]";
	}	
}
