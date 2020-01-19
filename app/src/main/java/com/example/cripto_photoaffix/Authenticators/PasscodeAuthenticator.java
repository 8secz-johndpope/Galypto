package com.example.cripto_photoaffix.Authenticators;

import android.widget.EditText;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.PasscodeSuccessfulAuthenticationActivityVisitor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.PasscodeUnsuccessfulAuthenticationActivityVisitor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;

public class PasscodeAuthenticator implements Authenticator {
    protected EditText field;

    public PasscodeAuthenticator(EditText field) {
        this.field = field;
    }

    public void initialize() {}

    public boolean canBeUsed() {
        return true;
    }

    public boolean filesReady() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        FilesManager manager = FilesManager.getInstance();

        return manager.exists(activity.getFilesDir() + "/passcodeFinalPassword") &&
                manager.exists(activity.getFilesDir() + "/passcodePassword");
    }

    public void authenticate() {
        String passcode = field.getText().toString();

        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        FilesManager manager = FilesManager.getInstance();

        String hash = manager.getFileContent("passcodePassword");

        ActivityVisitor activityVisitor;

        if (BCrypt.checkpw(passcode, hash))
            activityVisitor = new PasscodeSuccessfulAuthenticationActivityVisitor(this);
        else {
            activityVisitor = new PasscodeUnsuccessfulAuthenticationActivityVisitor();
            field.selectAll();
        }

        activity.accept(activityVisitor);
    }

    public EncryptedFile encrypt(String password) {
        EncryptedFile encryptedPassword = new EncryptedPassword();
        encryptedPassword.encrypt(password, field.getText().toString());

        return encryptedPassword;
    }

    public String decrypt(EncryptedFile file) {
        MyEncryptor encryptor = MyEncryptor.getInstance();

        return encryptor.decrypt(file, field.getText().toString());
    }

    public String getFinalPassword() {
        FilesManager manager = FilesManager.getInstance();

        EncryptedFile finalPassword = manager.restorePassword("passcodeFinalPassword");

        return decrypt(finalPassword);
    }
}
