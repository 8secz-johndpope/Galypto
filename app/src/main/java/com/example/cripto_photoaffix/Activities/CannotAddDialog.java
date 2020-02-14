package com.example.cripto_photoaffix.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CannotAddDialog extends AppCompatDialogFragment {
    private int cantFiles;

    public CannotAddDialog(int cant) {
        cantFiles = cant;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (cantFiles > 1) {
            builder.setTitle("Cannot add " + cantFiles + " files.")
                    .setMessage("These files are heavier than expected.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        else {
            builder.setTitle("Cannot add 1 file.")
                    .setMessage("The file is heavier than expected.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        return builder.create();
    }
}
