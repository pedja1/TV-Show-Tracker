package rs.pedjaapps.trakttvandroid.model;

public class Profile {
	private String name;
	private boolean active;
	public Profile(String name, boolean active) {
		super();
		this.name = name;
		this.active = active;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
