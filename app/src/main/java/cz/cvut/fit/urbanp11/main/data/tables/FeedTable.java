package cz.cvut.fit.urbanp11.main.data.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Petr Urban on 07.04.15.
 */
public class FeedTable {

    private void ArticleTable() {}

    public static final String TABLE_NAME = "feeds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINK = "link";

    /**
     * Helper
     */
    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LINK + " text not null, "
            + "unique(" + COLUMN_LINK + ")"
            + ")";

    private static final java.lang.String INSERT_FEED = "insert into "
            + TABLE_NAME
            + "(" + COLUMN_LINK + ") VALUES "
            + " ('http://servis.idnes.cz/rss.aspx?c=technet')";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
//        db.execSQL(INSERT_FEED);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
