package rs.pedjaapps.tvshowtracker.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by pedja on 19.10.14..
 */
public class StateSaveRecyclerView extends RecyclerView
{
    public StateSaveRecyclerView(Context context)
    {
        super(context);
    }

    public StateSaveRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public StateSaveRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(state);
    }
}
