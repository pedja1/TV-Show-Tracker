package rs.pedjaapps.tvshowtracker.model;

import java.util.List;
/**
 * Show object that isn't attached to dao context*/
public class ShowNoDao extends Show
{
    private Image image;

    private List<Actor> actors;
    private List<Genre> genres;
    private List<Episode> episodes;

    @Override
    public Image getImage()
    {
        return image;
    }

    @Override
    public void setImage(Image image)
    {
        this.image = image;
    }

    @Override
    public List<Actor> getActors()
    {
        return actors;
    }

    @Override
    public List<Genre> getGenres()
    {
        return genres;
    }

    @Override
    public List<Episode> getEpisodes()
    {
        return episodes;
    }

    public void setActors(List<Actor> actors)
    {
        this.actors = actors;
    }

    public void setGenres(List<Genre> genres)
    {
        this.genres = genres;
    }

    public void setEpisodes(List<Episode> episodes)
    {
        this.episodes = episodes;
    }

    /*public Show toShow()
    {
        new Show()
    }*/
}
