package com.example.cripto_photoaffix.Activities.RegisterActivities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Visitors.Visitor;

public class RegisterActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText field = findViewById(R.id.passcode);

        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    createUserData();

                return true;
            }
        });
    }

    public void createUserData() {
        EditText field = findViewById(R.id.passcode);
        String passcode = field.getText().toString();

        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(passcode, salt);

        FilesManager manager = new FilesManager(this);
        String path = getFilesDir() + "/userData";
        manager.createFile(path, "pswrd");

        manager.writeToFile("pswrd", password);

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());
        finish();
    }

    public void accept(Visitor visitor) {}
}
