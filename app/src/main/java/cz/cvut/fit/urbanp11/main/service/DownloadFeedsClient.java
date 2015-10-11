package cz.cvut.fit.urbanp11.main.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.PkRSS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;

/**
 * Created by Petr Urban on 06.05.15.
 */
public class DownloadFeedsClient {

    public interface Downloadable {
        public void onStart();
        public void onFinish();
    }

    public static void download(Context context, Downloadable downloadable) {
        downloadable.onStart();

        List<String> feeds = new ArrayList<>();

        String[] projection = {FeedTable.COLUMN_ID, FeedTable.COLUMN_LINK};
        Cursor cursor = context.getContentResolver().query(Descriptor.FeedDescriptor.CONTENT_URI, projection, null, null,
                null);
        if (cursor != null) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String link = cursor.getString(cursor.getColumnIndexOrThrow(FeedTable.COLUMN_LINK));
                feeds.add(link);
                cursor.moveToNext();
            }

            cursor.close();
        }

        List<Article> list;

        try {
            for (String feedLink : feeds) {
                list = PkRSS.with(context).load(feedLink).get();

                ListIterator<Article> iterator = list.listIterator(list.size());

                while (iterator.hasPrevious()) {
                    Article article = iterator.previous();
                    persistArticle(context, article.getTitle(), article.getDescription(), article.getSource().toString(), 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadable.onFinish();
    }

    private static void persistArticle(Context context, String title, String description, String link, int serverId) {

        ContentValues values = new ContentValues();
        values.put(ArticleTable.COLUMN_TITLE, title);
        values.put(ArticleTable.COLUMN_DESCRIPTION, description);
        values.put(ArticleTable.COLUMN_LINK, link);
        values.put(ArticleTable.COLUMN_SERVER_ID, link);
        values.put(ArticleTable.COLUMN_SERVER_ID, serverId);

        context.getContentResolver().insert(Descriptor.ArticleDescriptor.CONTENT_URI, values);
    }
}
