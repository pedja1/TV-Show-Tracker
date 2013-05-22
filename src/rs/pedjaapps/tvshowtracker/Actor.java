package rs.pedjaapps.tvshowtracker;

/**
 * Created by pedja on 5/21/13.
 */
public class Actor {
	private int id;
	private String actorId;
    private String name;
    private String role;
    private String image;
    private String profile;

    public Actor(){
    	
    }
    
    public Actor(int id, String actorId, String name, String role, String image, String profile) {
    	this.id = id;
    	this.actorId = actorId;
        this.name = name;
        this.role = role;
        this.image = image;
        this.profile = profile;
    }
    public Actor(String actorId, String name, String role, String image, String profile) {
    	this.actorId = actorId;
        this.name = name;
        this.role = role;
        this.image = image;
        this.profile = profile;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
    
    
}
