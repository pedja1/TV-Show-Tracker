package rs.pedjaapps.tvshowtracker;

import android.content.*;

public class SuggestionProvider extends SearchRecentSuggestionsProvider
 {
    public final static String AUTHORITY = "rs.pedjaapps.tvshowtracker.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
