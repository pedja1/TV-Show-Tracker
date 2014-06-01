package rs.pedjaapps.tvshowtracker.network;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import rs.pedjaapps.tvshowtracker.BuildConfig;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.DaoSession;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.Genre;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.Image;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.Tools;

/**
 * Created by pedja on 1/22/14.
 * Class responsible for handling all parsing of server responses in JSON format
 */
public class JSONUtility
{
    public enum Key
    {
        title, first_aired, country, overview, banner, air_day, air_time, images, network, year, url,
        runtime, status, fanart, poster, certification, imdb_id, tvdb_id, tvrage_id, last_updated, ratings,
        percentage, loved, hated, actors, people, name, character, headshot, genres, seasons, episodes,
        season, episode, screen, error
    }

    public enum RequestKey
    {
        query
    }

    public static List<Show> parseSearchResults(String query)
    {
        List<Show> shows = new ArrayList<Show>();
        query = URLEncoder.encode(query);
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQIEST_URL_SEARCH + "?" + RequestKey.query.toString() + "=" + query);
        if(!checkResponse(response))return null;
        try
        {
            JSONArray jsonArray = new JSONArray(response.responseData);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                Show show = new Show();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));
                if(jsonObject.has(Key.title.toString()))show.setTitle(jsonObject.getString(Key.title.toString()));
                if(jsonObject.has(Key.first_aired.toString()))show.setFirst_aired(jsonObject.getLong(Key.first_aired.toString()));
                if(jsonObject.has(Key.country.toString()))show.setCountry(jsonObject.getString(Key.country.toString()));
                if(jsonObject.has(Key.overview.toString()))show.setOverview(jsonObject.getString(Key.overview.toString()));
                if(jsonObject.has(Key.network.toString()))show.setNetwork(jsonObject.getString(Key.network.toString()));
                if(jsonObject.has(Key.air_day.toString()))show.setAir_day(jsonObject.getString(Key.air_day.toString()));
                if(jsonObject.has(Key.air_time.toString()))show.setAir_time(jsonObject.getString(Key.air_time.toString()));
                if(jsonObject.has(Key.year.toString()))show.setYear(jsonObject.getInt(Key.year.toString()));
                if(jsonObject.has(Key.images.toString()))
                {
                    JSONObject images = jsonObject.getJSONObject(Key.images.toString());
                    if(images.has(Key.banner.toString()))
                    {
                        show.setBanner(images.getString(Key.banner.toString()));
                    }
                }
                shows.add(show);
            }
        }
        catch (Exception e)
        {
            shows = null;
            if(BuildConfig.DEBUG)e.printStackTrace();
            if(BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
        }
        return shows;
    }

    public static Response parseShow(String tvdbId)
    {
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQIEST_URL_GET_SHOW_INFO + "/" + tvdbId + "/1");
        if(!checkResponse(response))
        {
            Response response1 = new Response();
            response1.status = false;
            response1.error = response.responseMessage;
            return response1;
        }
        try
        {
            JSONObject jsonObject = new JSONObject(response.responseData);
            if(jsonObject.has(Key.error.toString()))
            {
                return new Response().setError(jsonObject.getString(Key.error.toString())).setStatus(false);
            }
            Show show = new Show();
            List<Episode> episodes = new ArrayList<Episode>();
            List<Actor> actors = new ArrayList<Actor>();
            List<Genre> genres = new ArrayList<Genre>();
            if(jsonObject.has(Key.title.toString()))show.setTitle(jsonObject.getString(Key.title.toString()));
            if(jsonObject.has(Key.year.toString()))show.setYear(jsonObject.getInt(Key.year.toString()));
            if(jsonObject.has(Key.url.toString()))show.setUrl(jsonObject.getString(Key.url.toString()));
            if(jsonObject.has(Key.first_aired.toString()))show.setFirst_aired(jsonObject.getLong(Key.first_aired.toString()));
            if(jsonObject.has(Key.country.toString()))show.setCountry(jsonObject.getString(Key.country.toString()));
            if(jsonObject.has(Key.overview.toString()))show.setOverview(jsonObject.getString(Key.overview.toString()));
            if(jsonObject.has(Key.runtime.toString()))show.setRuntime(jsonObject.getInt(Key.runtime.toString()));
            if(jsonObject.has(Key.status.toString()))show.setStatus(jsonObject.getString(Key.status.toString()));
            if(jsonObject.has(Key.network.toString()))show.setNetwork(jsonObject.getString(Key.network.toString()));
            if(jsonObject.has(Key.air_day.toString()))show.setAir_day(jsonObject.getString(Key.air_day.toString()));
            if(jsonObject.has(Key.air_time.toString()))show.setAir_time(jsonObject.getString(Key.air_time.toString()));
            if(jsonObject.has(Key.certification.toString()))show.setCertification(jsonObject.getString(Key.certification.toString()));
            if(jsonObject.has(Key.imdb_id.toString()))show.setImdb_id(jsonObject.getString(Key.imdb_id.toString()));
            if(jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));
            if(jsonObject.has(Key.tvrage_id.toString()))show.setTvrage_id(jsonObject.getInt(Key.tvrage_id.toString()));
            if(jsonObject.has(Key.last_updated.toString()))show.setLast_updated(jsonObject.getLong(Key.last_updated.toString()));
            if(jsonObject.has(Key.images.toString()))
            {
                JSONObject images = jsonObject.getJSONObject(Key.images.toString());
                Image image = new Image();
                if(images.has(Key.banner.toString()))image.setBanner(images.getString(Key.banner.toString()));
                if(images.has(Key.fanart.toString()))image.setFanart(images.getString(Key.fanart.toString()));
                if(images.has(Key.poster.toString()))image.setPoster(images.getString(Key.poster.toString()));
                show.setImage(image);
                ImageDao imageDao = MainApp.getInstance().getDaoSession().getImageDao();
                long imageId = imageDao.insert(image);
                show.setImage_id(imageId);
            }
            if(jsonObject.has(Key.ratings.toString()))
            {
                JSONObject ratings = jsonObject.getJSONObject(Key.ratings.toString());
                if(ratings.has(Key.percentage.toString()))show.setRating(ratings.getInt(Key.percentage.toString()));
                if(ratings.has(Key.loved.toString()))show.setLoved(ratings.getInt(Key.loved.toString()));
                if(ratings.has(Key.hated.toString()))show.setHated(ratings.getInt(Key.hated.toString()));
            }
            show.setUser_id(MainApp.getInstance().getActiveUser().getId());
            ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
            long showId = showDao.insertOrReplace(show);
            if(jsonObject.has(Key.people.toString()))
            {
                JSONObject people = jsonObject.getJSONObject(Key.people.toString());
                if(people.has(Key.actors.toString()))
                {
                    JSONArray jActors = people.getJSONArray(Key.actors.toString());
                    for(int a = 0; a < jActors.length(); a++)
                    {
                        JSONObject jActor = jActors.getJSONObject(a);
                        Actor actor = new Actor();
                        if(jActor.has(Key.name.toString()))actor.setName(jActor.getString(Key.name.toString()));
                        if(jActor.has(Key.character.toString()))actor.setCharacter(jActor.getString(Key.character.toString()));
                        if(jActor.has(Key.images.toString()))
                        {
                            JSONObject images = jActor.getJSONObject(Key.images.toString());
                            if(images.has(Key.headshot.toString()))actor.setImage(images.getString(Key.headshot.toString()));
                        }
                        actor.setShow_id(showId);
                        actors.add(actor);
                    }
                    ActorDao actorDao = MainApp.getInstance().getDaoSession().getActorDao();
                    actorDao.insertOrReplaceInTx(actors);
                }
            }
            if(jsonObject.has(Key.genres.toString()))
            {
                JSONArray jGenres = jsonObject.getJSONArray(Key.genres.toString());
                for(int g = 0; g < jGenres.length(); g++)
                {
                    Genre genre = new Genre();
                    genre.setShow_id(showId);
                    genre.setName(jGenres.getString(g));
                    genres.add(genre);
                }
                GenreDao genreDao = MainApp.getInstance().getDaoSession().getGenreDao();
                genreDao.insertOrReplaceInTx(genres);
            }
            if(jsonObject.has(Key.seasons.toString()))
            {
                JSONArray seasons = jsonObject.getJSONArray(Key.seasons.toString());
                for(int s = 0; s < seasons.length(); s++)
                {
                    JSONObject season = seasons.getJSONObject(s);
                    if(season.has(Key.episodes.toString()))
                    {
                        JSONArray jEpisodes = season.getJSONArray(Key.episodes.toString());
                        for(int e = 0; e < jEpisodes.length(); e++)
                        {
                            JSONObject jEpisode = jEpisodes.getJSONObject(e);
                            Episode episode = new Episode();
                            if(jEpisode.has(Key.season.toString()))episode.setSeason(jEpisode.getInt(Key.season.toString()));
                            if(jEpisode.has(Key.episode.toString()))episode.setEpisode(jEpisode.getInt(Key.episode.toString()));
                            if(jEpisode.has(Key.tvdb_id.toString()))episode.setTvdb_id(jEpisode.getInt(Key.tvdb_id.toString()));
                            if(jEpisode.has(Key.title.toString()))episode.setTitle(jEpisode.getString(Key.title.toString()));
                            if(jEpisode.has(Key.overview.toString()))episode.setOverview(jEpisode.getString(Key.overview.toString()));
                            if(jEpisode.has(Key.first_aired.toString()))episode.setFirst_aired(jEpisode.getLong(Key.first_aired.toString()));
                            if(jEpisode.has(Key.url.toString()))episode.setUrl(jEpisode.getString(Key.url.toString()));
                            if(jEpisode.has(Key.screen.toString()))episode.setScreen(jEpisode.getString(Key.screen.toString()));
                            if(jEpisode.has(Key.ratings.toString()))
                            {
                                JSONObject ratings = jEpisode.getJSONObject(Key.ratings.toString());
                                if(ratings.has(Key.percentage.toString()))episode.setRating(ratings.getInt(Key.percentage.toString()));
                                if(ratings.has(Key.loved.toString()))episode.setLoved(ratings.getInt(Key.loved.toString()));
                                if(ratings.has(Key.hated.toString()))episode.setHated(ratings.getInt(Key.hated.toString()));
                            }
                            episode.setShow_id(showId);
                            episodes.add(episode);
                        }
                        EpisodeDao episodeDao = MainApp.getInstance().getDaoSession().getEpisodeDao();
                        episodeDao.insertOrReplaceInTx(episodes);
                    }
                }
            }

        }
        catch (Exception e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
            if(BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
            return new Response().setStatus(false).setError(e.getMessage());
        }
        return new Response().setError(null).setStatus(true);
    }

    public static boolean checkResponse(Internet.Response response)
    {
        return response.isResponseOk();
    }

    public static class Response
    {
        private boolean status;
        private String error;

        public Response setStatus(boolean status)
        {
            this.status = status;
            return this;
        }

        public Response setError(String error)
        {
            this.error = error;
            return this;
        }

        public boolean getStatus()
        {
            return status;
        }

        public String getError()
        {
            return error;
        }
    }
}
