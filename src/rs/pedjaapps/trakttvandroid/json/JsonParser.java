/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.pedjaapps.trakttvandroid.json;
import org.json.JSONException;
import org.json.JSONObject;
import rs.pedjaapps.trakttvandroid.model.User;

/**
 *
 * @author pedja
 */
public class JsonParser {

    private enum KEY {

        PROFILE("profile"),
        USERNAME("username"),
        FULL_NAME("full_name"),
        GENDER("gender"),
        AGE("age"),
        LOCATION("location"),
        ABOUT("about"),
        JOINED("joined"),
        LAST_LOGIN("last_login"),
        AVATAR("avatar"),
        URL("url"),
        VIP("vip"),
        
        ACCOUNT("account"),
        TIMEZONE("timezone"),
        USE_24H("use_24h"),
        PROTECTED("protected"),
        
        VIEWING("viewing"),
        RATINGS("ratings"),
        SHOUTS("shouts"),
        MODE("mode"),
        SHOW_BADGES("show_badges"),
        SHOW_SPOILERS("show_spoilers"),
        
        SHARING_TEXT("sharing_text"),
        WATCHING("watching"),
        WATCHED("watched"),
        
        
        CONNECTIONS("connections"),
        FACEBOOK("facebook"),
        TWITTER("twitter"),
        TUMBLR("tumblr"),
        PATH("path"),
        PROWL("prowl"),
        CONNECTED("connected"),
        TIMELINE_ENABLED("timeline_enabled"),
        SHARE_SCROBBLERS_START("share_scrobblers_start"),
        SHARE_SCROBBLERS_END("share_scrobblers_end"),
        SHARE_TV("share_tv"),
        SHARE_MOVIES("share_movies"),
        SHARE_RATINGS("share_ratings"),
        SHARE_CHECKINS("share_checkins");

        private String mValue;

        KEY(String value) {
            mValue = value;
        }

        public String value() {
            return mValue;
        }
    }

