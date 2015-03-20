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
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setLink(String link) {
        ((TextView) getActivity().findViewById(R.id.link_detail)).setText(link);
    }

    public void setDescription(String description) {
        ((TextView) getActivity().findViewById(R.id.description_detail)).setText(description);
    }

    public void setTitle(String title) {
        ((TextView) getActivity().findViewById(R.id.title_detail)).setText(title);
    }

}