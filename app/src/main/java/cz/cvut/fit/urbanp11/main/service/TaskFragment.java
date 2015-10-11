package cz.cvut.fit.urbanp11.main.service;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

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
public class TaskFragment extends Fragment {

    private TaskCallbacks mCallbacks;
    private UselessAsyncTask mTask;

    public static interface TaskCallbacks {
        void onPreExecute();

        void onProgressUpdate(int percent);

        void onCancelled();

        void onPostExecute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    public void executeTask() {
        mTask = new UselessAsyncTask();
        mTask.execute();
    }

    public void cancelTask() {
        mTask.cancel(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class UselessAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        @Override
        protected Void doInBackground(Void... none) {
//            for (int i = 0; !isCancelled() && i < 100; i++) {
//                SystemClock.sleep(100);
//                publishProgress(i);
//            }
            List<String> feeds = new ArrayList<>();

            String[] projection = {FeedTable.COLUMN_ID, FeedTable.COLUMN_LINK};
            Cursor cursor = getActivity().getContentResolver().query(Descriptor.FeedDescriptor.CONTENT_URI, projection, null, null,
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

                    list = PkRSS.with(getActivity()).load(feedLink).get();


                    ListIterator<Article> iterator = list.listIterator(list.size());

                    while (iterator.hasPrevious()) {
                        Article article = iterator.previous();
                        persistArticle(article.getTitle(), article.getDescription(), article.getSource().toString(), 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void persistArticle(String title, String description, String link, int serverId) {

            ContentValues values = new ContentValues();
            values.put(ArticleTable.COLUMN_TITLE, title);
            values.put(ArticleTable.COLUMN_DESCRIPTION, description);
            values.put(ArticleTable.COLUMN_LINK, link);
            values.put(ArticleTable.COLUMN_SERVER_ID, link);
            values.put(ArticleTable.COLUMN_SERVER_ID, serverId);

            getActivity().getContentResolver().insert(Descriptor.ArticleDescriptor.CONTENT_URI, values);
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void none) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
        }
    }
}