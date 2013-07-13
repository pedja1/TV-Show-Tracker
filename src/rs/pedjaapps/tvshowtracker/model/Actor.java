package rs.pedjaapps.tvshowtracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pedja on 5/21/13.
 */
public class Actor {
	private int _id;
	@Expose @SerializedName("id") private String actorId;
    @Expose @SerializedName("Name")private String name;
    @Expose @SerializedName("Role")private String role;
    @Expose @SerializedName("Image") private String image;
    private String profile;

    public Actor(){
    	
    }
    
    public Actor(int _id, String actorId, String name, String role, String image, String profile) {
    	this._id = _id;
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

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
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
