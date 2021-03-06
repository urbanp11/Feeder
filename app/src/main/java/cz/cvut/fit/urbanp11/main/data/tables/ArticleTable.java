package cz.cvut.fit.urbanp11.main.data.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Petr Urban on 07.04.15.
 */
public class ArticleTable {

    private void ArticleTable() {}

    public static final String TABLE_NAME = "articles";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_SERVER_ID = "server_id";

    /**
     * Helper
     */
    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_LINK + " text not null, "
            + COLUMN_SERVER_ID + " integer not null,"
            + "unique(" + COLUMN_TITLE + ")"
            + ")";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
