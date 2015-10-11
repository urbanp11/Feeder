package cz.cvut.fit.urbanp11.main.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;

/**
 * Created by Petr Urban on 08.04.15.
 */
public class ArticleListAdapter extends CursorAdapter {

    private Long selected;

    public void setSelected(Long selected) {
        this.selected = selected;
    }

    private static class ViewHolder {
        TextView title;
        TextView description;
        View row;
    }

    public ArticleListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, null);
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.description = (TextView) view.findViewById(R.id.description);
        holder.row = view.findViewById(R.id.layout_row);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_TITLE)));
        holder.description.setText(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_DESCRIPTION)));

        if (selected != null && cursor.getLong(cursor.getColumnIndex("_id")) == selected) {
            holder.row.setBackgroundColor(Color.DKGRAY);
        } else {
            holder.row.setBackgroundColor(Color.BLACK);
        }
    }
}
