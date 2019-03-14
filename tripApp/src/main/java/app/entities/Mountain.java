package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mountain")
public class Mountain {

	@Id
	@Column(name = "mountain_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "mountain_name")
	private String mountainName;
	
	@OneToMany(mappedBy = "mountain", cascade = CascadeType.ALL)
    private List<Peak> peaks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMountainName() {
		return mountainName;
	}

	public void setMountainName(String mountainName) {
		this.mountainName = mountainName;
	}

	public List<Peak> getPeaks() {
		return peaks;
	}

	public void setPeaks(List<Peak> peaks) {
		this.peaks = peaks;
	}

	@Override
	public String toString() {
		return "Mountain [mountainName=" + mountainName + "]";
	}
}
