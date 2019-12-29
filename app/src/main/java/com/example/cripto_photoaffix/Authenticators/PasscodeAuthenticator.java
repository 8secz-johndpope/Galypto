package com.example.cripto_photoaffix.Authenticators;

import android.widget.EditText;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.PasscodeSuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.PasscodeUnsuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.Visitor;

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

        String hash = manager.getFileContent("pswrd");

        Visitor visitor;

        if (BCrypt.checkpw(passcode, hash))
            visitor = new PasscodeSuccessfulAuthenticationVisitor();
        else {
            visitor = new PasscodeUnsuccessfulAuthenticationVisitor();
            field.selectAll();
        }

        activity.accept(visitor);
    }

    public EncryptedFile encrypt(String data) {
        MyEncryptor encryptor = new MyEncryptor();

        return encryptor.encrypt(data, field.getText().toString());
    }
}
