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
            builder.setTitle("No se puede añadir " + cantFiles + " archivos.")
                    .setMessage("Los archivos son mas pesados de lo esperado.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        else {
            builder.setTitle("No se puede añadir 1 archivo.")
                    .setMessage("El archivo es mas pesado de lo esperado.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        return builder.create();
    }
}
