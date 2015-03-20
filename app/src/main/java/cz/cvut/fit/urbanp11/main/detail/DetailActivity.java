package cz.cvut.fit.urbanp11.main.detail;

/**
 * Created by Petr Urban on 18.03.15.
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.DataStorage;

public class DetailActivity extends Activity {


    public static final String ARTICLE_LIST_ID = "ARTICLE_LIST_ID_KEY";

    String link;

    int articleListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String title = getResources().getString(R.string.placeholder_title);
        link = getResources().getString(R.string.placeholder_link);
        String description = getResources().getString(R.string.placeholder_description);

        if (getIntent().getExtras() != null) {
            articleListId = getIntent().getExtras().getInt(ARTICLE_LIST_ID);
            title = DataStorage.articles.get(articleListId).title;
            link = DataStorage.articles.get(articleListId).link;
            description = DataStorage.articles.get(articleListId).description;
        }

        FragmentManager fm = getFragmentManager();
        DetailFragment myFragment = (DetailFragment)  fm.findFragmentById(R.id.detailfragment);
        myFragment.setLink(link);
        myFragment.setTitle(title);
        myFragment.setDescription(description);
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
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link)+ " " + link);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser)));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}