package rs.pedjaapps.tvshowtracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.adapter.NavigationDrawerAdapter;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;
import rs.pedjaapps.tvshowtracker.fragment.TrendingShowsFragment;
import rs.pedjaapps.tvshowtracker.model.NDItem;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private static final int REQUEST_CODE_LOGIN = 9001;
    
    SearchView searchView;
    TextView tvLoginLogout;
    Menu menu;
    boolean menuDisable;

    //SlidingMenu sideMenu;
    
	private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerContent;
    private ListView lvDrawer;
    NavigationDrawerAdapter ndAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
	
	private List<NDItem> menuItems;

    private int currentItem = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if(PrefsManager.isFirstRun())
        {
            startActivity(new Intent(this, LoginActivity.class));
            PrefsManager.setFirstRun(false);
        }
        MainApp.getInstance().getDaoSession();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = (LinearLayout) findViewById(R.id.left_drawer);
        lvDrawer = (ListView) findViewById(R.id.lvDrawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //tvLoginLogout = (TextView)findViewById(R.id.tvMenuLoginLogout);
        //tvLoginLogout.setOnClickListener(this);

		menuItems = generateMenuOptions();
        // set up the drawer's list view with items and click listener
        ndAdapter = new NavigationDrawerAdapter(this, menuItems);
        lvDrawer.setAdapter(ndAdapter);
        lvDrawer.setOnItemClickListener(this);




        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
			this,                  /* host Activity */
			mDrawerLayout,         /* DrawerLayout object */
			R.string.drawer_open,  /* "open drawer" description for accessibility */
			R.string.drawer_closed  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint(getString(R.string.add_show));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        //searchView.setQueryRefinementEnabled(true);
    }

    private void setupUser()
    {
        String user = MainApp.getInstance().getActiveUser().getUsername();
        if(!PrefsManager.defaultUser.equals(user))
        {
            //getSupportActionBar().setSubtitle(user);
            //tvLoginLogout.setText(R.string.logout);
        }
        else
        {
            //tvLoginLogout.setText(R.string.login);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        System.out.println("onCreateOptionsMenu");
        //this.menu = menu;
        /*MenuItem item = menu.add(0, 0, 0, getString(R.string.add))
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView);
        MenuItemCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item = menu.add(0, 3, 3, getString(R.string.update_all)).setIcon(R.drawable.ic_action_sync);
        MenuItemCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerContent);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		/*if(mDrawerLayout.isDrawerOpen(drawerContent))
		{
		    mDrawerLayout.closeDrawer(drawerContent);
		}*/
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
        }
        /*switch (item.getItemId())
        {
            case 1:
                //startActivity(new Intent(this, AgendaActivity.class).putExtra("profile", profile));
                return true;
            case 2:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case 3:
                if (Tools.isNetworkAvailable(this))
                {
                    new UpdateShow().execute(db.getAllShows(prefs.getString("filter", "all"), profile, prefs.getString("sort", "id"), ""));
                }
                else
                {
                    Toast.makeText(
                            MainActivity.this,
                            "No Internet Connection!\nPlease connect to internet and try again!",
                            Toast.LENGTH_LONG).show();
                }
                //TODO update all shows
                return true;
        }*/
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

   /* private void askToSync()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.sync_with_trakt));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //todo start sync service
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }*/

    private void selectItem(int position)
	{
        // update the main content by replacing fragments
        lvDrawer.setItemChecked(position, true);
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
        getSupportActionBar().setSubtitle(menuItem.title);
        mDrawerLayout.closeDrawer(mDrawerContent);
        currentItem = position;
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
        NDItem item = ndAdapter.getItem(position);
        if(item.type == NDItem.TYPE_MAIN)
        {
            selectItem(position);
        }
        else
        {
            switch (item.id)
            {
                case login_logout:
                    //if(PrefsManager.defaultUser.equals(MainApp.getInstance().getActiveUser().getUsername()))
                    //{
                        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
                    //}
                    //else
                    //{
                        //new ATLogout();
                    //}
                    break;
                case settings:
                    break;
            }
            lvDrawer.setItemChecked(currentItem, true);
        }
    }
	
	private List<NDItem> generateMenuOptions()
	{
		List<NDItem> items = new ArrayList<>();
		NDItem item = new NDItem();
		item.title = getString(R.string.trending_shows);
		item.id = NDItem.Id.trending;
		item.type = NDItem.TYPE_MAIN;
        item.iconRes = R.drawable.ic_action_people;
		items.add(item);
		
		/*item = new NDItem();
		item.title = getString(R.string.my_shows);
		item.id = NDItem.Id.my_shows;
		item.type = NDItem.TYPE_MAIN;
        item.iconRes = R.drawable.ic_action_favorite;
		items.add(item);*/

        item = new NDItem();
        item.title = getString(R.string.my_watchlist);
        item.id = NDItem.Id.my_watchlist;
        item.type = NDItem.TYPE_MAIN;
        item.iconRes = R.drawable.ic_action_watchlist;
        items.add(item);

        item = new NDItem();
        item.type = NDItem.TYPE_SEPARATOR;
        items.add(item);

        item = new NDItem();
        item.title = getString(R.string.login);
        item.id = NDItem.Id.login_logout;
        item.type = NDItem.TYPE_OPT;
        items.add(item);

        item = new NDItem();
        item.title = getString(R.string.settings);
        item.id = NDItem.Id.settings;
        item.type = NDItem.TYPE_OPT;
        items.add(item);
		
		return items;
	}
	
}
