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
}
