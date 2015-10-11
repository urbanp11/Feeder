package cz.cvut.fit.urbanp11.main.detail;

/**
 * Created by Petr Urban on 18.03.15.
 */

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;

public class DetailActivity extends ActionBarActivity {


    public static final String ARTICLE_LIST_ID = "ARTICLE_LIST_ID_KEY";

    String link;
    private Uri todoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(25);

        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras.getParcelable(Descriptor.ArticleDescriptor.CONTENT_TYPE_ITEM);

            FragmentManager fm = getFragmentManager();
            DetailFragment myFragment = (DetailFragment) fm.findFragmentById(R.id.detailfragment);
            myFragment.fillData(todoUri);
            link = myFragment.getLink();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link) + " " + link);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser)));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String[] projection = {ArticleTable.COLUMN_ID, ArticleTable.COLUMN_TITLE, ArticleTable.COLUMN_DESCRIPTION};
//        CursorLoader cursorLoader = new CursorLoader(this,
//                Descriptor.ArticleDescriptor.CONTENT_URI, projection, null, null, null);
//        return cursorLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }
}