package rs.pedjaapps.tvshowtracker.utils;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import java.util.List;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;

public class NetSearchSuggestionProvider extends ContentProvider
{

	@Override
	public boolean onCreate()
	{
		System.out.println("nssp init");
		return true;
	}

	@Override
	public Cursor query(Uri p1, String[] p2, String p3, String[] p4, String p5)
	{
		System.out.println("nssp query");
		String[] columns = {"_id", SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2};
		
		MatrixCursor cursor = new MatrixCursor(columns);

		List<Show> shows = null;//JSONUtility.parseSearchResults(p1.getLastPathSegment());
		System.out.println("shows: " + shows.size());
		
		for (Show show : shows)
		{
			String[] tmp = {show.getTvdb_id() + "", R.drawable.no_image + "", show.getTitle() + " (" + show.getYear() + ")", show.getNetwork()};
			cursor.addRow(tmp);
		}
		return cursor;
	}

	@Override
	public String getType(Uri p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Uri insert(Uri p1, ContentValues p2)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public int delete(Uri p1, String p2, String[] p3)
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public int update(Uri p1, ContentValues p2, String p3, String[] p4)
	{
		// TODO: Implement this method
		return 0;
	}
	
}
