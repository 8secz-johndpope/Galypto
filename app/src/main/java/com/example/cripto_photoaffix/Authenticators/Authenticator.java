package com.example.cripto_photoaffix.Authenticators;

import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;

public interface Authenticator {

    public void authenticate();

    public void initialize();

    public boolean canBeUsed();

    public EncryptedFile encrypt(String data);

    public String decrypt(EncryptedFile file);

    public String getFinalPassword();

    public boolean filesReady();
}
