package com.example.cripto_photoaffix.Authenticators;

import android.content.Context;

import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.Security.MyHasher;

public class PasscodeAuthenticator {
    private Context context;

    public PasscodeAuthenticator(Context context) {
        this.context = context;
    }

    public boolean authenticate(String passcode) {
        System.out.println("Passcode: " + passcode);
        MyHasher myHasher = new MyHasher();

        TextFilesManager manager = new TextFilesManager(context);

        String hash = manager.getFileContent("pswrd");
        System.out.println(hash);
        System.out.println(myHasher.match(hash, passcode));

        return myHasher.match(hash, passcode);
    }
}
