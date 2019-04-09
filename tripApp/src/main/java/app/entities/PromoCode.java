package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "promo_code")
public class PromoCode {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_code_id")
    private Integer promoCodeId;
 
    @OneToOne(mappedBy = "promoCode")
    private UserEntity user;

    @Column(name = "value")
    private String value;
    
    @Column(name = "status")
    private String status;

    public PromoCode() {}
    
	public PromoCode(String value, String status) {
		super();
		this.value = value;
		this.status = status;
	}

	public Integer getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(Integer promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
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

	@Override
	public String toString() {
		return "PromoCode [value=" + value + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((promoCodeId == null) ? 0 : promoCodeId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromoCode other = (PromoCode) obj;
		if (promoCodeId == null) {
			if (other.promoCodeId != null)
				return false;
		} else if (!promoCodeId.equals(other.promoCodeId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	} 
}
