package rs.pedjaapps.tvshowtracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity
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
