package com.example.cripto_photoaffix.Authenticators;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedPassword;
import com.example.cripto_photoaffix.Visitors.FingerprintSuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.FingerprintUnsuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.Visitor;

import java.security.KeyStore;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class FingerprintAuthenticator extends Authenticator {
    protected BiometricPrompt.PromptInfo promptInfo;

    public FingerprintAuthenticator(MyActivity activity) {
        super(activity);

        if (canBeUsed())
            initialize();
    }

    public void authenticate() {}

    public boolean canBeUsed() {
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(activity);

        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public void initialize() {
        Executor executor = ContextCompat.getMainExecutor(activity);
        initializePromptInfo();
        BiometricPrompt prompt = new BiometricPrompt(activity, executor, new MyAuthenticatorCallback());
        prompt.authenticate(promptInfo);
    }

    public EncryptedFile encrypt(String data) {
        EncryptedFile res = null;

        try {
            SecretKey secretKey = getSecretKey();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] iv = cipher.getIV();
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));

            res  = new EncryptedPassword();
            res.setIV(iv);
            res.setData(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public String decrypt(EncryptedFile file) {
        String res = "";
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry("Crypto-PhotoAffix", null);

            SecretKey secretKey = entry.getSecretKey();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, file.getIV());

            cipher.init(Cipher.DECRYPT_MODE, secretKey,spec);

            byte[] decodedData = cipher.doFinal(file.getData());

            res = new String(decodedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getFinalPassword() {
        FilesManager manager = new FilesManager(activity);

        EncryptedFile finalPassword = manager.restorePassword("fingerprintFinalPassword");

        return decrypt(finalPassword);
    }

    protected void initializePromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Touch the fingerprint sensor")
                .setSubtitle("Log in using your fingerprint.").setNegativeButtonText("Use passcode")
                .build();
    }

    class MyAuthenticatorCallback extends BiometricPrompt.AuthenticationCallback {

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Visitor visitor = new FingerprintUnsuccessfulAuthenticationVisitor();
            activity.accept(visitor);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            Visitor visitor = new FingerprintSuccessfulAuthenticationVisitor(FingerprintAuthenticator.this);
            activity.accept(visitor);
        }
    }

    protected Cipher getCryptoCipher() {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES, "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipher;
    }

    protected SecretKey getSecretKey() {
        SecretKey secretKey = null;

        KeyGenerator keygen = null;
        KeyGenParameterSpec specs = null;
        try {
            keygen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            specs = new KeyGenParameterSpec.Builder("Crypto-PhotoAffix",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();

            keygen.init(specs);
            secretKey = keygen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey;
    }
}
