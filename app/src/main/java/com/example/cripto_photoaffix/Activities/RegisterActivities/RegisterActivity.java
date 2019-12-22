package com.example.cripto_photoaffix.Activities.RegisterActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.MyHasher;

public class RegisterActivity extends AppCompatActivity {

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
        System.out.println("PASSSCCCODODEEEE: " + passcode);
        System.out.println(passcode);
        MyHasher myHasher = new MyHasher();

        String password = myHasher.hash(passcode);

        TextFilesManager manager = new TextFilesManager(this);
        String path = getFilesDir() + "/userData";
        manager.createFile(path, "pswrd");

        manager.writeToFile("pswrd", password);

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());
        finish();
    }
}
