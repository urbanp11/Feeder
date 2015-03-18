package cz.cvut.fit.urbanp11.main.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ShareActionProvider;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.detail.DetailActivity;
import cz.cvut.fit.urbanp11.main.detail.DetailFragment;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class MainActivity extends Activity implements MainFragment.OnRowClickListener{

    private ShareActionProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRowClick(int articleListId) {
        Log.i("action", "onRowClick - articleListId = " + articleListId);

        DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailfragment);
        if (fragment != null && fragment.isInLayout()) {
            return;
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    DetailActivity.class);
            intent.putExtra(DetailActivity.ARTICLE_LIST_ID, articleListId);
            startActivity(intent);

        }
    }
}