package cz.cvut.fit.urbanp11.main.feeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.fit.urbanp11.R;
import cz.cvut.fit.urbanp11.main.data.provider.Descriptor;
import cz.cvut.fit.urbanp11.main.data.tables.FeedTable;


public class AddFeedDialog extends DialogFragment {
    private EditText mFeedUrl;

    /**
     * Shows the {@link AddFeedDialog}.
     *
     * @param fragmentManager The fragment manager.
     */
    public static void show(FragmentManager fragmentManager) {
        String tag = AddFeedDialog.class.getCanonicalName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Look for existing instance of the fragment first, just to be sure.
        Fragment existingDialog = fragmentManager.findFragmentByTag(tag);
        if (existingDialog != null) {
            // If found, remove it.
            transaction.remove(existingDialog);
        }

        // Create and show new dialog.
        AddFeedDialog newDialog = new AddFeedDialog();
        newDialog.show(transaction, tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_feed, null);
        mFeedUrl = (EditText) view.findViewById(R.id.add_feed_url);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_feed_dialog_title);
        builder.setView(view);

        builder.setPositiveButton(R.string.add_feed_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String feedUrl = mFeedUrl.getText().toString();
                if(!feedUrl.isEmpty()) {
                    // Just insert the link to the database, the data will show up on the next refresh.
                    // Note that this should be done on a different thread, but we'll change it in the future
                    // to parse the feed first.
                    ContentValues values = new ContentValues();
                    values.put(FeedTable.COLUMN_LINK, feedUrl);
                    getActivity().getContentResolver().insert(Descriptor.FeedDescriptor.CONTENT_URI, values);
                    Toast.makeText(getActivity(), R.string.add_feed_added, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.add_feed_url_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // No listener is needed, the dialog closes on any button click
        builder.setNegativeButton(R.string.add_feed_cancel, null);

        builder.setCancelable(true);

        return builder.create();
    }
}
