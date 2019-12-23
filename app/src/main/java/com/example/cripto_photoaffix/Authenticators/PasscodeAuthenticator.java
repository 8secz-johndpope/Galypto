package com.example.cripto_photoaffix.Authenticators;

import android.widget.EditText;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Visitors.SuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.UnsuccessfulAuthenticationVisitor;
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

        TextFilesManager manager = new TextFilesManager(activity);

        String hash = manager.getFileContent("pswrd");

        Visitor visitor;

        if (BCrypt.checkpw(passcode, hash))
            visitor = new SuccessfulAuthenticationVisitor();
        else {
            visitor = new UnsuccessfulAuthenticationVisitor();
            field.selectAll();
        }

        activity.accept(visitor);
    }
}
