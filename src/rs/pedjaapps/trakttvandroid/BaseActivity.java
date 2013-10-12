package rs.pedjaapps.trakttvandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import rs.pedjaapps.trakttvandroid.utils.DatabaseHandler;

public class BaseActivity extends FragmentActivity
{

    static DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle sis)
	{
		db = DatabaseHandler.getInstance(this);
		db.open();
		super.onCreate(sis);
	}

	@Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
	
}