    public static User parseUserData(JSONObject feed) throws JSONException 
    {
        User user = User.getInstance();

        JSONObject profile = feed.getJSONObject(KEY.PROFILE.value());
        if(profile.has(KEY.USERNAME.value()))
            user.setUsername(profile.getString(KEY.USERNAME.value()));
        if(profile.has(KEY.FULL_NAME.value()))
            user.setFull_name(profile.getString(KEY.FULL_NAME.value()));
        if(profile.has(KEY.GENDER.value()))
            user.setGender(profile.getString(KEY.GENDER.value()));
        if(profile.has(KEY.AGE.value()))
            user.setAge(profile.getString(KEY.AGE.value()));
        if(profile.has(KEY.LOCATION.value()))
            user.setLocation(profile.getString(KEY.LOCATION.value()));
        if(profile.has(KEY.ABOUT.value()))
            user.setAbout(profile.getString(KEY.ABOUT.value()));
        if(profile.has(KEY.JOINED.value()))
            user.setJoined(profile.getLong(KEY.JOINED.value()));
        if(profile.has(KEY.LAST_LOGIN.value()))
            user.setLast_login(profile.getLong(KEY.LAST_LOGIN.value()));
        if(profile.has(KEY.AVATAR.value()))
            user.setAvatar(profile.getString(KEY.AVATAR.value()));
        if(profile.has(KEY.URL.value()))
            user.setUrl(profile.getString(KEY.URL.value()));
        if(profile.has(KEY.VIP.value()))
            user.setVip(profile.getBoolean(KEY.VIP.value()));
        
        JSONObject account = feed.getJSONObject(KEY.ACCOUNT.value());
        if(account.has(KEY.TIMEZONE.value()))
            user.setTimezone(account.getString(KEY.TIMEZONE.value()));
        if(account.has(KEY.USE_24H.value()))
            user.setUse_24h(account.getBoolean(KEY.USE_24H.value()));
        if(account.has(KEY.PROTECTED.value()))
            user.setmProtected(account.getBoolean(KEY.PROTECTED.value()));
            
        JSONObject viewing = feed.getJSONObject(KEY.VIEWING.value());
        JSONObject ratings = viewing.getJSONObject(KEY.RATINGS.value());
        JSONObject shouts = viewing.getJSONObject(KEY.SHOUTS.value());
        if(ratings.has(KEY.MODE.value()))
            user.setRatings_mode(ratings.getString(KEY.MODE.value()));
        if(shouts.has(KEY.SHOW_BADGES.value()))
            user.setShow_badges(shouts.getBoolean(KEY.SHOW_BADGES.value()));
        if(shouts.has(KEY.SHOW_SPOILERS.value()))
            user.setShow_badges(shouts.getBoolean(KEY.SHOW_SPOILERS.value()));
        
        JSONObject sharing_text = feed.getJSONObject(KEY.SHARING_TEXT.value());
        if(sharing_text.has(KEY.WATCHED.value()))
            user.setWatched(sharing_text.getString(KEY.WATCHED.value()));
        if(sharing_text.has(KEY.WATCHING.value()))
            user.setWatching(sharing_text.getString(KEY.WATCHING.value()));
        
        JSONObject connections = feed.getJSONObject(KEY.CONNECTIONS.value());
        JSONObject facebook = connections.getJSONObject(KEY.FACEBOOK.value());
        JSONObject twitter = connections.getJSONObject(KEY.TWITTER.value());
        JSONObject tumblr = connections.getJSONObject(KEY.TUMBLR.value());
        JSONObject path = connections.getJSONObject(KEY.PATH.value());
        JSONObject prowl = connections.getJSONObject(KEY.PROWL.value());
        
        //facebook
        User.Connection facebookC = new User.Connection();
        if(facebook.has(KEY.CONNECTED.value()))
            facebookC.setConnected(facebook.getBoolean(KEY.CONNECTED.value()));
        if(facebook.has(KEY.TIMELINE_ENABLED.value()))
            facebookC.setTimeline_enabled(facebook.getBoolean(KEY.TIMELINE_ENABLED.value()));
        if(facebook.has(KEY.SHARE_SCROBBLERS_START.value()))
            facebookC.setShare_scrobblers_start(facebook.getBoolean(KEY.SHARE_SCROBBLERS_START.value()));
        if(facebook.has(KEY.SHARE_SCROBBLERS_END.value()))
            facebookC.setShare_scrobblers_end(facebook.getBoolean(KEY.SHARE_SCROBBLERS_END.value()));
        if(facebook.has(KEY.SHARE_TV.value()))
            facebookC.setShare_tv(facebook.getBoolean(KEY.SHARE_TV.value()));
        if(facebook.has(KEY.SHARE_MOVIES.value()))
            facebookC.setShare_movies(facebook.getBoolean(KEY.SHARE_MOVIES.value()));
        if(facebook.has(KEY.SHARE_RATINGS.value()))
            facebookC.setShare_ratings(facebook.getBoolean(KEY.SHARE_RATINGS.value()));
        if(facebook.has(KEY.SHARE_CHECKINS.value()))
            facebookC.setShare_checkins(facebook.getBoolean(KEY.SHARE_CHECKINS.value()));
        user.setFacebook(facebookC);
        
        //twiter
        User.Connection twitterC = new User.Connection();
        if(twitter.has(KEY.CONNECTED.value()))
            twitterC.setConnected(twitter.getBoolean(KEY.CONNECTED.value()));
        if(twitter.has(KEY.SHARE_SCROBBLERS_START.value()))
            twitterC.setShare_scrobblers_start(twitter.getBoolean(KEY.SHARE_SCROBBLERS_START.value()));
        if(twitter.has(KEY.SHARE_SCROBBLERS_END.value()))
            twitterC.setShare_scrobblers_end(twitter.getBoolean(KEY.SHARE_SCROBBLERS_END.value()));
        if(twitter.has(KEY.SHARE_TV.value()))
            twitterC.setShare_tv(twitter.getBoolean(KEY.SHARE_TV.value()));
        if(twitter.has(KEY.SHARE_MOVIES.value()))
            twitterC.setShare_movies(twitter.getBoolean(KEY.SHARE_MOVIES.value()));
        if(twitter.has(KEY.SHARE_RATINGS.value()))
            twitterC.setShare_ratings(twitter.getBoolean(KEY.SHARE_RATINGS.value()));
        if(twitter.has(KEY.SHARE_CHECKINS.value()))
            twitterC.setShare_checkins(twitter.getBoolean(KEY.SHARE_CHECKINS.value()));
        user.setTwitter(twitterC);
        
        //tumblr
        User.Connection tumblrC = new User.Connection();
        if(tumblr.has(KEY.CONNECTED.value()))
            tumblrC.setConnected(tumblr.getBoolean(KEY.CONNECTED.value()));
        if(tumblr.has(KEY.SHARE_SCROBBLERS_START.value()))
            tumblrC.setShare_scrobblers_start(tumblr.getBoolean(KEY.SHARE_SCROBBLERS_START.value()));
        if(tumblr.has(KEY.SHARE_SCROBBLERS_END.value()))
            tumblrC.setShare_scrobblers_end(tumblr.getBoolean(KEY.SHARE_SCROBBLERS_END.value()));
        if(tumblr.has(KEY.SHARE_TV.value()))
            tumblrC.setShare_tv(tumblr.getBoolean(KEY.SHARE_TV.value()));
        if(tumblr.has(KEY.SHARE_MOVIES.value()))
            tumblrC.setShare_movies(tumblr.getBoolean(KEY.SHARE_MOVIES.value()));
        if(tumblr.has(KEY.SHARE_RATINGS.value()))
            tumblrC.setShare_ratings(tumblr.getBoolean(KEY.SHARE_RATINGS.value()));
        if(tumblr.has(KEY.SHARE_CHECKINS.value()))
            tumblrC.setShare_checkins(tumblr.getBoolean(KEY.SHARE_CHECKINS.value()));
        user.setTumblr(tumblrC);
        
        //path
        User.Connection pathC = new User.Connection();
        if(path.has(KEY.CONNECTED.value()))
            pathC.setConnected(path.getBoolean(KEY.CONNECTED.value()));
        user.setPath(pathC);
        
        //prowl
        User.Connection prowlC = new User.Connection();
        if(prowl.has(KEY.CONNECTED.value()))
            prowlC.setConnected(prowl.getBoolean(KEY.CONNECTED.value()));
        user.setProwlr(prowlC);
        return user;
    }
}
