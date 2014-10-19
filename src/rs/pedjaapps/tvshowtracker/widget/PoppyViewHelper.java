package rs.pedjaapps.tvshowtracker.widget;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;


public class PoppyViewHelper
{

    public enum PoppyViewPosition
    {
        TOP, BOTTOM
    }

    private static final int SCROLL_TO_TOP = -1;

    private static final int SCROLL_TO_BOTTOM = 1;

    private static final int SCROLL_DIRECTION_CHANGE_THRESHOLD = 5;

    private Activity mActivity;

    private LayoutInflater mLayoutInflater;

    private View mPoppyView;

    private int mScrollDirection = 0;

    private int mPoppyViewHeight = -1;

    private PoppyViewPosition mPoppyViewPosition;

    public PoppyViewHelper(Activity activity, PoppyViewPosition position)
    {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        mPoppyViewPosition = position;
    }

    public PoppyViewHelper(Activity activity)
    {
        this(activity, PoppyViewPosition.BOTTOM);
    }

    public View createPoppyViewOnGridView(int listViewId, int poppyViewResId, RecyclerView.OnScrollListener onScrollListener)
    {
        final RecyclerView listView = (RecyclerView) mActivity.findViewById(listViewId);
        mPoppyView = mLayoutInflater.inflate(poppyViewResId, null);
        initPoppyViewOnGridView(listView, onScrollListener);
        return mPoppyView;
    }

	public View createPoppyViewOnGridView(RecyclerView gridCiew, int poppyViewResId, RecyclerView.OnScrollListener onScrollListener)
    {
        mPoppyView = mLayoutInflater.inflate(poppyViewResId, null);
        initPoppyViewOnGridView(gridCiew, onScrollListener);
        return mPoppyView;
    }
	
	public View createPoppyViewOnGridView(RecyclerView gridView, int poppyViewResId)
    {
        return createPoppyViewOnGridView(gridView, poppyViewResId, null);
    }
	
    public View createPoppyViewOnGridView(int listViewId, int poppyViewResId)
    {
        return createPoppyViewOnGridView(listViewId, poppyViewResId, null);
    }

    // common

    private void setPoppyViewOnView(View view)
    {
        LayoutParams lp = view.getLayoutParams();
        ViewParent parent = view.getParent();
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(view);
        final FrameLayout newContainer = new FrameLayout(mActivity);
        group.removeView(view);
        group.addView(newContainer, index, lp);
        newContainer.addView(view);
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = mPoppyViewPosition == PoppyViewPosition.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        newContainer.addView(mPoppyView, layoutParams);
        group.invalidate();
    }

    private void onScrollPositionChanged(boolean show)
    {
        int newScrollDirection;

        if (show)
        {
            newScrollDirection = SCROLL_TO_TOP;
        }
        else
        {
            newScrollDirection = SCROLL_TO_BOTTOM;
        }

        if (newScrollDirection != mScrollDirection)
        {
            mScrollDirection = newScrollDirection;
            translateYPoppyView();
        }
    }

    private void translateYPoppyView()
    {
        mPoppyView.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mPoppyViewHeight <= 0)
                {
                    mPoppyViewHeight = mPoppyView.getHeight();
                }

                int translationY = 0;
                switch (mPoppyViewPosition)
                {
                    case BOTTOM:
                        translationY = mScrollDirection == SCROLL_TO_TOP ? 0 : mPoppyViewHeight;
                        break;
                    case TOP:
                        translationY = mScrollDirection == SCROLL_TO_TOP ? -mPoppyViewHeight : 0;
                        break;
                }
                ViewPropertyAnimator.animate(mPoppyView).setDuration(300).translationY(translationY);
            }
        });
    }

    private void initPoppyViewOnGridView(RecyclerView listView, final RecyclerView.OnScrollListener onScrollListener)
    {
        setPoppyViewOnView(listView);
        listView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (onScrollListener != null)
                {
                    onScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                onScrollPositionChanged(dy < 0);
                System.out.println(dy);
            }
        });
    }

}
