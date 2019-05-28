package app.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "register")
public class Register {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Integer register_id;
 
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    
    @Column
    private String registerDate;
    
    public Register() {}

	public Register(UserEntity user, String registerDate) {
		super();
		this.user = user;
		this.registerDate = registerDate;
	}

	public Integer getRegister_id() {
		return register_id;
	}

	public void setRegister_id(Integer register_id) {
		this.register_id = register_id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return "Register [registerDate=" + registerDate + "]";
	}
}
