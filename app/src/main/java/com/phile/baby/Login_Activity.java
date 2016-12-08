package com.phile.baby;

import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Activity extends AppCompatActivity {
    EditText etPassword;
    FloatingActionButton fabGoogle, fabFacebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fabGoogle = (FloatingActionButton) findViewById(R.id.fabGoogle);
        fabFacebook = (FloatingActionButton) findViewById(R.id.fabFacebook);
        etPassword = (EditText) findViewById(R.id.etPass_login);
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

    }
}
