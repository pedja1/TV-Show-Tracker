package rs.pedjaapps.tvshowtracker;

public class EpisodeSection implements Episode{

	private int season;
	
	public EpisodeSection(int season) {
		super();
		this.season = season;
	}

	public int getSeason() {
		return season;
	}

	@Override
	public boolean isSection() {
		return true;
	}

}
