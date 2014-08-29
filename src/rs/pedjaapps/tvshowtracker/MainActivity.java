package rs.pedjaapps.tvshowtracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.adapter.NavigationDrawerAdapter;
import rs.pedjaapps.tvshowtracker.model.NDItem;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;
import rs.pedjaapps.tvshowtracker.fragment.TrendingShowsFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private static final int REQUEST_CODE_LOGIN = 9001;
    
    SearchView searchView;
    TextView tvLoginLogout;
    Menu menu;
    boolean menuDisable;

    //SlidingMenu sideMenu;
    
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
	
	private List<NDItem> menuItems;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        MainApp.getInstance().getDaoSession();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*sideMenu = new SlidingMenu(this);
        sideMenu.setMode(SlidingMenu.LEFT);
        sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sideMenu.setShadowWidthRes(R.dimen.shadow_width);
        sideMenu.setShadowDrawable(R.drawable.menu_shadow);
        sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sideMenu.setFadeDegree(0.35f);
        sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sideMenu.setMenu(R.layout.menu_layout);

        findViewById(R.id.tvMenuCalendar).setOnClickListener(this);
        tvLoginLogout = (TextView) findViewById(R.id.tvMenuLoginLogout);
        tvLoginLogout.setOnClickListener(this);
        findViewById(R.id.tvMenuMissed).setOnClickListener(this);
        findViewById(R.id.tvMenuSettings).setOnClickListener(this);
        findViewById(R.id.tvMenuTrending).setOnClickListener(this);

        setupUser();*/
		
		mTitle = mDrawerTitle = getTitle();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		menuItems = generateMenuOptions();
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new NavigationDrawerAdapter(this, menuItems));
        mDrawerList.setOnItemClickListener(this);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
			this,                  /* host Activity */
			mDrawerLayout,         /* DrawerLayout object */
			R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open,  /* "open drawer" description for accessibility */
			R.string.drawer_closed  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getActionBar().getThemedContext());
        searchView.setQueryHint(getString(R.string.add_show));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        //searchView.setQueryRefinementEnabled(true);

        
    }

    private void setupUser()
    {
        String user = MainApp.getInstance().getActiveUser();
        if(!PrefsManager.defaultUser.equals(user))
        {
            getActionBar().setSubtitle(user);
            tvLoginLogout.setText(R.string.logout);
        }
        else
        {
            tvLoginLogout.setText(R.string.login);
        }
    }

    

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvMenuCalendar:
                break;
            case R.id.tvMenuLoginLogout:
                if(PrefsManager.defaultUser.equals(MainApp.getInstance().getActiveUser()))
                {
                    startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
                }
                else
                {
                    //new ATLogout();
                }
                break;
            case R.id.tvMenuMissed:
                break;
            case R.id.tvMenuSettings:
                break;
            case R.id.tvMenuTrending:
               break;
        }
    }


    

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        System.out.println("onCreateOptionsMenu");
        //this.menu = menu;
        menu.add(0, 0, 0, getString(R.string.add))
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );
		/*menu.add(0, 1, 1, "Agenda").setIcon(R.drawable.ic_action_agenda)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		/*menu.add(0, 2, 2, "Preferences").setIcon(R.drawable.ic_action_settings)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        menu.add(0, 3, 3, getString(R.string.update_all)).setIcon(R.drawable.ic_action_update)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
	
	/*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerContent);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		/*if(mDrawerLayout.isDrawerOpen(drawerContent))
		{
		    mDrawerLayout.closeDrawer(drawerContent);
		}
		/*if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
        }*/
        switch (item.getItemId())
        {
            case 1:
                //startActivity(new Intent(this, AgendaActivity.class).putExtra("profile", profile));
                return true;
            case 2:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case 3:
                /*if (Tools.isNetworkAvailable(this))
                {
                    new UpdateShow().execute(db.getAllShows(prefs.getString("filter", "all"), profile, prefs.getString("sort", "id"), ""));
                }
                else
                {
                    Toast.makeText(
                            MainActivity.this,
                            "No Internet Connection!\nPlease connect to internet and try again!",
                            Toast.LENGTH_LONG).show();
                }*/
                //TODO update all shows
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   

    @Override
    public void onDestroy()
    {
        //Utility.setKeepScreenOn(MainActivity.this, false);
        //db.close();
        super.onDestroy();
    }

	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (menu != null)
		{
			for (int i = 0; i < menu.size(); i++)
			{
				if (menuDisable)
					menu.getItem(i).setEnabled(false);
				else
					menu.getItem(i).setEnabled(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}*/

    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_LOGIN:
                    setupUser();
                    break;
            }
        }
    }
	
	private void selectItem(int position) 
	{
        // update the main content by replacing fragments
        Fragment fragment = null;
		
		NDItem menuItem = menuItems.get(position);
		
		switch(menuItem.id)
		{
			case trending:
				fragment = TrendingShowsFragment.newInstance();
				break;
			case my_shows:
				fragment = MyShowsFragment.newInstance();
				break;
		}

		if(fragment != null)
		{
        	FragmentManager fragmentManager = getSupportFragmentManager();
        	FragmentTransaction ft = fragmentManager.beginTransaction();
        	ft.replace(R.id.content_frame, fragment);
        	ft.commit();
		}

        // update selected item title, then close the drawer
        getActionBar().setSubtitle(menuItem.title);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        selectItem(position);
    }
	
	private List<NDItem> generateMenuOptions()
	{
		List<NDItem> items = new ArrayList<>();
		NDItem item = new NDItem();
		item.title = getString(R.string.trending_shows);
		item.id = NDItem.Id.trending;
		item.type = NDItem.TYPE_MAIN;
		items.add(item);
		
		item = new NDItem();
		item.title = getString(R.string.my_shows);
		item.id = NDItem.Id.my_shows;
		item.type = NDItem.TYPE_MAIN;
		items.add(item);
		
		return items;
	}
	
}
