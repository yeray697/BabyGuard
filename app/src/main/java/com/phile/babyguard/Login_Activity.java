package com.phile.babyguard;

import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.phile.babyguard.interfaces.LoginPresenter;
import com.phile.babyguard.interfaces.LoginView;
import com.phile.babyguard.model.ErrorClass;
import com.phile.babyguard.presenter.LoginPresenterImpl;
import com.phile.babyguard.utils.Utils;

public class Login_Activity extends AppCompatActivity implements LoginView, LoginPresenterImpl.OnLoginFinishedListener {
    TextInputLayout tilUser, tilPassword;
    EditText etUser, etPassword;
    TextView tvForgotPass;
    MaterialRippleLayout btLogin;
    LoginPresenter presenter;
    boolean blockedButtons = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenterImpl(this, this);
        tilUser = (TextInputLayout) findViewById(R.id.tilUser_login);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPass_login);
        etUser = (EditText) findViewById(R.id.etUser_login);
        etPassword = (EditText) findViewById(R.id.etPass_login);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        btLogin = (MaterialRippleLayout) findViewById(R.id.rlButton);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //TODO abre una web
                    /*
                    Intent intent = new Intent();
                    startActivity(intent);
                    */
            }
        });
        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilUser.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean result = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    result = true;
                }
                return result;
            }
        });
        presenter.isUserSet();
    }

    private void login() {
        if (!blockedButtons) {
            Utils.hideKeyboard(this);
            blockedButtons = true;
            presenter.login(etUser.getText().toString(), etPassword.getText().toString());
        }
    }


    @Override
    public void setMessageError(String messageError, int idView) {
        blockedButtons = false;
        switch (idView){
            case R.id.tilUser_login:
                tilUser.setError(messageError);
                break;
            case R.id.tilPass_login:
                tilPassword.setError(messageError);
                break;
        }
    }

    @Override
    public void setCredentials(String user, String pass) {
        etUser.setText(user);
        etPassword.setText(pass);
    }

    @Override
    public void onFailure(ErrorClass error) {
        Toast.makeText(this, error.getMessageError(this,error.getCode()), Toast.LENGTH_SHORT).show();
        blockedButtons = false;
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(Login_Activity.this, KidList_Activity.class);
        startActivity(intent);
        finish();
        blockedButtons = false;
    }
}
