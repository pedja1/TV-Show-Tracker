package rs.pedjaapps.tvshowtracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.LinePageIndicator;


public class EpisodeDetailsActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        LinePageIndicator indicator = (LinePageIndicator)findViewById(R.id.lines);
    }
}
