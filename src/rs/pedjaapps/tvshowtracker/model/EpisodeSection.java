package rs.pedjaapps.tvshowtracker.model;

public class EpisodeSection implements EpisodeOld
{

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
