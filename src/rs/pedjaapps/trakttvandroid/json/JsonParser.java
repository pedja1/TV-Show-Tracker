/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.pedjaapps.trakttvandroid.json;

import java.util.logging.Level;
import java.util.logging.Logger;
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
        FULL_NAME("full_name"),;

        private String mValue;

        KEY(String value) {
            mValue = value;
        }

        public String value() {
            return mValue;
        }
    }

    public static User parseUserData(JSONObject feed) throws JSONException {
        User user = User.getInstance();

        JSONObject profile = feed.getJSONObject(KEY.PROFILE.value());
        if(profile.has(KEY.USERNAME.value()))
            user.setUsername(profile.getString(KEY.USERNAME.value()));
        if(profile.has(KEY.FULL_NAME.value()))
            user.setFull_name(profile.getString(KEY.FULL_NAME.value()));
            

        return user;
    }
}
