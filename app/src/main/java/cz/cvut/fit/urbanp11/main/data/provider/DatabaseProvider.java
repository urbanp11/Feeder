package cz.cvut.fit.urbanp11.main.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;

import cz.cvut.fit.urbanp11.main.data.helper.DatabaseHelper;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;

/**
 * Created by Petr Urban on 07.04.15.
 */
public class DatabaseProvider extends ContentProvider {
    DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int match = Descriptor.URI_MATCHER.match(uri);

        switch (match) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                // Set the table
//                checkColumns(projection);
                queryBuilder.setTables(ArticleTable.TABLE_NAME);
                break;
            case Descriptor.ArticleDescriptor.PATH_FOR_ID_TOKEN:
                // adding the ID to the original query
                queryBuilder.setTables(ArticleTable.TABLE_NAME);
//                checkColumns(projection);
                String where = ArticleTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                queryBuilder.appendWhere(where);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (Descriptor.URI_MATCHER.match(uri)) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                return Descriptor.ArticleDescriptor.CONTENT_TYPE;
            case Descriptor.ArticleDescriptor.PATH_FOR_ID_TOKEN:
                return Descriptor.ArticleDescriptor.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final int match = Descriptor.URI_MATCHER.match(uri);
        String table = null;
        switch (match) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                table = ArticleTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
        long id = db.insert(table, null, values);
        Uri insertedUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertedUri, null);
        return insertedUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final int match = Descriptor.URI_MATCHER.match(uri);

        switch (match) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                int numInserted = 0;
                db.beginTransaction();
                try {

                    for (ContentValues cv : values) {
                        long newID = db.insertOrThrow(ArticleTable.TABLE_NAME, null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into " + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
                return numInserted;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        final int match = Descriptor.URI_MATCHER.match(uri);

        int rowsDeleted = 0;
        switch (match) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                rowsDeleted = sqlDB.delete(ArticleTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case Descriptor.ArticleDescriptor.PATH_FOR_ID_TOKEN:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ArticleTable.TABLE_NAME,
                            ArticleTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ArticleTable.TABLE_NAME,
                            ArticleTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        final int match = Descriptor.URI_MATCHER.match(uri);

        int rowsUpdated = 0;
        switch (match) {
            case Descriptor.ArticleDescriptor.PATH_TOKEN:
                rowsUpdated = sqlDB.update(ArticleTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case Descriptor.ArticleDescriptor.PATH_FOR_ID_TOKEN:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ArticleTable.TABLE_NAME,
                            values,
                            ArticleTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ArticleTable.TABLE_NAME,
                            values,
                            ArticleTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
