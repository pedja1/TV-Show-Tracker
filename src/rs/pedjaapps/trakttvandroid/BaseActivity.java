package rs.pedjaapps.trakttvandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BaseActivity extends FragmentActivity {

    SlidingMenu sideMenu;

    @Override
    protected void onCreate(Bundle sis) 
    {
        super.onCreate(sis);
        sideMenu = new SlidingMenu(this);
        sideMenu.setMode(SlidingMenu.LEFT);
        sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sideMenu.setShadowWidthRes(R.dimen.shadow_width);
        sideMenu.setShadowDrawable(R.drawable.menu_shadow);
        sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sideMenu.setFadeDegree(0.35f);
        sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sideMenu.setMenu(R.layout.menu_layout);
    }

    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
    }

}
