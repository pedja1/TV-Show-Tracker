package rs.pedjaapps.tvshowtracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends ActionBarActivity
{
	
	@Override
	protected void onCreate(Bundle sis)
	{
		super.onCreate(sis);
	}

	protected void onDestroy()
	{
		//db.close();
		super.onDestroy();
	}
	
}
