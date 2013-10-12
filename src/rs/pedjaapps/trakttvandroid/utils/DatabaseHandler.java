package rs.pedjaapps.trakttvandroid.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.trakttvandroid.model.Actor;
import rs.pedjaapps.trakttvandroid.model.EpisodeItem;
import rs.pedjaapps.trakttvandroid.model.Show;
public class DatabaseHandler extends SQLiteOpenHelper
{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "tvst.db";

	// table names
	private static final String TABLE_SERIES = "series";
	private static final String TABLE_EPISODES = "episodes";
	private static final String TABLE_ACTORS = "actors";

	// private static final String TABLE_ITEM = "item_table";
	// Table Columns names

	private static final String[] show_filds = {"title",
			"year", "url", "first_aired", "country", "overview", "runtime",
			"network", "air_day", "air_time", "certification", "imdb_id",
			"tvdb_id", "tvrage_id", "last_updated", "poster", "fanart", "banner", "ended" };
	private static final String[] episode_filds = {"episode", "season",
			"episode_name", "first_aired", "imdb_id", "overview", "rating",
			"watched", "episode_id", "seriesId", "profile_name", "id" };
	private static final String[] actors_filds = {"actor_id", "name",
			"role", "image", "seriesId", "profile_name" };
	SQLiteDatabase db;
	static DatabaseHandler databaseHandler = null;
	
	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized DatabaseHandler getInstance(Context context)
	{
		if(databaseHandler == null)
		{
			databaseHandler = new DatabaseHandler(context);
		}
		return databaseHandler;
	}
	
