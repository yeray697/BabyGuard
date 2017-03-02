package com.phile.babyguard;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.phile.babyguard.interfaces.LoginPresenter;
import com.phile.babyguard.interfaces.LoginView;
import com.phile.babyguard.model.ErrorClass;
import com.phile.babyguard.presenter.LoginPresenterImpl;
import com.phile.babyguard.utils.OneClickListener;

/**
 * Login view
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Login_Activity extends AppCompatActivity implements LoginView, LoginPresenterImpl.OnLoginFinishedListener {
    private static final String FORGOTTEN_PASS_URL = "http://www.google.com";
    private static final String LOGIN_LISTENER = "loginListener";

    private LoginPresenter presenter;

    private TextInputLayout tilUser, tilPassword;
    private EditText etUser, etPassword;
    private TextView tvForgotPass;
    private MaterialRippleLayout btLogin;

    private OneClickListener btLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = (EditText) findViewById(R.id.etUser_login);
        etPassword = (EditText) findViewById(R.id.etPass_login);
        if (savedInstanceState != null){
            btLoginListener = (OneClickListener) savedInstanceState.getSerializable(LOGIN_LISTENER);
        }
        if (btLoginListener == null) {
            btLoginListener = new OneClickListener() {
                @Override
                protected void onOneClick() {
                    login();
                }
            };
        }
        presenter = new LoginPresenterImpl(this, this);
        presenter.isUserSet();
        tilUser = (TextInputLayout) findViewById(R.id.tilUser_login);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPass_login);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        btLogin = (MaterialRippleLayout) findViewById(R.id.rlButton);

        btLogin.setOnClickListener(btLoginListener);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FORGOTTEN_PASS_URL));
                startActivity(browserIntent);
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
    }

    @Override
    public void setMessageError(String messageError, int idView) {
        btLoginListener.setClicked(false);
        switch (idView){
            case R.id.tilUser_login:
                tilUser.setError(messageError);
                break;
            case R.id.tilPass_login:
                tilPassword.setError(messageError);
                break;
            case ErrorClass.VIEW_TOAST:
                Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void setCredentials(String user, String pass) {
        btLoginListener.setClicked(true);
        etUser.setText(user);
        etPassword.setText(pass);
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(Login_Activity.this, KidList_Activity.class);
        startActivity(intent);
        finish();
        btLoginListener.setClicked(false);
    }

    /**
     * Tries to login. Called when user click btLogin or if press Enter on the keyboard while is typing the password
     */
    private void login() {
        presenter.login(etUser.getText().toString(), etPassword.getText().toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LOGIN_LISTENER, btLoginListener);
    }
}
