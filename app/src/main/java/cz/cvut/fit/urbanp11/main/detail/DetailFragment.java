package cz.cvut.fit.urbanp11.main.detail;

/**
 * Created by Petr Urban on 18.03.15.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.fit.urbanp11.R;

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    public void setViews(String title, String link, String description){
        ((TextView) getActivity().findViewById(R.id.title)).setText(title);
        ((TextView) getActivity().findViewById(R.id.link)).setText(link);
        ((TextView) getActivity().findViewById(R.id.description)).setText(description);
    }

}