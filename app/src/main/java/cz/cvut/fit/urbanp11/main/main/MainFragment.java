package cz.cvut.fit.urbanp11.main.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.DataStorage;
import cz.cvut.fit.urbanp11.main.data.model.Article;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class MainFragment extends Fragment {

    private OnRowClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fake_list, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LinearLayout listView = (LinearLayout) getActivity().findViewById(R.id.fakeListLayout);

        int articleListId = 0;
        for(final Article article : DataStorage.articles){
            View fakeRow = getActivity().getLayoutInflater().inflate(R.layout.row, listView, false);
            TextView title = (TextView) fakeRow.findViewById(R.id.title);
            title.setText(article.title);
            TextView des = (TextView) fakeRow.findViewById(R.id.description);
            des.setText(article.description);
            final int finalArticleListId = articleListId;
            fakeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRowClick(finalArticleListId);
                }
            });
            listView.addView(fakeRow);
            articleListId ++;
        }
    }

    public interface OnRowClickListener {
        public void onRowClick(int listId);
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
}
