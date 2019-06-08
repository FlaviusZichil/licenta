package app.models;

import java.util.List;

import app.dto.UsersReportsDTO;

public class UsersReportsViewModel {
	
	private List<UsersReportsDTO> users;
	private List<UsersReportsDTO> staffMembers;
	
	public List<UsersReportsDTO> getUsers() {
		return users;
	}
	public void setUsers(List<UsersReportsDTO> users) {
		this.users = users;
	}
	public List<UsersReportsDTO> getStaffMembers() {
		return staffMembers;
	}
	public void setStaffMembers(List<UsersReportsDTO> staffMembers) {
		this.staffMembers = staffMembers;
	}	
}
