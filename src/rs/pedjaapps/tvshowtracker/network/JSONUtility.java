package rs.pedjaapps.tvshowtracker.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.BuildConfig;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.Genre;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.Image;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.model.ShowNoDao;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.utils.ShowMemCache;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by pedja on 1/22/14.
 * Class responsible for handling all parsing of server responses in JSON format
 */
public class JSONUtility
{
    public enum Key
    {
        title, first_aired_utc, country, overview, banner, air_day, air_time, images, network, year, url,
        runtime, status, fanart, poster, certification, imdb_id, tvdb_id, tvrage_id, last_updated, ratings,
        percentage, loved, hated, actors, people, name, character, headshot, genres, seasons, episodes,
        season, episode, screen, error, error_message, error_code, id, email, first_name, last_name, avatar,
        password, message
		}

    public enum RequestKey
    {
        query
		}

    public static Response parseLoginResponse(PostParams params)
    {
        Internet.Response response = Internet.getInstance().httpPost(Constants.REQUEST_URL_LOGIN, params);
        if (!checkResponse(response))
        {
            Response response1 = new Response();
            response1.status = false;
            response1.errorMessage = response.responseMessage;
            return response1;
        }
        try
        {
            JSONObject jsonObject = new JSONObject(response.responseData);
            if (jsonObject.has(Key.status.toString()) && jsonObject.getInt(Key.status.toString()) == 1)
            {
                String email = jsonObject.getString(Key.email.toString());
                PrefsManager.setActiveUser(email);
                MainApp.getInstance().setActiveUser(email);
            }
            else
            {
                return new Response()
					//.setErrorCode(jsonObject.getString(Key.error_code.toString()))
					.setErrorMessage(jsonObject.getString(Key.error_message.toString()))
					.setStatus(false);
            }
        }
        catch (Exception e)
        {
            if (BuildConfig.DEBUG)e.printStackTrace();
            if (BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
            return new Response().setStatus(false).setErrorMessage(e.getMessage()).setErrorCode(Response.ErrorCode.internal);
        }
        return new Response().setErrorMessage(null).setErrorCode(null).setStatus(true);
    }

    public static List<Show> parseSearchResults(String query)
    {
        List<Show> shows = new ArrayList<Show>();
        query = URLEncoder.encode(query);
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQUEST_URL_SEARCH + "?" + RequestKey.query.toString() + "=" + query);
        if (!checkResponse(response))return null;
        try
        {
            JSONArray jsonArray = new JSONArray(response.responseData);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                ShowNoDao show = new ShowNoDao();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));
                if (jsonObject.has(Key.title.toString()))show.setTitle(jsonObject.getString(Key.title.toString()));
                if (jsonObject.has(Key.first_aired_utc.toString()))show.setFirst_aired(jsonObject.getLong(Key.first_aired_utc.toString()));
                if (jsonObject.has(Key.country.toString()))show.setCountry(jsonObject.getString(Key.country.toString()));
                if (jsonObject.has(Key.overview.toString()))show.setOverview(jsonObject.getString(Key.overview.toString()));
                if (jsonObject.has(Key.network.toString()))show.setNetwork(jsonObject.getString(Key.network.toString()));
                if (jsonObject.has(Key.air_day.toString()))show.setAir_day(jsonObject.getString(Key.air_day.toString()));
                if (jsonObject.has(Key.air_time.toString()))show.setAir_time(jsonObject.getString(Key.air_time.toString()));
                if (jsonObject.has(Key.year.toString()))show.setYear(jsonObject.getInt(Key.year.toString()));
                if (jsonObject.has(Key.images.toString()))
                {
                    JSONObject images = jsonObject.getJSONObject(Key.images.toString());
					Image image = new Image();
                    if (images.has(Key.banner.toString()))
                    {
                        image.setBanner(images.getString(Key.banner.toString()));
                    }
					if (images.has(Key.poster.toString()))
                    {
                        image.setPoster(images.getString(Key.poster.toString()));
                    }
					show.setImage(image);
                }
                shows.add(show);
            }
        }
        catch (Exception e)
        {
            shows = null;
            if (BuildConfig.DEBUG)e.printStackTrace();
            if (BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
        }
        return shows;
    }

	public static Response parseTrendingShows()
    {
        List<Show> shows = new ArrayList<Show>();
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQUEST_URL_TRENDING);
		System.out.println(response.responseMessage);
        if (!checkResponse(response))
			return new Response()
				.setStatus(false)
				.setErrorCode(Response.ErrorCode.internet);
        try
        {
            JSONArray jsonArray = new JSONArray(response.responseData);
			ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Show show = new ShowNoDao();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));
				QueryBuilder<Show> builder = showDao.queryBuilder();
				builder.where(ShowDao.Properties.Tvdb_id.eq(show.getTvdb_id()));
				Show tmp = builder.unique();
                if (tmp == null)
				{
					if (jsonObject.has(Key.title.toString()))show.setTitle(jsonObject.getString(Key.title.toString()));
					if (jsonObject.has(Key.images.toString()))
					{
						JSONObject images = jsonObject.getJSONObject(Key.images.toString());
						Image image = new Image();
						if (images.has(Key.poster.toString()))
						{
							image.setPoster(images.getString(Key.poster.toString()));
						}
						show.setImage(image);
					}
				}
				else
				{
					show = tmp;
				}
                shows.add(show);
            }
        }
        catch (Exception e)
        {
            if (BuildConfig.DEBUG)e.printStackTrace();
            if (BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
			return new Response()
				.setStatus(false)
				.setErrorCode(Response.ErrorCode.internet);
        }
        return new Response().setStatus(true).setResponseObject(shows);
    }

    public static Response parseShow(String tvdbId, boolean insertInDb)
    {
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQUEST_URL_GET_SHOW_INFO + "/" + tvdbId + "/1");
        if (!checkResponse(response))
        {
            Response response1 = new Response();
            response1.status = false;
            response1.errorMessage = response.responseMessage;
            return response1;
        }
        try
        {
            JSONObject jsonObject = new JSONObject(response.responseData);
            if (jsonObject.has(Key.error.toString()))
            {
                return new Response().setErrorMessage(jsonObject.getString(Key.error.toString())).setStatus(false);
            }
            Show show = insertInDb ? new Show() : new ShowNoDao();
            List<Episode> episodes = new ArrayList<Episode>();
            List<Actor> actors = new ArrayList<Actor>();
            List<Genre> genres = new ArrayList<Genre>();
            if (jsonObject.has(Key.title.toString()))show.setTitle(jsonObject.getString(Key.title.toString()));
            if (jsonObject.has(Key.year.toString()))show.setYear(jsonObject.getInt(Key.year.toString()));
            if (jsonObject.has(Key.url.toString()))show.setUrl(jsonObject.getString(Key.url.toString()));
            if (jsonObject.has(Key.first_aired_utc.toString()))show.setFirst_aired(jsonObject.getLong(Key.first_aired_utc.toString()));
            if (jsonObject.has(Key.country.toString()))show.setCountry(jsonObject.getString(Key.country.toString()));
            if (jsonObject.has(Key.overview.toString()))show.setOverview(jsonObject.getString(Key.overview.toString()));
            if (jsonObject.has(Key.runtime.toString()))show.setRuntime(jsonObject.getInt(Key.runtime.toString()));
            if (jsonObject.has(Key.status.toString()))show.setStatus(jsonObject.getString(Key.status.toString()));
            if (jsonObject.has(Key.network.toString()))show.setNetwork(jsonObject.getString(Key.network.toString()));
            if (jsonObject.has(Key.air_day.toString()))show.setAir_day(jsonObject.getString(Key.air_day.toString()));
            if (jsonObject.has(Key.air_time.toString()))show.setAir_time(jsonObject.getString(Key.air_time.toString()));
            if (jsonObject.has(Key.certification.toString()))show.setCertification(jsonObject.getString(Key.certification.toString()));
            if (jsonObject.has(Key.imdb_id.toString()))show.setImdb_id(jsonObject.getString(Key.imdb_id.toString()));
            if (jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));
            if (jsonObject.has(Key.tvrage_id.toString()))show.setTvrage_id(jsonObject.getInt(Key.tvrage_id.toString()));
            if (jsonObject.has(Key.last_updated.toString()))show.setLast_updated(jsonObject.getLong(Key.last_updated.toString()));
            if (jsonObject.has(Key.images.toString()))
            {
                JSONObject images = jsonObject.getJSONObject(Key.images.toString());
                Image image = new Image();
                if (images.has(Key.banner.toString()))image.setBanner(images.getString(Key.banner.toString()));
                if (images.has(Key.fanart.toString()))image.setFanart(images.getString(Key.fanart.toString()));
                if (images.has(Key.poster.toString()))image.setPoster(images.getString(Key.poster.toString()));
                show.setImage(image);
                long imageId;
                if (insertInDb)
                {
                    ImageDao imageDao = MainApp.getInstance().getDaoSession().getImageDao();
                    imageId = imageDao.insert(image);
                    show.setImage_id(imageId);
                }
                else
                {
                    show.setImage(image);
                }
            }
            if (jsonObject.has(Key.ratings.toString()))
            {
                JSONObject ratings = jsonObject.getJSONObject(Key.ratings.toString());
                if (ratings.has(Key.percentage.toString()))show.setRating(ratings.getInt(Key.percentage.toString()));
                if (ratings.has(Key.loved.toString()))show.setLoved(ratings.getInt(Key.loved.toString()));
                if (ratings.has(Key.hated.toString()))show.setHated(ratings.getInt(Key.hated.toString()));
            }
            long showId = 0;
            if (insertInDb)
            {
                ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
                showId = showDao.insertOrReplace(show);
            }
            if (jsonObject.has(Key.people.toString()))
            {
                JSONObject people = jsonObject.getJSONObject(Key.people.toString());
                if (people.has(Key.actors.toString()))
                {
                    JSONArray jActors = people.getJSONArray(Key.actors.toString());
                    for (int a = 0; a < jActors.length(); a++)
                    {
                        JSONObject jActor = jActors.getJSONObject(a);
                        Actor actor = new Actor();
                        if (jActor.has(Key.name.toString()))actor.setName(jActor.getString(Key.name.toString()));
                        if (jActor.has(Key.character.toString()))actor.setCharacter(jActor.getString(Key.character.toString()));
                        if (jActor.has(Key.images.toString()))
                        {
                            JSONObject images = jActor.getJSONObject(Key.images.toString());
                            if (images.has(Key.headshot.toString()))actor.setImage(images.getString(Key.headshot.toString()));
                        }
                        actor.setShow_id(showId);
                        actors.add(actor);
                    }
                    if (insertInDb)
                    {
                        ActorDao actorDao = MainApp.getInstance().getDaoSession().getActorDao();
                        actorDao.insertOrReplaceInTx(actors);
                    }
                    else
                    {
                        ((ShowNoDao)show).setActors(actors);
                    }
                }
            }
            if (jsonObject.has(Key.genres.toString()))
            {
                JSONArray jGenres = jsonObject.getJSONArray(Key.genres.toString());
                for (int g = 0; g < jGenres.length(); g++)
                {
                    Genre genre = new Genre();
                    genre.setShow_id(showId);
                    genre.setName(jGenres.getString(g));
                    genres.add(genre);
                }
                if (insertInDb)
                {
                    GenreDao genreDao = MainApp.getInstance().getDaoSession().getGenreDao();
                    genreDao.insertOrReplaceInTx(genres);
                }
                else
                {
                    ((ShowNoDao)show).setGenres(genres);
                }
            }
            if (jsonObject.has(Key.seasons.toString()))
            {
                JSONArray seasons = jsonObject.getJSONArray(Key.seasons.toString());
                for (int s = 0; s < seasons.length(); s++)
                {
                    JSONObject season = seasons.getJSONObject(s);
                    if (season.has(Key.episodes.toString()))
                    {
                        JSONArray jEpisodes = season.getJSONArray(Key.episodes.toString());
                        for (int e = 0; e < jEpisodes.length(); e++)
                        {
                            JSONObject jEpisode = jEpisodes.getJSONObject(e);
                            Episode episode = new Episode();
                            if (jEpisode.has(Key.season.toString()))episode.setSeason(jEpisode.getInt(Key.season.toString()));
                            if (jEpisode.has(Key.episode.toString()))episode.setEpisode(jEpisode.getInt(Key.episode.toString()));
                            if (jEpisode.has(Key.tvdb_id.toString()))episode.setTvdb_id(jEpisode.getInt(Key.tvdb_id.toString()));
                            if (jEpisode.has(Key.title.toString()))episode.setTitle(jEpisode.getString(Key.title.toString()));
                            if (jEpisode.has(Key.overview.toString()))episode.setOverview(jEpisode.getString(Key.overview.toString()));
                            if (jEpisode.has(Key.first_aired_utc.toString()))episode.setFirst_aired(jEpisode.getLong(Key.first_aired_utc.toString()));
                            if (jEpisode.has(Key.url.toString()))episode.setUrl(jEpisode.getString(Key.url.toString()));
                            if (jEpisode.has(Key.screen.toString()))episode.setScreen(jEpisode.getString(Key.screen.toString()));
                            if (jEpisode.has(Key.ratings.toString()))
                            {
                                JSONObject ratings = jEpisode.getJSONObject(Key.ratings.toString());
                                if (ratings.has(Key.percentage.toString()))episode.setRating(ratings.getInt(Key.percentage.toString()));
                                if (ratings.has(Key.loved.toString()))episode.setLoved(ratings.getInt(Key.loved.toString()));
                                if (ratings.has(Key.hated.toString()))episode.setHated(ratings.getInt(Key.hated.toString()));
                            }
                            episode.setShow_id(showId);
                            episodes.add(episode);
                        }
                        if (insertInDb)
                        {
                            EpisodeDao episodeDao = MainApp.getInstance().getDaoSession().getEpisodeDao();
                            episodeDao.insertOrReplaceInTx(episodes);
                        }
                        else
                        {
                            ((ShowNoDao)show).setEpisodes(episodes);
                        }
                    }
                }
            }
            return new Response().setErrorMessage(null).setStatus(true).setResponseObject((ShowNoDao) show);
        }
        catch (Exception e)
        {
            if (BuildConfig.DEBUG)e.printStackTrace();
            if (BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
            return new Response().setStatus(false).setErrorMessage(e.getMessage());
        }
    }

    public static boolean checkResponse(Internet.Response response)
    {
        return response.isResponseOk();
    }

    public static class Response
    {
		public enum ErrorCode
		{
			internet, database, internal
		}
		
        private boolean status;
        private String errorMessage;
		private ErrorCode errorCode;
        private Object responseObject;

        public Response setStatus(boolean status)
        {
            this.status = status;
            return this;
        }

        public Response setErrorMessage(String errorMessage)
        {
            this.errorMessage = errorMessage;
            return this;
        }

        public Response setErrorCode(ErrorCode errorCode)
        {
            this.errorCode = errorCode;
            return this;
        }

        public boolean getStatus()
        {
            return status;
        }

        public String getErrorMessage()
        {
            return errorMessage;
        }

        public ErrorCode getErrorCode()
        {
            return errorCode;
        }

        public ShowNoDao getShow()
        {
            return responseObject instanceof ShowNoDao ? (ShowNoDao)responseObject : null;
        }
		
		/**
		* Make sure responseObject is list since it cant be checked in runtime
		*/
		public List<Show> getShowList()
		{
			return (List<Show>)responseObject;
		}

        public Response setResponseObject(Object responseObject)
        {
            this.responseObject = responseObject;
            return this;
        }
    }
}
