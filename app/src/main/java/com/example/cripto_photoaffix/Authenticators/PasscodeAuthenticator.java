package com.example.cripto_photoaffix.Authenticators;

import android.widget.EditText;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.PasscodeSuccessfulAuthenticationActivityVisitor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.PasscodeUnsuccessfulAuthenticationActivityVisitor;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;

public class PasscodeAuthenticator extends Authenticator {
    protected EditText field;

    public PasscodeAuthenticator(MyActivity activity, EditText field) {
        super(activity);

        this.field = field;
    }

    public void initialize() {}

    public boolean canBeUsed() {
        return true;
    }

    public void authenticate() {
        String passcode = field.getText().toString();

        FilesManager manager = new FilesManager(activity);

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
        MyEncryptor encryptor = new MyEncryptor();

        return encryptor.decrypt(file, field.getText().toString());
    }

    public String getFinalPassword() {
        FilesManager manager = new FilesManager(activity);

        EncryptedFile finalPassword = manager.restorePassword("passcodeFinalPassword");

        return decrypt(finalPassword);
    }
}
