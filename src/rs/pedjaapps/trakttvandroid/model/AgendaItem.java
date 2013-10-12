package rs.pedjaapps.trakttvandroid.model;

public class AgendaItem implements Agenda{

	private String episodeName;
	private String banner;
	private String nextEpisode;
	private String airDate;
	private String overview;

	public AgendaItem(String episodeName, String banner, String nextEpisode, String airDate, String overview)
	{
		this.episodeName = episodeName;
		this.banner = banner;
		this.nextEpisode = nextEpisode;
		this.airDate = airDate;
		this.overview = overview;
	}

	public void setOverview(String overview)
	{
		this.overview = overview;
	}

	public String getOverview()
	{
		return overview;
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
