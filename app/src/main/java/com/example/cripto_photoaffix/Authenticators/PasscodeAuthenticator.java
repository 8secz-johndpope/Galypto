package com.example.cripto_photoaffix.Authenticators;

import android.content.Context;

import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.Security.BCrypt;

public class PasscodeAuthenticator {
    private Context context;

    public PasscodeAuthenticator(Context context) {
        this.context = context;
    }

    public boolean authenticate(String passcode) {
        TextFilesManager manager = new TextFilesManager(context);

        String hash = manager.getFileContent("pswrd");

        return BCrypt.checkpw(passcode, hash);
    }
}
