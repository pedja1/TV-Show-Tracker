package rs.pedjaapps.tvshowtracker.network;


import rs.pedjaapps.tvshowtracker.AbsActivity;
import rs.pedjaapps.tvshowtracker.model.TypeListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedja on 29.10.14. 11.22.
 * This class is part of the Hub2Date
 * Copyright © 2014 ${OWNER}
 */
public class PagedLoader<T>
{
    /**
     * List holding all loaded items*/
    private List<TypeListItem<T>> items;
    int page, items_per_page = -1, total_items;

    /**
     * callback for loading*/
    OnLoadListener<T> listener;

    /**
     * NetAction object responsible for loading data from net*/
    NetAction na;

    /**
     * Request code for NetAction*/
    int requestCode;

    /**
     * optional parameters for NetAction*/
    String[] optParams;

    public PagedLoader(AbsActivity activity, int requestCode, String... optParams)
    {
        this(activity, requestCode, true, optParams);
    }

    public PagedLoader(AbsActivity activity, int requestCode, final boolean autoAddLoaderItem, String... optParams)
    {
        this.optParams = optParams;
        this.requestCode = requestCode;
        items = new ArrayList<>();
        na = new NetAction(activity);
        na.setResponseHandler(new ResponseHandlerImpl()
        {
            @Override
            public void onResponse(int responseStatus, JSONUtility jsonUtility)
            {
                if (responseStatus == RESPONSE_STATUS_OK && jsonUtility.getParseObject() != null)
                {
                    Page<T> pge = jsonUtility.getParseObject();
                    page = pge.page;
                    items_per_page = pge.items_per_page;
                    total_items = pge.total_items;

                    if (autoAddLoaderItem && !items.isEmpty() && items.get(items.size() - 1).getViewType() == TypeListItem.VIEW_TYPE_LOADER)
                    {
                        items.remove(items.size() - 1);
                    }
                    List<TypeListItem<T>> list = pge.items;
                    double totalPages = Math.ceil((double) total_items / items_per_page);
                    
                    if (autoAddLoaderItem && page + 1 < totalPages && getItemsCount(list) >= items_per_page)
                    {
                        list.add(new TypeListItem<T>());
                    }
                    items.addAll(list);
                    if (listener != null) listener.onLoaded(true, list);
                }
                else
                {
                    if (listener != null) listener.onLoaded(false, null);
                }
            }
        });
    }

    private int getItemsCount(List<TypeListItem<T>> list)
    {
        int count = 0;
        for(TypeListItem<T> item : list)
        {
            if(item.getViewType() == TypeListItem.VIEW_TYPE_ITEM)
                count++;
        }
        return count;
    }

    /**
     * Load next page
     * This will cancel all active tasks with same id*/
    public void loadPreviousPage()
    {
        if(page <= 0)return;
        na.cancel(requestCode, true);
        na.loadNextPage(requestCode, page - 1, optParams);
    }

    /**
     * Load next page
     * This will cancel all active tasks with same id*/
    public void loadNextPage()
    {
        na.cancel(requestCode, true);
        na.loadNextPage(requestCode, page + 1, optParams);
    }

    /**
     * Resets loader
     * to initial state(for loading first page)*/
    public void reset()
    {
        na.cancel(requestCode, true);
        page = -1;
        items_per_page = -1;
        total_items = 0;
        items.clear();
        loadNextPage();
    }

    public List<TypeListItem<T>> getItems()
    {
        return items;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public void setItems_per_page(int items_per_page)
    {
        this.items_per_page = items_per_page;
    }

    public void setTotal_items(int total_items)
    {
        this.total_items = total_items;
    }

    public void setOnLoadListener(OnLoadListener<T> listener)
    {
        this.listener = listener;
    }

    public int getPage()
    {
        return page;
    }

    public int getItems_per_page()
    {
        return items_per_page;
    }

    public int getTotal_items()
    {
        return total_items;
    }

    public void setOptParams(String... optParams)
    {
        this.optParams = optParams;
    }

    public static interface OnLoadListener<T>
    {
        /**
         * Called when loading has been finished or error occured
         * @param success if loading was successful*/
        public void onLoaded(boolean success, List<TypeListItem<T>> newItems);
    }

    public static class Page<T>
    {
        public List<TypeListItem<T>> items;
        public int page, items_per_page = -1, total_items;

        public Page()
        {
            items = new ArrayList<>();
        }
    }

    public void cancel()
    {
        if(na != null)
            na.cancel(requestCode, true);
    }
}
