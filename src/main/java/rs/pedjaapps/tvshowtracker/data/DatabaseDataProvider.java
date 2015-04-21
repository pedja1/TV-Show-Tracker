package rs.pedjaapps.tvshowtracker.data;

/**
 * Created by pedja on 7.11.14. 09.34.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class DatabaseDataProvider<B, T> implements DataProvider<T>
{
    //QueryBuilder<B> builder;
    T resultData;
    int requestCode;

   /* public DatabaseDataProvider(QueryBuilder<B> builder, int requestCode)
    {
        if(requestCode <= 0)
        {
            throw new IllegalArgumentException("invalid request code");
        }
        this.builder = builder;
        this.requestCode = requestCode;
    }

    public DatabaseDataProvider(int requestCode)
    {
        this(null, requestCode);
    }*/

    @Override
    @SuppressWarnings("unchecked")
    public boolean load()
    {
        /*if(SettingsManager.DEBUG()) Log.d(Constants.LOG_TAG, String.format("DatabaseDataProvider::load()[requestCode=%s]", requestCode));
        switch (requestCode)
        {
            case REQUEST_CODE_FILTER_TYPE:
                // IMPORTANT
                // make sure that T is always FilterDropdownItem and B is always Type when using this request code

                TypeDao typeDao = MainApp.getInstance().getDaoSession().getTypeDao();
                List<Type> list = builder != null ? (List<Type>) builder.list() : typeDao.loadAll();
                if(list == null || list.isEmpty())return false;
                List<FilterDialog.FilterDropdownItem> items = new ArrayList<>();

                for(Type type : list)
                {
                    FilterDialog.FilterDropdownItem item = new FilterDialog.FilterDropdownItem(type.getTitle(), type.getId() + "");
                    items.add(item);
                }

                resultData = (T) items;
                return true;
        }*/
        return false;
    }

    @Override
    public T getResult()
    {
        return resultData;
    }
}
