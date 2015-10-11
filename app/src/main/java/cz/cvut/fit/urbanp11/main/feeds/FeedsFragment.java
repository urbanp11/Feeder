package cz.cvut.fit.urbanp11.main.feeds;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cz.cvut.fit.urbanp11.main.adapters.FeedListAdapter;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;

/**
 * Created by Petr Urban on 08.04.15.
 */
public class FeedsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private FeedListAdapter adapter;
    private OnRowClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listener.onRowClick(id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {FeedTable.COLUMN_ID, FeedTable.COLUMN_LINK};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                Descriptor.FeedDescriptor.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public interface OnRowClickListener {
        public void onRowClick(long listId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnRowClickListener) {
            listener = (OnRowClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement MainFragment.OnRowClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void loadData() {
        getLoaderManager().initLoader(0, null, this);
        adapter = new FeedListAdapter(getActivity(), null, 0);
        setListAdapter(adapter);
    }
}
