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
@Table(name = "user_medal")
public class UserMedal {
	@Id
	@Column(name = "user_medal_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_medal_id;
	
	@ManyToOne
    @JoinColumn
    private Medal medal;
	
	@ManyToOne
    @JoinColumn
    private UserEntity user;
	
	public Integer getUser_medal_id() {
		return user_medal_id;
	}

	public void setUser_medal_id(Integer user_medal_id) {
		this.user_medal_id = user_medal_id;
	}

	public Medal getMedal() {
		return medal;
	}

	public void setMedal(Medal medal) {
		this.medal = medal;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserMedal [user_medal_id=" + user_medal_id + ", medal=" + medal + "]";
	}
}
