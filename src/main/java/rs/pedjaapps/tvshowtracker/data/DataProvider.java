package rs.pedjaapps.tvshowtracker.data;

/**
 * Created by pedja on 6.11.14. 15.41.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public interface DataProvider<T>
{
    public static final int REQUEST_CODE_SOMETHING = 401;
    public boolean load();
    public T getResult();
}
