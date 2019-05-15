package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "medal")
public class Medal {

	@Id
	@Column(name = "medal_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer medal_id;
	
	@ManyToOne
    @JoinColumn
    private Peak peak;
	
	@OneToMany(mappedBy = "medal", cascade = CascadeType.ALL)
    private List<UserMedal> userMedals;

	public Integer getMedal_id() {
		return medal_id;
	}

	public void setMedal_id(Integer medal_id) {
		this.medal_id = medal_id;
	}

	public Peak getPeak() {
		return peak;
	}

	public void setPeak(Peak peak) {
		this.peak = peak;
	}

	public List<UserMedal> getUserMedals() {
		return userMedals;
	}

	public void setUserMedals(List<UserMedal> userMedals) {
		this.userMedals = userMedals;
	}

	@Override
	public String toString() {
		return "Medal [medal_id=" + medal_id + ", peak=" + peak + ", userMedals=" + userMedals + "]";
	}
}
