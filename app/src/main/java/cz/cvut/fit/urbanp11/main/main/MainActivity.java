package cz.cvut.fit.urbanp11.main.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ShareActionProvider;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.DataStorage;
import cz.cvut.fit.urbanp11.main.detail.DetailActivity;
import cz.cvut.fit.urbanp11.main.detail.DetailFragment;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class MainActivity extends ActionBarActivity implements MainFragment.OnRowClickListener{

    private ShareActionProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(25);
    }

    @Override
    public void onRowClick(int articleListId) {
        Log.i("action", "onRowClick - articleListId = " + articleListId);

        DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailfragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setTitle(DataStorage.articles.get(articleListId).title);
            fragment.setLink(DataStorage.articles.get(articleListId).link);
            fragment.setDescription(DataStorage.articles.get(articleListId).description);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    DetailActivity.class);
            intent.putExtra(DetailActivity.ARTICLE_LIST_ID, articleListId);
            startActivity(intent);
        }

    }
}
