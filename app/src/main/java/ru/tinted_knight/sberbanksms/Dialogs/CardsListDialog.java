package ru.tinted_knight.sberbanksms.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class CardsListDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//        String[] data = savedInstanceState.getStringArrayList(BundleConstants.CARDS_LIST).toArray(new String[]{});
        String[] data = getArguments().getStringArrayList(BundleConstants.CARDS_LIST).toArray(new String[]{});

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("List of cards")
                .setItems(data, (DialogInterface.OnClickListener) getActivity())
                .setNegativeButton("Close", (DialogInterface.OnClickListener) getActivity());
        return builder.create();
    }

}
