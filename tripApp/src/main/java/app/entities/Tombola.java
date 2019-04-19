package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tombola")
public class Tombola {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tombola_id")
    private Integer tombolaId;
 
    @Column(name = "date")
    private String date;
    
    @Column(name = "status")
    private String status;
    
    @ManyToOne
	@JoinColumn
	private UserEntity userTombola;
   
    public Tombola() {}
   
	public Tombola(String date, String status, UserEntity userTombola) {
		super();
		this.date = date;
		this.status = status;
		this.userTombola = userTombola;
	}

	public Integer getTombolaId() {
		return tombolaId;
	}

	public void setTombolaId(Integer tombolaId) {
		this.tombolaId = tombolaId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserEntity getUser() {
		return userTombola;
	}

	public void setUser(UserEntity user) {
		this.userTombola = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Tombola [tombolaId=" + tombolaId + ", date=" + date + ", status=" + status + ", userTombola="
				+ userTombola + "]";
	}
	
	
}
