package cz.cvut.fit.urbanp11.main.main;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.PkRSS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;
import cz.cvut.fit.urbanp11.main.detail.DetailActivity;
import cz.cvut.fit.urbanp11.main.detail.DetailFragment;
import cz.cvut.fit.urbanp11.main.feeds.FeedsActivity;
import cz.cvut.fit.urbanp11.main.service.BrReceiver;
import cz.cvut.fit.urbanp11.main.service.TaskFragment;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class MainActivity extends ActionBarActivity implements MainFragment.OnRowClickListener, TaskFragment.TaskCallbacks {

    private static final long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15;
    private ProgressBar progressBar;
//    private TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_dual); // testovani tablet zobrazeni na telefonu

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(25);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);


//        FragmentManager fm = getFragmentManager();
//        mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");
//
//        if (mTaskFragment == null) {
//            mTaskFragment = new TaskFragment();
//            fm.beginTransaction().add(mTaskFragment, "task").commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        boolean dualPanel = findViewById(R.id.detailfragment) != null && findViewById(R.id.detailfragment).getVisibility() == View.VISIBLE;
        if (dualPanel) {
            MenuItem item = menu.findItem(R.id.action_share);
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_feeds) {
            Intent i = new Intent(this, FeedsActivity.class);
            startActivity(i);
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link) + " " + link);
//            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser)));

            return true;
        } else if (id == R.id.action_update) {
            downloadFeeds();
        } else if (id == R.id.action_share) {

            DetailFragment fragment = (DetailFragment) getFragmentManager()
                    .findFragmentById(R.id.detailfragment);

            if (fragment == null || fragment.getLink() == null) {
                Toast.makeText(this, R.string.nothink_to_share, Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link) + " " + fragment.getLink());
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser)));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadFeeds() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent launchIntent = new Intent(MainActivity.this.getApplicationContext(), BrReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this.getApplicationContext(),
                0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DOWNLOAD_INTERVAL,
                pi);
//        String feedUrl = "http://servis.idnes.cz/rss.aspx?c=technet";
////        PkRSS.with(this).load(feedUrl).callback(this).async();
//        AsyncHttpTask asyncHttpTask = new AsyncHttpTask();
//        asyncHttpTask.execute(feedUrl);

        // TADY taha instantne - vyse je to za minutu co kazdou minutu s notifikaci do listy
        FeedDownloader fd = new FeedDownloader();
        fd.execute();
//        mTaskFragment.executeTask();
    }

    @Override
    public void onPreExecute() {
        preLoad();
    }

    @Override
    public void onProgressUpdate(int percent) {
    }

    @Override
    public void onCancelled() {
        loadFailure();
    }

    @Override
    public void onPostExecute() {
        loadSuccess();
    }

    private class FeedDownloader extends AsyncTask<Void, Integer, Integer> {

        List<Article> list;

        public FeedDownloader() {
        }

        @Override
        protected Integer doInBackground(Void... params) {

            List<String> feeds = new ArrayList<>();

            String[] projection = {FeedTable.COLUMN_ID, FeedTable.COLUMN_LINK};
            Cursor cursor = getContentResolver().query(Descriptor.FeedDescriptor.CONTENT_URI, projection, null, null,
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

            try {
                for (String feedLink : feeds) {
                    list = PkRSS.with(getApplicationContext()).load(feedLink).get();

                    ListIterator<Article> iterator = list.listIterator(list.size());

                    while (iterator.hasPrevious()) {
                        Article article = iterator.previous();
                        persistArticle(article.getTitle(), article.getDescription(), article.getSource().toString(), 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            preLoad();
        }

        @Override
        protected void onPostExecute(Integer s) {
            if (s != null) {
                loadSuccess();
            } else {
                loadFailure();
            }
        }
    }

    @Override
    public void onRowClick(long articleListId) {
        Log.i("action", "onRowClick - articleListId = " + articleListId);

        DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailfragment);

        Uri todoUri = Uri.parse(Descriptor.ArticleDescriptor.CONTENT_URI + "/" + articleListId);

        if (fragment != null && fragment.isInLayout()) {
            fragment.fillData(todoUri); // naplni pravou stranu - pro tablety
        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(Descriptor.ArticleDescriptor.CONTENT_TYPE_ITEM, todoUri);

            startActivity(i);
        }

    }

    public void preLoad() {
        Log.d("OnPreLoad: ", "OnPreLoad");
        progressBar.setVisibility(View.VISIBLE);
    }

    public void loadSuccess() {
        progressBar.setVisibility(View.GONE);
    }

    public void loadFailure() {
        progressBar.setVisibility(View.GONE);
    }

    private void persistArticle(String title, String description, String link, int serverId) {

        ContentValues values = new ContentValues();
        values.put(ArticleTable.COLUMN_TITLE, title);
        values.put(ArticleTable.COLUMN_DESCRIPTION, description);
        values.put(ArticleTable.COLUMN_LINK, link);
        values.put(ArticleTable.COLUMN_SERVER_ID, link);
        values.put(ArticleTable.COLUMN_SERVER_ID, serverId);

        getContentResolver().insert(Descriptor.ArticleDescriptor.CONTENT_URI, values);
    }
}
