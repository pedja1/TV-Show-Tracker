package rs.pedjaapps.tvshowtracker.model;

import java.util.*;

public class JsonObject
{
    List<JsonShow> shows;
	List<String> profiles;

	public JsonObject(List<JsonShow> shows, List<String> profiles)
	{
		this.shows = shows;
		this.profiles = profiles;
	}

	public void setShows(List<JsonShow> shows)
	{
		this.shows = shows;
	}

	public List<JsonShow> getShows()
	{
		return shows;
	}

	public void setProfiles(List<String> profiles)
	{
		this.profiles = profiles;
	}

	public List<String> getProfiles()
	{
		return profiles;
	}
	
/*	public class JsonEpisode
	{
		private String episode_name;
		private int episode;
		private int season;
		private String first_aired;
		private String imdb_id;
		private String overview;
		private double rating;
		private boolean watched;
		private int episode_id;
		private String profile;

		public JsonEpisode(String episode_name, int episode, int season, String first_aired, String imdb_id, String overview, double rating, boolean watched, int episode_id, String profile)
		{
			this.episode_name = episode_name;
			this.episode = episode;
			this.season = season;
			this.first_aired = first_aired;
			this.imdb_id = imdb_id;
			this.overview = overview;
			this.rating = rating;
			this.watched = watched;
			this.episode_id = episode_id;
			this.profile = profile;
		}

	}
	
	public class JsonActor
	{
		private String actor_id;
		private String name;
		private String role;
		private String image;
		private String profile;

		public JsonActor(String actor_id, String name, String role, String image, String profile)
		{
			this.actor_id = actor_id;
			this.name = name;
			this.role = role;
			this.image = image;
			this.profile = profile;
		}

		
	}*/
}
