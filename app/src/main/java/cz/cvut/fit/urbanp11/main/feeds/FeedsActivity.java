package cz.cvut.fit.urbanp11.main.feeds;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.provider.DatabaseProvider;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;
import cz.cvut.fit.urbanp11.main.main.MainFragment;

/**
 * Created by Petr Urban on 08.04.15.
 */
public class FeedsActivity extends ActionBarActivity implements FeedsFragment.OnRowClickListener  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar_feeds);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(25);
    }

    @Override
    public void onRowClick(long listId) {
        Log.i("listId: ", listId + " ");
        Uri todoUri = Uri.parse(Descriptor.FeedDescriptor.CONTENT_URI + "/" + listId);
        getContentResolver().delete(todoUri, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_feed) {

//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link) + " " + link);
//            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser)));

//            ContentValues values = new ContentValues();
//            values.put(FeedTable.COLUMN_LINK, "http://servis.idnes.cz/rss.aspx?c=technet");
//
//            getContentResolver().insert(Descriptor.FeedDescriptor.CONTENT_URI, values);
            AddFeedDialog.show(getFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
