package cz.cvut.fit.urbanp11.main.detail;

/**
 * Created by Petr Urban on 18.03.15.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.regex.Pattern;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.tables.ArticleTable;

public class DetailFragment extends Fragment {
    private String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setLink(Uri uri) {
//        String url = uri.getAuthority() + uri.getPath();
        String url = uri.toString();
        TextView view = ((TextView) getActivity().findViewById(R.id.link_detail));
        Pattern pattern = Pattern.compile(url);
        Linkify.addLinks(view, pattern, "http://");
        view.setText(Html.fromHtml("<a href='http://" + url + "'>" + url + "</a>"));
    }

    private void setDescription(String description) {
        ((TextView) getActivity().findViewById(R.id.description_detail)).setText(description);
    }

    private void setTitle(String title) {
        ((TextView) getActivity().findViewById(R.id.title_detail)).setText(title);
    }

    public void fillData(Uri uri) {
        String[] projection = {ArticleTable.COLUMN_ID,
                ArticleTable.COLUMN_TITLE, ArticleTable.COLUMN_DESCRIPTION, ArticleTable.COLUMN_LINK};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            String title = cursor.getString(cursor.getColumnIndexOrThrow(ArticleTable.COLUMN_TITLE));
            link = cursor.getString(cursor.getColumnIndexOrThrow(ArticleTable.COLUMN_LINK));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(ArticleTable.COLUMN_DESCRIPTION));

            cursor.close();

            FragmentManager fm = getFragmentManager();
            DetailFragment myFragment = (DetailFragment) fm.findFragmentById(R.id.detailfragment);

            Uri uriLink = Uri.parse(link);

            myFragment.setLink(uriLink);
            myFragment.setTitle(title);
            myFragment.setDescription(description);
        }
    }

    public String getLink() {
        return link;
    }

}