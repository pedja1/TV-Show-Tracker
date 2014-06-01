package rs.pedjaapps.tvshowtracker.model;

import java.util.*;

public class JsonShow
{
	private ShowOld show;
	private List<EpisodeItem> episodes;
	private List<ActorOld> actors;

	public JsonShow(ShowOld show, List<EpisodeItem> episodes, List<ActorOld> actors)
	{
		this.show = show;
		this.episodes = episodes;
		this.actors = actors;
	}

	public void setShow(ShowOld show)
	{
		this.show = show;
	}

	public ShowOld getShow()
	{
		return show;
	}

	public void setEpisodes(List<EpisodeItem> episodes)
	{
		this.episodes = episodes;
	}

	public List<EpisodeItem> getEpisodes()
	{
		return episodes;
	}

	public void setActors(List<ActorOld> actors)
	{
		this.actors = actors;
	}

	public List<ActorOld> getActors()
	{
		return actors;
	}
	/*private String series_name;
	 private String first_aired;
	 private String imdb_id;
	 private String overview;
	 private double rating;
	 private int series_id;
	 private String language;
	 private String banner;
	 private String fanart;
	 private String network;
	 private int runtime;
	 private String status;
	 //private boolean ignore_agenda;
	 //private boolean hide_from_list;
	 private String updated;
	 //private String actors;
	 private String profile_name;
	 private List<JsonEpisode> episodes;
	 private List<JsonActor> actors;

	 public JsonShow(String series_name, String first_aired, String imdb_id, String overview, double rating, int series_id, String language, String banner, String fanart, String network, int runtime, String status, String updated, String profile_name, List<JsonEpisode> episodes, List<JsonActor> actors)
	 {
	 this.series_name = series_name;
	 this.first_aired = first_aired;
	 this.imdb_id = imdb_id;
	 this.overview = overview;
	 this.rating = rating;
	 this.series_id = series_id;
	 this.language = language;
	 this.banner = banner;
	 this.fanart = fanart;
	 this.network = network;
	 this.runtime = runtime;
	 this.status = status;
	 this.updated = updated;
	 this.profile_name = profile_name;
	 this.episodes = episodes;
	 this.actors = actors;
	 }*/


}
