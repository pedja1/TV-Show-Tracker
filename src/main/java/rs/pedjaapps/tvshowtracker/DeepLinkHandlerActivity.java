package rs.pedjaapps.tvshowtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import java.util.List;

import rs.pedjaapps.tvshowtracker.utils.Utility;

/**
 * Created by pedja on 5.8.14. 12.43.
 * This class is part of the Dating
 * Copyright © 2014 ${OWNER}
 */
public class DeepLinkHandlerActivity extends AbsActivity
{
    Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link_handler);
        Intent intent = getIntent();
        data = intent.getData();

        handleUrl(data.getHost());

    }

    private void error()
    {
        Utility.showToast(this, R.string.error_handling_url);
        finish();
    }

    private void handleUrl(String domain)
    {
        Intent intent = new Intent(this, ShowDetailsActivity.class);
        if(domain == null)
        {
            error();
            return;
        }

        if(domain.contains("trakt"))
        {
            List<String> pathSegments = data.getPathSegments();
            if (!pathSegments.isEmpty() && "show".equals(pathSegments.get(0)))
            {
                intent.putExtra(ShowDetailsActivity.EXTRA_SHOW_NAME, pathSegments.get(1));
            }
            else
            {
                error();
                return;
            }
        }
        else if(domain.contains("tvdb"))
        {
            String id = data.getQueryParameter("id");
            intent.putExtra(ShowDetailsActivity.EXTRA_TVDB_ID, Utility.parseInt(id));
        }
        else if(domain.contains("imdb"))
        {
            List<String> pathSegments = data.getPathSegments();
            if (!pathSegments.isEmpty())
            {
                intent.putExtra(ShowDetailsActivity.EXTRA_IMDB_ID, pathSegments.get(1));
            }
            else
            {
                error();
                return;
            }
        }
        startActivity(intent);
        finish();
    }
}
