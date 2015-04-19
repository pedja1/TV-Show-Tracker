package rs.pedjaapps.tvshowtracker.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.BuildConfig;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Image;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.User;
import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;

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
        password, message, success, profile, username, full_name, gender, age, location, about, joined,
        last_login, sharing_text, watched, watching
	}

    public enum RequestKey
    {
        query
		}

    public static Response parseLoginResponse(PostParams params)
    {
        Internet.Response response = Internet.getInstance().httpPost(Constants.REQUEST_URL_LOGIN, params);
        try
        {
            JSONObject jsonObject = new JSONObject(response.responseData);
            if (Key.success.toString().equals(jsonObject.optString(Key.status.toString())))
            {
                User user = new User();
                JSONObject jProfile = jsonObject.optJSONObject(Key.profile.toString());
                user.setUsername(jProfile.optString(Key.username.toString()));
                user.setPassword(params.getPostParameters().get(1).getValue());
                user.setFull_name(jProfile.optString(Key.full_name.toString()));
                user.setGender(jProfile.optString(Key.gender.toString()));
                user.setLocation(jProfile.optString(Key.location.toString()));
                user.setAge(jProfile.optInt(Key.age.toString()));
                user.setAbout(jProfile.optString(Key.about.toString()));
                user.setJoined(jProfile.optLong(Key.joined.toString()));
                user.setLast_login(jProfile.optLong(Key.last_login.toString()));
                user.setAvatar(jProfile.optString(Key.avatar.toString()));
                user.setUrl(jProfile.optString(Key.url.toString()));

                JSONObject jShare = jsonObject.optJSONObject(Key.sharing_text.toString());
                if(jShare != null)
                {
                    user.setShare_text_watched(jShare.optString(Key.watched.toString()));
                    user.setShare_text_watching(jShare.optString(Key.watching.toString()));
                }

                UserDao userDao = MainApp.getInstance().getDaoSession().getUserDao();
                userDao.insertOrReplace(user);

                PrefsManager.setActiveUser(user.getUsername());
                MainApp.getInstance().setActiveUser(user);
            }
            else
            {
                return new Response()
					.setErrorCode(Response.ErrorCode.fromString(jsonObject.optString(Key.status.toString())))
					.setErrorMessage(jsonObject.optString(Key.error.toString()))
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

    public static Response addToLibrary(Show show)
    {
        JSONObject reuestJson = new JSONObject();
        try
        {
            reuestJson.put(Key.username.toString(), MainApp.getInstance().getActiveUser().getUsername());
            reuestJson.put(Key.password.toString(), MainApp.getInstance().getActiveUser().getPassword());
            reuestJson.put(Key.imdb_id.toString(), show.getImdb_id());
            reuestJson.put(Key.tvdb_id.toString(), show.getTvdb_id());
            reuestJson.put(Key.tvdb_id.toString(), show.getTvdb_id());
            reuestJson.put(Key.title.toString(), show.getTitle());
            reuestJson.put(Key.year.toString(), show.getYear());
            JSONArray episodes = new JSONArray();
            for(Episode episode : show.getEpisodes())
            {
                if(episode.isWatched())
                {
                    JSONObject jEpisode = new JSONObject();
                    jEpisode.put(Key.season.toString(), episode.getSeason());
                    jEpisode.put(Key.episode.toString(), episode.getEpisode());
                    episodes.put(jEpisode);
                }
            }
            reuestJson.put(Key.episodes.toString(), episodes);
        }
        catch (JSONException e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
        }

        Internet.Response response = Internet.getInstance().httpPost(Constants.REQUEST_URL_ADD_TO_LIBRARY, reuestJson.toString());
        try
        {
            JSONObject jsonObject = new JSONObject(response.responseData);
            if (!Key.success.toString().equals(jsonObject.optString(Key.status.toString())))
            {
                return new Response()
                        .setErrorCode(Response.ErrorCode.fromString(jsonObject.optString(Key.status.toString())))
                        .setErrorMessage(jsonObject.optString(Key.error.toString()))
                        .setStatus(false);
            }
            else
            {
                return new Response()
                        .setErrorCode(Response.ErrorCode.fromString(jsonObject.optString(Key.status.toString())))
                        .setErrorMessage(jsonObject.optString(Key.message.toString()))
                        .setStatus(true);
            }
        }
        catch (Exception e)
        {
            if (BuildConfig.DEBUG)e.printStackTrace();
            if (BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
            Crashlytics.logException(e);
            return new Response().setStatus(false).setErrorMessage(e.getMessage()).setErrorCode(Response.ErrorCode.internal);
        }
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
                Show show = new Show();
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
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Show show = new Show();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(Key.tvdb_id.toString()))show.setTvdb_id(jsonObject.getInt(Key.tvdb_id.toString()));

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

    public static Response parseCollectionResponse()
    {
        List<Show> shows = new ArrayList<Show>();
        Internet.Response response = Internet.getInstance().httpGet(Constants.REQUEST_URL_COLLECTION + MainApp.getInstance().getActiveUser().getUsername() + "/min");
        if (!checkResponse(response))
            return new Response()
                    .setStatus(false)
                    .setErrorCode(Response.ErrorCode.internet);
        try
        {
            JSONArray jsonArray = new JSONArray(response.responseData);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Show show = new Show();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                show.setTvdb_id(jsonObject.optInt(Key.tvdb_id.toString()));
                show.setTitle(jsonObject.getString(Key.title.toString()));
                List<Episode> episodes = new ArrayList<>();
                if (jsonObject.has(Key.seasons.toString()))
                {
                    JSONArray seasons = jsonObject.getJSONArray(Key.seasons.toString());
                    for (int s = 0; s < seasons.length(); s++)
                    {
                        JSONObject seasonObjec = seasons.getJSONObject(s);
                        int season = seasonObjec.optInt(Key.season.toString());
                        if (seasonObjec.has(Key.episodes.toString()))
                        {
                            JSONArray jEpisodes = seasonObjec.getJSONArray(Key.episodes.toString());
                            for (int e = 0; e < jEpisodes.length(); e++)
                            {
                                int jEpisode = jEpisodes.optInt(e);
                                Episode episode = new Episode();
                                episode.setSeason(season);
                                episode.setEpisode(jEpisode);
                                episodes.add(episode);
                            }
                        }
                    }
                }
                show.setEpisodes(episodes);
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


    public static Response parseShow(String tvdbId)
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
            Show show = new Show();
            List<Episode> episodes = new ArrayList<Episode>();
            List<Actor> actors = new ArrayList<Actor>();
            List<String> genres = new ArrayList<String>();
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
            }
            if (jsonObject.has(Key.ratings.toString()))
            {
                JSONObject ratings = jsonObject.getJSONObject(Key.ratings.toString());
                if (ratings.has(Key.percentage.toString()))show.setRating(ratings.getInt(Key.percentage.toString()));
                if (ratings.has(Key.loved.toString()))show.setLoved(ratings.getInt(Key.loved.toString()));
                if (ratings.has(Key.hated.toString()))show.setHated(ratings.getInt(Key.hated.toString()));
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
                        actors.add(actor);
                    }
                    show.setActors(actors);
                }
            }
            if (jsonObject.has(Key.genres.toString()))
            {
                JSONArray jGenres = jsonObject.getJSONArray(Key.genres.toString());
                for (int g = 0; g < jGenres.length(); g++)
                {
                    genres.add(jGenres.getString(g));
                }
                show.setGenres(genres);
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
                            episodes.add(episode);
                        }
                        show.setEpisodes(episodes);
                    }
                }
            }
            return new Response().setErrorMessage(null).setStatus(true).setResponseObject(show);
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
			success, unknown, internet, database, internal;

            public static ErrorCode fromString(String value)
            {
                for(ErrorCode code : values())
                {
                    if(code.toString().equals(value))
                    {
                        return code;
                    }
                }
                return unknown;
            }
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

        public Show getShow()
        {
            return (Show) responseObject;
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
