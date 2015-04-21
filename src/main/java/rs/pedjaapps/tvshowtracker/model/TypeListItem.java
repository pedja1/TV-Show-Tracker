package rs.pedjaapps.tvshowtracker.model;

/**
 * Created by pedja on 29.10.14. 11.26.
 * This class is part of the Hub2Date
 * Copyright © 2014 ${OWNER}
 */
public class TypeListItem<I>
{
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADER = 1;
    public static final int VIEW_TYPE_COUNT = 2;

    private int viewType = VIEW_TYPE_ITEM;
    I item;

    public TypeListItem(I item)
    {
        this.item = item;
        this.viewType = VIEW_TYPE_ITEM;
    }

    public TypeListItem()
    {
        this.viewType = VIEW_TYPE_LOADER;
    }

    public int getViewType()
    {
        return viewType;
    }

    public void setViewType(int viewType)
    {
        this.viewType = viewType;
    }

    public I getItem()
    {
        return item;
    }

    public void setItem(I item)
    {
        this.item = item;
    }
}
