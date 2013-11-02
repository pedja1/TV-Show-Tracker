package rs.pedjaapps.trakttvandroid;

import android.os.*;
import android.support.v4.app.*;
import android.widget.*;
import com.j256.ormlite.android.apptools.*;
import com.jeremyfeinstein.slidingmenu.lib.*;
import java.text.*;
import java.util.*;
import rs.pedjaapps.trakttvandroid.model.*;
import rs.pedjaapps.trakttvandroid.utils.*;

public class BaseActivity extends FragmentActivity {

    SlidingMenu sideMenu;
	DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle sis) 
	{
        super.onCreate(sis);
    }
	@Override
    protected void onDestroy()
    {
        super.onDestroy();

		/*
         * You'll need this in your class to release the helper when done.
		 */
        if(databaseHelper != null)
        {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getHelper()
    {
        if(databaseHelper == null)
        {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void closeDrawer() 
	{
        sideMenu.toggle(true);
    }
	
	protected void setupMenu()
	{
		sideMenu = new SlidingMenu(this);
        sideMenu.setMode(SlidingMenu.LEFT);
        sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sideMenu.setShadowWidthRes(R.dimen.shadow_width);
        sideMenu.setShadowDrawable(R.drawable.menu_shadow);
        sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sideMenu.setFadeDegree(0.35f);
        sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sideMenu.setMenu(R.layout.menu_layout);
		
		User user = User.getInstance();
		
		TextView fullName = (TextView)findViewById(R.id.menu_full_name);
		fullName.setText(user.getFull_name());
		
		TextView lastLogin = (TextView)findViewById(R.id.menu_last_login);
		Date lastLoginDate = new Date();
		lastLoginDate.setTime(user.getLast_login());
		SimpleDateFormat lastLoginFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
		lastLogin.setText(lastLoginFormat.format(lastLoginDate));
	}

}
