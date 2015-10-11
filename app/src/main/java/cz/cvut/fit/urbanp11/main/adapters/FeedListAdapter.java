package cz.cvut.fit.urbanp11.main.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;

/**
 * Created by Petr Urban on 08.04.15.
 */
public class FeedListAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView title;
    }

    public FeedListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_feed, null);
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.link);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(cursor.getColumnIndex(FeedTable.COLUMN_LINK)));
    }
}
