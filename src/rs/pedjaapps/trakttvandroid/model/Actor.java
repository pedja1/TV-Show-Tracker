package rs.pedjaapps.trakttvandroid.model;

/**
 * Created by pedja on 5/21/13.
 */
public class Actor
{
	private String actorId;
    private String name;
    private String role;
    private String image;
    private String profile;
    private String seriesId;

	public Actor(String actorId, String name, String role, String image, String profile, String seriesId)
	{
		this.actorId = actorId;
		this.name = name;
		this.role = role;
		this.image = image;
		this.profile = profile;
		this.seriesId = seriesId;
	}

    public Actor(){
    	
    }

	public void setSeriesId(String seriesId)
	{
		this.seriesId = seriesId;
	}

	public String getSeriesId()
	{
		return seriesId;
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