	public boolean open()
	{
		try
		{
		     db = this.getWritableDatabase();
			 return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void close()
	{
		try
		{
			db.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		super.close();
	}
	
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_SERIES_TABLE = "CREATE TABLE " + TABLE_SERIES 
		        + "("
				+ show_filds[0] + " TEXT,"
				+ show_filds[1] + " INTEGER,"
				+ show_filds[2] + " TEXT,"
				+ show_filds[3] + " TEXT,"
				+ show_filds[4] + " TEXT,"
				+ show_filds[5] + " TEXT,"
				+ show_filds[6] + " TEXT,"
				+ show_filds[7] + " TEXT,"
				+ show_filds[8] + " DOUBLE,"
				+ show_filds[9] + " INTEGER,"
				+ show_filds[10] + " TEXT,"
				+ show_filds[11] + " TEXT,"
				+ show_filds[12] + " BOOLEAN,"
				+ show_filds[13] + " BOOLEAN,"
				+ show_filds[14] + " TEXT,"
				+ show_filds[15] + " TEXT,"
			    + show_filds[16] + " TEXT,"
			    + show_filds[17] + " INTEGER NOT NULL,"
			+ "PRIMARY KEY ( " + show_filds[1] + ", " + show_filds[16] + ", " + show_filds[17] + " )"
				+ ")";

		String CREATE_INDEXES_ON_SERIES_TABLE = "CREATE INDEX series_idx ON " + TABLE_SERIES + "(series_id, profile_name, id)";

        String CREATE_EPISODE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EPISODES
		        + "("
			    + episode_filds[0] + " INTEGER,"
                + episode_filds[1] + " INTEGER,"
                + episode_filds[2] + " TEXT,"
                + episode_filds[3] + " TEXT,"
                + episode_filds[4] + " TEXT,"
                + episode_filds[5] + " TEXT,"
                + episode_filds[6] + " DOUBLE,"
                + episode_filds[7] + " BOOLEAN,"
                + episode_filds[8] + " INTEGER,"
			    + episode_filds[9] + " TEXT,"
			    + episode_filds[10] + " TEXT,"
			    + episode_filds[11] + " INTEGER NOT NULL,"
			+ "PRIMARY KEY ( " + episode_filds[8] + ", " + episode_filds[9] + ", " + episode_filds[10] + ")"
                + ")";

        String CREATE_INDEXES_ON_EPISODES_TABLE = "CREATE INDEX episodes_idx ON " + TABLE_EPISODES + "(seriesId, profile_name, id)";

        String CREATE_ACTORS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTORS
			    + "("
			    + actors_filds[0] + " TEXT,"
                + actors_filds[1] + " TEXT,"
			    + actors_filds[2] + " TEXT,"
                + actors_filds[3] + " TEXT,"
			    + actors_filds[4] + " TEXT,"
                + actors_filds[5] + " TEXT,"
                + "PRIMARY KEY ( " + actors_filds[0] + ", " + actors_filds[4] + ", " + actors_filds[5] + ")"
                + ")";

        String CREATE_INDEXES_ON_ACTORS_TABLE = "CREATE INDEX actors_idx ON " + TABLE_ACTORS + "(seriesId, profile_name)";

		db.execSQL(CREATE_SERIES_TABLE);
		db.execSQL(CREATE_INDEXES_ON_SERIES_TABLE);

		db.execSQL(CREATE_EPISODE_TABLE);
        db.execSQL(CREATE_INDEXES_ON_EPISODES_TABLE);

		db.execSQL(CREATE_ACTORS_TABLE);
        db.execSQL(CREATE_INDEXES_ON_ACTORS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void insertShows(List<Show> shows)
	{
	    final SQLiteStatement statement = db.compileStatement("INSERT OR REPLACE INTO " + TABLE_SERIES + " (id, series_name, first_aired, imdb_id, overview, rating, series_id, language, banner, fanart, network, runtime, status, updated, profile_name) VALUES((SELECT IFNULL(MAX(id), 0) + 1 FROM series), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    db.beginTransaction();
	    try 
	    {
	        for(Show show : shows)
	        {
	            statement.clearBindings();
	            statement.bindString(1, show.getSeriesName());
	            statement.bindString(2, show.getFirstAired());
	            statement.bindString(3, show.getImdbId());
	            statement.bindString(4, show.getOverview());
	            statement.bindDouble(5, show.getRating());
	            statement.bindLong(6, show.getSeriesId());
	            statement.bindString(7, show.getLanguage());
	            statement.bindString(8, show.getBanner());
	            statement.bindString(9, show.getFanart());
	            statement.bindString(10, show.getNetwork());
	            statement.bindLong(11, show.getRuntime());
	            statement.bindString(12, show.getStatus());
	            statement.bindString(13, show.getUpdated());
	            statement.bindString(14, show.getProfileName());
	            statement.execute();
	        }
	        db.setTransactionSuccessful();
	    } 
	    finally 
	    {
	        db.endTransaction();
	    }
	    //db.close();
	}
	 
	 public void wipeDatabase()
	 {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);
		 onCreate(db);
	 }

	/**
	 * @param filter
	 *            Can be either all, continuing, or ended
	 */
	public synchronized List<Show> getAllShows(String filter, String profile, String sortOrder, String sortType)
	{
		long startTime = System.currentTimeMillis();
		List<Show> shows = new ArrayList<Show>();
		// Select All Query
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT  * FROM " + TABLE_SERIES + " WHERE");

		if (filter.equals("ended"))
		{
			builder.append(" status LIKE \"%Ended%\"");
		}
		else if (filter.equals("continuing"))
		{
			builder.append(" status LIKE \"%Continuing%\"");
		}
		else
		{
			builder.append(" status LIKE \"%\"");
		}
		builder.append(" and profile_name LIKE \"%" + profile + "%\" ");
		if(sortOrder.length() != 0 && columnExists(show_filds, sortOrder))
		{
			builder.append("ORDER BY " + sortOrder + " " + sortType);
		}
		String selectQuery = builder.toString();// "SELECT  * FROM " +
												// TABLE_SERIES;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Show show = new Show();
				show.setSeriesName(cursor.getString(0));
				show.setSeriesId(cursor.getInt(1));
				show.setLanguage(cursor.getString(2));
				show.setBanner(cursor.getString(3));
				show.setNetwork(cursor.getString(4));
				show.setFirstAired(cursor.getString(5));
				show.setImdbId(cursor.getString(6));
				show.setOverview(cursor.getString(7));
				show.setRating(cursor.getDouble(8));
				show.setRuntime(cursor.getInt(9));
				show.setStatus(cursor.getString(10));
				show.setFanart(cursor.getString(11));
				show.setIgnore(intToBool(cursor.getInt(12)));
				show.setHide(intToBool(cursor.getInt(13)));
				show.setUpdated(cursor.getString(14));
				show.setActors(cursor.getString(15));
				show.setProfileName(cursor.getString(16));

				shows.add(show);
			}
			while (cursor.moveToNext());
		}

		// return list
		cursor.close();
		Log.d(Constants.LOG_TAG,
				"DatabaseHandler.java > getAllShows(): "
						+ (System.currentTimeMillis() - startTime) + "ms");
		return shows;
	}

	public synchronized boolean showExists(String seriesName, String profile)
	{
		Cursor cursor = db.query(TABLE_SERIES, show_filds, show_filds[0]
				+ "=? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesName }, null, null, null, null);
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}

	public synchronized void deleteSeries(String seriesId, String profile)
	{
		db.delete(TABLE_SERIES, show_filds[1]
				+ " = ? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesId });

		deleteEpisodes(seriesId, profile);
		deleteActors(seriesId, profile);
	}

	public synchronized void deleteEpisodes(String seriesId, String profile)
	{
		db.delete(TABLE_EPISODES, "profile_name = ? and seriesId = ?",
				new String[] { profile, seriesId });
	}

	public synchronized void deleteActors(String seriesId, String profile)
	{
		db.delete(TABLE_ACTORS, "profile_name = ? and seriesId = ?",
				new String[] { profile, seriesId });
	}

	public synchronized Show getShow(String seriesId, String profile)
	{
		Cursor cursor = db.query("series", show_filds, show_filds[1]
				+ "=? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesId }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Show e = new Show(cursor.getString(0),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getDouble(8), cursor.getInt(1), cursor.getString(2),
				cursor.getString(3), cursor.getString(11), cursor.getString(4),
				cursor.getInt(9), cursor.getString(10),
				intToBool(cursor.getInt(12)), intToBool(cursor.getInt(13)),
				cursor.getString(14), cursor.getString(15), cursor.getString(16));
		// return list
		cursor.close();
		return e;
	}

    public void insertEpisodes(List<EpisodeItem> episodeItems)
    {
        final SQLiteStatement statement = db.compileStatement("INSERT OR REPLACE INTO " + TABLE_EPISODES
                + " (id, episode_name, episode, season, first_aired,�$h"Ж�ro��2�J��m�9�.�J�W)#V ��	��B��;;T.�A�~��-���u媒/�)؞�Li�w�6�m�	*��z�d	cB�fK�S��$������:w%��xѤ��Kg���QcT��)�0$!�=���i�i6a�紞y|�۩��z�c�vU�I�\Ně��-��ļ6
�+��tn����[�
��A�6a���O�Aյ��*� p^m�f�[6��@v
/k3�����C'ۿcH��W!͚�%�� �䧔�~Ĭ=���q�RR$��!P�9Tޕ ����k��|H������ܒʨl�h�R��vq���ePΩ"-�q �H~����B�?�9/y�H���g���������dU��2���C��V�ЮC�ɜ�����Z]UcUl<�<y�k����(�&:���R>)��$�d��9��ΊK�=�q � ��f�	~�QS5|7]�=|�D�C���Ƨ �3϶��3�T�d'����5�w��+��%a�#��h�D�Sw��	����˛h�H��j9�}��T�O���'�»�Ÿ&�" �c}�*����i�A�����n���E�ؘ`Q�'�k�.؇�E��?�&fr�
��^��t��3+����*�T�1��i��_��W�bۙ�c���l��q����D����&\?l��n?P\��W� E�G�?M4,:��jo�R��N&��"�k��pͦ���M�}	;B��%���Ͼ��%���Ҭ�l�g��fP��3a�ܓq'>k
�$�w�w�l��_9��)�j���o[���B�c�(���u�*}�OjS�(vX_��ڰ�f*p�_��#�J�7�������ʈKg�Q�V�x)�Wx~�e��bC ��z�����w�fЛ�:H�n���.R��u(�,��0���/HWc�!a�(���/���%�H��W�U��"�v�FE˰u�9	06�4�yYr�0TV4tQ����'=�r^�ׁ
�c<�job��6��j
�����ЉP���^�3�y�^�%���)E�<�P?��.��!�OCN�U��?A�FYgWh��`r</kV��� ����BL�!C��e���g��n�]/8��p$MD+�5��@yT�4J�"
j@��������Q(���<�X�N�f�C�s5��V�L:��O�+����u��u����Q_��֚J[�����D>A��m�#M?BmL@� �� r�Q�H����W���EPw�uŽ]�e�15�k
ԍ�"(�~V�&�8ֆ_k�\��Q��=}B�NG�sL�[��f�6�������<]�����2�L�j`p��1n��r�/m�M��2-sfdW��J��1��.N�,���J/?ɩ9�*F5gd�ܒX��W?���UTL���|G��߸����~$l�����|4�!�y�n�;�M�@ǯ�d�V��*B��S��MK���,x�*�Cɫw���!�cJ���꽫�A��N���>6��z�e���+hl�Fp��(x�#a�v��xb��c��:	��~�h�%�p��u3mg�}o��r�)�S[��jߚ�����M���#k�w-iy�D{x�a؅�<�l|���#��l�p˽3��[��~�P����߭TLRz�%m)��#WJ�pc�V��M�z�~ǋ+��
��Q����-Q���n�!����͖U?�F.̌Y:��d�⁺O�����F��	����H����]^��;f�P�' �Exu��6��5ַ�HID��He�{�e��3	z���̋"*�����l4��-h\�-��zwt]o�Z�Q=!�y��Y���`=7	�%J!�N�纁�nn�wj�������CiZ����DB- �;�%^ۯɷ�����^�}�9�9��x �{����]ۄ:�Xᔑ��9��L�Chͯ�U��I'w��q� �ق�ڮ����Uz�ݕ�r��*Ʉ���$��=|2�V���]����*��E��)��@��vY�;73Uv�8��ZRP���G? ��G�6s?e�L���3ڛ�_�ۧۛ�4M%�����`_�f�J��}��aL�)���֨*ޗ��&�����F��z�;8�@a��O�aՋɃ|�]���c�;��~@*�b�f����T�fEa�`�8��'�}`�W�e`	'#��2�!���o�Q��\���Ę-�7��k.��1Zs!`� �h���.�R������]�'�e�C���{��g3�"Ћƭ�s{y�k��:Q�/�h�.�[�5Y?������� �T��B(Z.&���Tl�؝G	լ�{{^�߯@%/�I�
����O�w5��4��g���_�@A��)�b���{��b5����ڌi�F��@fIjn;��P	}�P�"w��W~
My��oVO?��5��9`��'0GR':�2R[�1�:��z���[�+�;�45���}����V�u�Y��+q2�;��,�]��~C�*��a��m�*���H���4�������`9���ur�~�ȅ�Tx�2=��K�J-j���U��� ���̷XC0d�l|�t������D]S
;�y@7�}7��q9(�HP��_-'�!�7�÷��O\visqWe����&����y�
h���B �3-/x)�I�(t3�J����%M=	У��m^�9��
�k�:C��%R���]���\��5 À�,��^�(�zc̐��!΂�<K�r�T�}us���Bj$J/O�2]��"�t��Pl��eLggQ�s<�tBLVI���j�����~@��85Ah�_���/�*��R���^mT��Z.e�b[�5V�:���<~��jX�	��e����\�m���3|ѕ��u�<�_:?�av�'�2l��x����,��w��H=9�(U$$��?���?+Tf~X�Ѵa��;^� ���ݖ�]̲�042��C��-�7�r��,�ʺ����.b���~���]����?y���.x*r�S����ע�4��b���W���/�i8��&���䒋��Êb����Hr�Vw��ה�2'�5�PNz>�\�/���D���=���F�x�KǠ��H*��o�Z:�;��r���)hL�s��"�<:���Q#Bn[D9�M�t���{��N'�6f-�?�yr������*i�=�������;~��<�o��~��x #��kCR+���o�,�.;S�5|m2w�"U�A��N���p�	� ������Hx9ؓȆ��z���عC�+��`�_���5E.�;߳��q�ѹ���ȥ@-��ʘ�2p����J(���C�_Z?����}ޏ�  ��#����C�[��V��~V`#hf���F����� s^}�D������Hd�^:<����zp�#a�ʺ�7L��~�V�ӗ�k6�F��(мW�	)i`}�y0}ԮB6�,�qVz���P?)1bo�i���o����ѩ��(�A��f��>������Z�9:Nm����H�3�8R�u�w|��Zv2�-��'F�P
�� ��?=A��韤��~��"��)����b��	�pP���է��+:E���j�e{jl��|�, �Ī���`�
�t�6�2FI-�b���x����Uފ�9���Fp-jcQ�8P�A���x�����}��dW��$�UK��`\��$���s(1����{Sr{8�=+�7��G#E���xW�*1Z0�H��`ef�$r��50q�G��یW�@��t�G������Pi^౬�$����K6�;���z�0勷�7cؙwǗ9f�n�%o��<8D?����g͎˵���~]�C�,��np��,=����f�e]���"� ����{&���E�p�!��e*�2O><̨�?	���vIx"t��}<W�����/)�L[��g��Lm�]~�0�?u�U}�G��g���;�D�ixSW���֓%ę�:��_.G]����� ~���ޡ�*&W>�l����k}�Q�;&,����?�8�,����nY5����"%C���t,;1���J�F-�4�=<dB����d�ɕ�Υ��hd��d�Ȝ/�E�s��D����k<d��R�Mg��HQ}�d)DB���`��6�:$�'�W���p��D�y~ڈG*��lx��$r���a��`���]wZ�
`��'��12������܅>*9��4r���~FL��^�F�2c��af*�j>��e��m���18�>���]
�ٓ[z���T��f�"P�=[����Ϊ�J���i���+�E�09(a3�[q�Qp%�kQ&���X 2`weᅟ�wWE��ɟ'�˲�H�4.ptt�1�v�����R��Y-8i�Ù;ݪ�3�(ԫ˺�uM��jkp
ʉ��ut�q���8��!Q.�-5�)ٞ�zAe�a�����R���ڔ�U�kë��
.��ߣ�����	��6|
naC9��&�Ϭ��C�!띇�M�o�e*.ڲ���o�&N�B�v%)�R��FB���O��, �>�a�w�r��;p�� l����>��:�������
��?����?Tm*Tp��@8����}ƠU��n���Iܺ~ܪ�hF5��N|x.�72q���A����
IVN�?ؠ���G�U��>
�.�@�������Os*�
7[�h49?<�A_�Ғn��?�u��V��#���.�*$=�˥yJǩlGs�4T��X#P�]��ݖ:��`�6Y,�̋���~�ez�On�5ū��jtm�nX���LR�P!�����zC#�E)U�x�@x@E����A~�]��~r�r�^7�R����Zo���=�YFrpX~��M8H������G��?l�(�y9�i�EɅU���Vż��V�Q
 r�H0�9�v��ξ� +:"��0���;B����� M�sS�`�B�VV7�����N��6��/E�0��E�S>�"�Ĝ2a��8K��m�0�,N�`GwA}�3���W�T6=s�B�e]2��4�w��(����3� c��f�we�-3���J���\��>N�j���}�D��ZY�.f�8'�f��ʨ��:c�����-�fv��t                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            