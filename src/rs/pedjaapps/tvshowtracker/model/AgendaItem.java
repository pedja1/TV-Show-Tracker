package rs.pedjaapps.tvshowtracker.model;

public class AgendaItem implements Agenda{

	private String episodeName;
	private String banner;
	private String nextEpisode;
	
	public AgendaItem(String episodeName, String banner, String nextEpisode) {
		super();
		this.episodeName = episodeName;
		this.banner = banner;
		this.nextEpisode = nextEpisode;
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
