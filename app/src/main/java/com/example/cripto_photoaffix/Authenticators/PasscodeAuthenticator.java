package com.example.cripto_photoaffix.Authenticators;

public class PasscodeAuthenticator {
    private String filePath;

    public PasscodeAuthenticator(String path) {
        filePath = path;

    }

    public boolean authenticate(Integer passcode) {

        return true;
    }
}
