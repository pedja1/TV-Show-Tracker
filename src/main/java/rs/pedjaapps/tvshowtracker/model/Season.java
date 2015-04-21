package rs.pedjaapps.tvshowtracker.model;

import java.util.List;

/**
 * Created by pedja on 12.6.14..
 */
public class Season
{
    private int season;
    private List<Episode> episodes;

    public int getSeason()
    {
        return season;
    }

    public void setSeason(int season)
    {
        this.season = season;
    }

    public List<Episode> getEpisodes()
    {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes)
    {
        this.episodes = episodes;
    }
}
