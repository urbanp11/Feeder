package cz.cvut.fit.urbanp11.main.main;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.adapters.ArticleListAdapter;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ArticleListAdapter adapter;
    boolean dualPanel;
    private OnRowClickListener listener;

    Long selectedItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fake_list, container, false);
//
//        return view;
//    } // TODO: az (pokud) predelam ListFragment na Fragment

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View detail = getActivity().findViewById(R.id.detailfragment);
        dualPanel = detail != null && detail.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getLong("selectedItemId", -1);
            Log.i("selectedItemId: ", "id: " + selectedItemId);

            if (selectedItemId != -1) focusOnSelectedRow();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ArticleTable.COLUMN_ID, ArticleTable.COLUMN_TITLE, ArticleTable.COLUMN_DESCRIPTION };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                Descriptor.ArticleDescriptor.CONTENT_URI, projection, null, null, ArticleTable.COLUMN_ID + " DESC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data); // changeCUrsor (proc ne swapCursor?)
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public interface OnRowClickListener {
        public void onRowClick(long listId);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        selectedItemId = id;
        Log.i("selectedItemId", "selectedItemId: " + selectedItemId);
        focusOnSelectedRow();
    }

    private void focusOnSelectedRow() {
        listener.onRowClick(selectedItemId);

        if (dualPanel && selectedItemId != null) { // pokud je dualni panel, je treba oznacit pozici v listu
            adapter.setSelected(selectedItemId);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedItemId != null) outState.putLong("selectedItemId", selectedItemId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnRowClickListener) {
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
        adapter = new ArticleListAdapter(getActivity(), null, 0);
        setListAdapter(adapter);
    }
}
