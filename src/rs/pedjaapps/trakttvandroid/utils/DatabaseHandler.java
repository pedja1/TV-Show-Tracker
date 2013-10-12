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
                + " (id, episode_name, episode, season, first_aired,‡$h"Ğ–ËroÑ÷2ÅJ¡™m•9¿.åJ‹W)#V …à«	öÍB·Å;;T.ÙA¸~ğ×-¸üÜuåª’/ )ØLiÌwİ6óm›	*Ãúzèd	cB˜fKŠSßß$®Úêğ”ğÙ:w%îªşxÑ¤ÀŸKg«—ÙQcT¥)ß0$!ì=ÁªĞiÎi6a©ç´y|ñÛ©•ÏzÇcÊvU½I§\NÄ›öÈ-¦ÔÄ¼6
Ã+…ètn±”ª¯[á
åé˜Aâ…6a³•OèAÕµ ½*Î p^m¸f¼[6à˜@vÂ”
/k3·ÚÊ®ŸC'Û¿cH©ÒW!Íšİ%Èõ ¼ä§”å~Ä¬=Úğ×q¦RR$Š¬!PÓ9TŞ• •Ï•‚k°É|H”Ãû—»Ü’Ê¨lºhÚRº‹vq¡½ÛePÎ©"-¥q ùH~ìíÁªBå?§9/yëHÒígıä¨‹‘•ğ€®£dU¡ú2ƒÈÊCœÂVîĞ®CüÉœóÙóŸ×ÄZ]UcUl<‰<yÁk½İ(ù&:‰÷ÛR>)³£$ÿdÓç9¡½ÎŠK®=•q Ò ¾Âfğ	~êQS5|7]ı=|ÃD“C»»ûÆ§ Š3Ï¶òÂ3åT˜d'ö’‹•5ôwãÕ+®‡%ağ²#÷°hÏD¼SwõÆ	•ÓßÆË›háHİøj9¤}å‘òT‡O£Øú'™Â»‡Å¸&ç" ¢c}µ*‰ÃâŸçiÖA†­ÿÃûn¬â×EäØ˜`Q˜'°k”.Ø‡¿E¹ƒ?ß&fr
Àâ^µ„tÓ£3+›åâƒñ¡*•Tô1ŞâiÑÕ_˜ŞW¥bÛ™ñcşı«l€¼q ³êD‘Œ«Ï&\?lø˜n?P\Ğç­Wû EúGä?M4,:ºñjo«R­ÇN&Àõ"©kíšöpÍ¦‡ãMø}	;Bö¢%¶‚ÛÏ¾Œ©%ùÚÒ¬€lØgÿÖfP‚ˆ3a™Ü“q'>k
–$±wºw¨l€é_9ÓÊ)êjÎƒÓo[ÇÖÈBæ¬cç±(ôØæu’*}‹OjSÜ(vX_ÂÓÚ°‹f*p®_öø#êJ®7ÜıË‡áÂüÊˆKg‘QèVÎx)åWx~ eÂábC ßßz©Á—ŞwÅfĞ›Â:HÆnÕéÒ.RòÙu(í¦,²ı0µ“–/HWc×!aÜ(¯×Ú/±¿¡%„HùàWãUßÉ"¬vşFEË°u©9	06¸4èyYr 0TV4tQ¯¾ôÎ'=¾r^Ş×
¿c<—job¶6–½j
ßıõøâĞ‰PöšÌ^”3„yÑ^Á%ö‡ˆ)Eı<ÀP?óğ.‹Š!ºOCNÀU¥¯?A‰FYgWh²Ò`r</kV•… ¥‰À«BLŸ!CœÚeİïĞg£Œn¼]/8¼¢p$MD+í5¶¬@yTÊ4J“"
j@Ñ¸Àœù€Q(±¶®<İXòN¬fïC€s5ç¸VïL:ùOç+ŒÁüòu‡òu£ãÿQ_ú‚ÖšJ[Å£¦ŒÈD>AıŒm…#M?BmL@Æ Îä rØQÀH§·ş¡W•ëEPwÏuÅ½]Êe¿15ç»k
Ô„"(~V&ß8Ö†_kº\¥Qßÿ=}BNGØsLÉ[ÆÈf³6Šõ×éÖıÑ<]”Ÿäğ‚2‡L¨j`p˜û1n›ır±/m¥MÔß2-sfdWºöJãÚ1Üé.NÙ,×ŸîJ/?É©9°*F5gdÜ’XíìW?ä„óUTL¶„ú|Gñıß¸îÏÑõ~$lƒ¬¨™Œ|4Ş!ày•n¢;ÌMŸ@Ç¯öd¿V‰ê*BåİSÓêMK„¢°,x‰*éCÉ«w‚Åì!çcJ²ïê½«ƒAÌãN¸–ù>6Æğzñ¹e½”ì+hl€FpÛÖ(xñ#a…v‚şxbı—cÁ‚:	£Ê~’hŞÂ’%¸pàøu3mgÖ}oôÂr¦)á¼S[ó€Üjßš‰‚¦ŒÜM–ÖË#kêw-iy¯D{x©aØ…¥<l|ŒÑĞ#‡©lpË½3–é[ØÌ~ƒPĞîú¢ß­TLRzÙ%m)ÖÊ#WJ¹pcªVúÓM÷zÒ~Ç‹+¼›
Š¯QÇÍşş-Q¯ÒØn !öñú³Í–U?ÅF.ÌŒY:Äd¶âºOÀ»—³¹Fµ	«ñôşH·½¿Â]^ÿı;f„P' ùExuû¢6Œ’5Ö·HID€ÇHeË{¢eü3	z´×ñÌ‹"*¡ú’Ël4óÑ-h\ÿ-Äàzwt]oßZãQ=!äŸyÅáY‚àİ`=7	¡%J!®Nòçºünnûwj»˜¦”‘‰öCiZ•ã¶ã¢DB- Ø;ñ%^Û¯É·œ­„¿Ş^„}®9Ë9õ‡x â{âêû«]Û„:ŸXá”‘öì9¬†LıChÍ¯ãU­ÿI'w¨qŸ ÔÙ‚‡Ú®Á ­ÀUzçİ•™r²Õ*É„´‡½$±è¡=|2ĞVÖæÔ]ÅæŞÑ*²¸E½×)–@¯×vYé‹;73Uvî8’£ZRP¡†âG? êØGä6s?e­LÚİê3Ú›Ş_¬Û§Û›4M%Æûë“Ÿ`_Áf¢Jš—}ûìaLå)´üïÖ¨*Ş—öî&İÇóŠÅFëŸz™;8@a²O—aÕ‹Éƒ|Ù]ü·„cÀ;ÇÇ~@*ÓbâfïÅ–ÆTŒfEaÒ`Ò8íã'Õ}`ÒWÕe`	'#ö¨2ô!şŞÃoÂQõÔ\‰ŒÜÄ˜-¥7âûk.å1Zs!`‘ ‡hµôÂ.ÔRàˆºÑıö]Ñ'–e¹C»°È{şíg3ï"Ğ‹Æ­’s{yk:Q˜/Öh¥.ñ[õ5Y?©Ãô·Çí§ —TàªöB(Z.&¤çĞTl»ØG	Õ¬Ş{{^¡ß¯@%/ëIà
…€çæšO¡w5¬´4êÓg¹ûÜ_‰@Aäğ)Ûbå´÷à{Êí‡b5›ó×ÜÚŒişFşğ@fIjn;„ÀP	}ïP†"w±è™W~
MyêóoVO?»á5«Ì9`ĞË'0GR':Ü2R[¸1:¿×z°É×[¾+‹;Á45“Ã}¥ğ÷ÚVÄu°Yš£+q2†;–…,‘]¶å‰~C”*®ùa«ÿmÀ*ÏÜÄHòÃ™4£ØÂùÔÑÎ`9¡¤“ur»~ÎÈ…¶Tx‹2=îôKJ-j¹Ş×Uÿüó ©ÔÌ·XC0d·l|ÿtºØë©äıÍD]S
;¨y@7®}7µÀq9(ÄHPàñ_-'’!ª7İÃ·íÖO\visqWeÀŠÉÍ&€êÉ—y‡
hàÂÜB ®3-/x)ó‚IÖ(t3ØJ¹À·%M=	Ğ£‡m^ï‰9‰‡
´kÙ:CÅĞ%RÄûà]½ü¶\Éğ¶5 Ã€Æ,Î¿^ˆ(›zcÌ¹š!Î‚¦<K‡r®Tş}us­ Bj$J/Oæ2]¡ë"…t’ßPlâ‡eLggQİs<ÇtBLVIàÌîjŸøˆÏä~@ãİ85Ah¨_×Âí¯/Ñ*ëôR©Ëì^mTÿÀZ.e®b[Ÿ5V‡:ˆÒÇ<~™‚jX¢	©—eó¯Îú\˜m¿è÷3|Ñ•ª‚uª<ã_:?†avÍ'2lÒúxÆş‡Æ,Êÿw¹ÄH=9(U$$¾¾?½ªÿ?+Tf~X½Ñ´a§å;^‚ £¦©İ–¼]Ì²Œ042Â‘Cª²-á7ârÂè…,€Êº£º†º.bíÙÓ~×ú»]‚•¿²?y—±Ñ.x*rœSÉİİÈ×¢4ş±béĞíW“‡Ù/Íi8îü&–Íÿä’‹„¯ÃŠbÕåõÖHráVwµ§×”ú2'î5˜PNz>\ã/øêöDïÊğ¼¨=—îñF®xŞKÇ „ïH*À¤o´Z:½;Äár©×ÿ)hL´sºÅ"ğ<:Ğ¤îQ#Bn[D9ôƒM˜tš‹{¨ÕN'Ì6f-à?Ûyró²÷°•š¨*i°=·‘û¡«‘¨;~£º<„oéß~‘†x #åükCR+ú²–oİ,í.;Sæ¶5|m2w•"UñAúƒNÌßİp½	Ú º³¨Ä©˜Hx9Ø“È†…’zŠ¦Ø¹Có+Üİ`£_¹ò•Î5E.Ä;ß³óÿqÛÑ¹ù…„È¥@-Å•Ê˜î2p åëßJ(’ş¯C«_Z?ğšœáí}Ş  üß#é‡·†CÉ[æßV½´~V`#hf–ÎçF¿„¹¾Ó s^}D‰œ æHdğ^:<½ßÛãzpÊ#a½Êº‚7LàŞ~íV‘Ó—ùk6ßFÉí(Ğ¼WÓ	)i`}…y0}Ô®B6Ì,¼qVzŠŠè¯P?)1boÿiˆ°Ùo•ö³ÖÑ©’Ç(¾A¯õf˜>Òõ´¤Z›9:Nm•ÓõåHì3Ÿ8RÀu™w|¹ÌZv2÷-°º'F¼P
õş Õæ?=A™ÃéŸ¤’ã~§Á"Œ´)ôöúbôš	èpPÏÑÎÕ§”ˆ+:E£±™jâe{jl±–|î, ÆÄª†šà`ô
Ğt§6í2FI-µb¹ÎäxŞ¸¸šUŞŠø9Ûé£ÄFp-jcQ8P¼Aˆ¹ĞxüÁ‡Ÿö}›®dW»Ç$ÚUKƒÀ`\¨®$¡‘Ís(1ƒ–†ƒ{Sr{8ß=+ê7ö·G#Eù“ÎxWË*1Z0ÎHúê`efÙ$rÏÿ50qåGõØÛŒW´@ß¯tÆG”­ø­¹—Pi^à±¬Ÿ$®©‹î•K6å;ãõÆzò0å‹·Î7cØ™wÇ—9f¢nÔ%oÛí<8D?Šû¡gÍËµôÉ~]¸C×,­ßnp³Ì,=ŒÛÀğf¤e]—¡Ü"” ÏÄä§{&Ã¡ÆEÃpÀ!ªñe*Ñ2O><Ì¨¥?	‚îÁvIx"t®ğ}<Wì‚Ğú¿/)¶L[™·gºÖLmà]~â0?u†U}GúŠgıÎâ;ÕDixSW†«£Ö“%Ä™î:ÿœ_.G]ú€§Ÿ ~¯¤ĞŞ¡»*&W>Ál¸‹›Šk}ÂQÌ;&,ÿÍ¶?¸8î,÷¼÷®nY5½±ÆÃ"%C¥ÿ˜t,;1şºÕJıF-‡4ù=<dBã–ËÁ›d‚É•”Î¥Æhd’çdÁÈœ/„Esûí™D¶º‰ôk<dRàMgÙ¿HQ}ğ´d)DB³Úù`°î6à:$­'ÍWƒ”Öpü¶D˜y~ÚˆG*ÿÖlx­ø$r“ìùaÓò‹`äîÚ]wZÑ
`Ïİ'ğ12¬‹ôü½Ü…>*9Üğ4r§òÛ~FL‡‹^îFï2cÄÄaf*j>şıeàm«¡İ18½>Ø¥î]
ÑÙ“[zT¼äfç"Pú=[ûğïÍÎªöJøáiíØß+ÚEø09(a3²[qªQp%õkQ&èĞÖX 2`weá…Ÿ¤wWEù—ÉŸ'¶Ë²àHø4.pttŠ1¡v¡£Ãı‡RÛÜY-8i©Ã™;İª¿3ô(Ô«ËºuMäèjkp
Ê‰òÏutqğËä8ºØ!Q.í-5±)ÙÔzAe¢a£¶­¨§R¶ò‚øÚ”±UèkÃ«Ò
.Œî•ß£ûÁØï·ñ	§È6|
naC9ş¸&ÓÏ¬ÄÅC’!ë‡ÂM‚oÚe*.Ú²ØøĞo¤&N¯B€v%)¬RßøFB±¢¯Oíâ, ¬>¾aw¦r¼–;péß l’¶²€>ûÂ:í¬ ¨‹÷ñäÁ
²‚?Á«ÜëŒ?Tm*Tp¨@8Øè¬Ïô}Æ Uê’çnÒåÉIÜº~Üª„hF5¯øN|x.¥72qÂíûA¨ñ¢üš
IVN€?Ø –©½G’Uå‘>
„.«@‚¾ş·‚˜Os*
7[·h49?<ĞA_²Ò’n¼©?òu÷ÏV·À#¦¥ö.Ÿ*$=ŸË¥yJÇ©lGs¯4T¼şX#P³]ìåİ–:¹ˆ`‰6Y,ôÌ‹ûÿ~¸ezÕOn§5Å«ô‚×jtm nXêø²LR‡P!¾Á“¤§zC#ÉE)Ux³@x@EÈæãÌA~¹]½Â~rËr¦^7ïR¢ÄæüZoóË‰=ĞYFrpX~²½M8H×ú§¨şÍG³È?læ‚(øy9²iEÉ…UÏãÍVzÌ‡ÒşV¬Q
 rÅH0Å9óvÚ£Î¾” +:"õ×0™š;B¾‰¿—¾ MšsS€`BÕVV7‘ƒ™à€N½6üÎ/EÓ0¡™EßS>¡"ƒÄœ2a¥†8KŸ¦mÍ0Ğ,N¾`GwA}¢3Á¢åWó»T6=sĞBµe]2ÖŞ4Åw“ş(¼§ÑÅ3Æ cÔúf©weé-3—‰JØÛØ\ñ£>NÃj†³Ê}»D÷ÉZY….fì8'ÛfÿâÊ¨Èò:c–°ÊÙÙ-ıfv¯…t                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            