package rs.pedjaapps.tvshowtracker.model;

public class AgendaItem implements Agenda{

	private String episodeName;
	private String banner;
	private String nextEpisode;
	private String airDate;
	
	public AgendaItem(String episodeName, String banner, String nextEpisode, String airDate) {
		super();
		this.episodeName = episodeName;
		this.banner = banner;
		this.nextEpisode = nextEpisode;
		this.airDate = airDate;
	}

	public void setAirDate(String airDate)
	{
		this.airDate = airDate;
	}

	public String getAirDate()
	{
		return airDate;
	}
	
	public String getEpisodeName() {
		return episodeName;
	}
	public String getBanner() {
		return banner;
	}
	public String getNextEpisode() {
		return nextEpisode;
	}
	
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return false;
	}

}
