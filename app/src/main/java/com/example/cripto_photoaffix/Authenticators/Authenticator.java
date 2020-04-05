package com.example.cripto_photoaffix.Authenticators;

import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;

public interface Authenticator {

    /**
     * When trying to authenticate.
     */
    public void authenticate();

    /**
     * Initializes the authenticator.
     */
    public void initialize();

    /**
     * Determines whether the phone can use this authenticator.
     * @return True if the phone can and false if it can't.
     */
    public boolean canBeUsed();

    /**
     * Encrypts the given data.
     * @param data Data to encrypt.
     * @return EncryptedFile with the data.
     */
    public EncryptedFile encrypt(String data);

    /**
     * Decrypts the EncryptedFile give. The user must be previously authenticated in order to be
     * able to decrcypt the file.
     * @param file EncryptedFile to decrpyt.
     * @return String Decrypted data.
     */
    public String decrypt(EncryptedFile file);

    /**
     * Returns the "universal password" associated with the authenticator.
     * @return Universal password.
     */
    public String getFinalPassword();

    /**
     * Determines whether the needed files to work are ready.
     * @return True if they are, False on the contrary.
     */
    public boolean filesReady();
}
