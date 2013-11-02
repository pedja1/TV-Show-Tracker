/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rs.pedjaapps.trakttvandroid.model;

import com.j256.ormlite.table.*;
import org.json.*;
import rs.pedjaapps.trakttvandroid.json.*;
import com.j256.ormlite.field.*;

/**
 *
 * @author pedja
 */
@DatabaseTable(tableName = "user")
public class User 
{
    private static User user = null;
    
    /*profile*/
    @DatabaseField(id = true) private String username;
    @DatabaseField private String full_name;
    @DatabaseField private String gender; //TODO this can be enum
    @DatabaseField private String age;
    @DatabaseField private String location;
    @DatabaseField private String about;
    @DatabaseField private long joined;
    @DatabaseField private long last_login;
    @DatabaseField private String avatar;
    @DatabaseField private String url;
    @DatabaseField private boolean vip;
    
    /*account*/
    @DatabaseField private String timezone;
    @DatabaseField private boolean use_24h;
    @DatabaseField private boolean mProtected;
    
    @DatabaseField private String ratings_mode;//TODO can be enum
    
    @DatabaseField private boolean show_badges;
    @DatabaseField private boolean show_spoilers;
    
    /*connections*/
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) private Connection facebook;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) private Connection twitter;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) private Connection tumblr;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) private Connection path;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) private Connection prowlr;
    
    /*sharing texts*/
    @DatabaseField private String watching;
    @DatabaseField private String watched;
    
    public static synchronized User getInstance()
    {
        if(user == null)
        {
            user = new User();
        }
        
        return user;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public long getJoined() {
        return joined;
    }

    public void setJoined(long joined) {
        this.joined = joined;
    }

    public long getLast_login() {
        return last_login;
    }

    public void setLast_login(long last_login) {
        this.last_login = last_login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isUse_24h() {
        return use_24h;
    }

    public void setUse_24h(boolean use_24h) {
        this.use_24h = use_24h;
    }

    public boolean ismProtected() {
        return mProtected;
    }

    public void setmProtected(boolean mProtected) {
        this.mProtected = mProtected;
    }

    public String getRatings_mode() {
        return ratings_mode;
    }

    public void setRatings_mode(String ratings_mode) {
        this.ratings_mode = ratings_mode;
    }

    public boolean isShow_badges() {
        return show_badges;
    }

    public void setShow_badges(boolean show_badges) {
        this.show_badges = show_badges;
    }

    public boolean isShow_spoilers() {
        return show_spoilers;
    }

    public void setShow_spoilers(boolean show_spoilers) {
        this.show_spoilers = show_spoilers;
    }

    public Connection getFacebook() {
        return facebook;
    }

    public void setFacebook(Connection facebook) {
        this.facebook = facebook;
    }

    public Connection getTwitter() {
        return twitter;
    }

    public void setTwitter(Connection twitter) {
        this.twitter = twitter;
    }

    public Connection getTumblr() {
        return tumblr;
    }

    public void setTumblr(Connection thumblr) {
        this.tumblr = thumblr;
    }

    public Connection getPath() {
        return path;
    }

    public void setPath(Connection path) {
        this.path = path;
    }

    public Connection getProwlr() {
        return prowlr;
    }

    public void setProwlr(Connection prowlr) {
        this.prowlr = prowlr;
    }

    public String getWatching() {
        return watching;
    }

    public void setWatching(String watching) {
        this.watching = watching;
    }

    public String getWatched() {
        return watched;
    }

    public void setWatched(String watched) {
        this.watched = watched;
    }
    
    public void setUserFromJson(JSONObject json) throws JSONException
    {
        user = JsonParser.parseUserData(json);
    }
    
    public static class Connection
    {
        private boolean connected;
        private boolean timeline_enabled;
        private boolean share_scrobblers_start;
        private boolean share_scrobblers_end;
        private boolean share_tv;
        private boolean share_movies;
        private boolean share_ratings;
        private boolean share_checkins;

        public Connection() 
        {
            
        }

        public boolean isConnected() {
            return connected;
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public boolean isTimeline_enabled() {
            return timeline_enabled;
        }

        public void setTimeline_enabled(boolean timeline_enabled) {
            this.timeline_enabled = timeline_enabled;
        }

        public boolean isShare_scrobblers_start() {
            return share_scrobblers_start;
        }

        public void setShare_scrobblers_start(boolean share_scrobblers_start) {
            this.share_scrobblers_start = share_scrobblers_start;
        }

        public boolean isShare_scrobblers_end() {
            return share_scrobblers_end;
        }

        public void setShare_scrobblers_end(boolean share_scrobblers_end) {
            this.share_scrobblers_end = share_scrobblers_end;
        }

        public boolean isShare_tv() {
            return share_tv;
        }

        public void setShare_tv(boolean share_tv) {
            this.share_tv = share_tv;
        }

        public boolean isShare_movies() {
            return share_movies;
        }

        public void setShare_movies(boolean share_movies) {
            this.share_movies = share_movies;
        }

        public boolean isShare_ratings() {
            return share_ratings;
        }

        public void setShare_ratings(boolean share_ratings) {
            this.share_ratings = share_ratings;
        }

        public boolean isShare_checkins() {
            return share_checkins;
        }

        public void setShare_checkins(boolean share_checkins) {
            this.share_checkins = share_checkins;
        }

    }
    
}
