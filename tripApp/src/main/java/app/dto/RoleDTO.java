package app.dto;

public class RoleDTO {
	
	private String roleName;

	public RoleDTO() {}
	
	public RoleDTO(String name) {
		this.roleName = name;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
