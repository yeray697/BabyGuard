package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.utils.Utils;

import java.lang.reflect.Field;

import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;

/**
 * Created by yeray697 on 12/06/17.
 */

public class DialogChangePasswordPreference extends DialogPreference {

    private EditText etOldPass;
    private EditText etNewPass1;
    private EditText etNewPass2;

    public DialogChangePasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        setPersistent(false);
        super.onBindDialogView(view);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int marginTopBot = (int) Utils.dpToPx(48);
        int padding = (int) Utils.dpToPx(20);
        params.setMargins(0, marginTopBot, 0, marginTopBot);
        view.setLayoutParams(params);
        view.setPadding(padding, padding, padding, padding);
        etOldPass = new EditText(getContext());
        etNewPass1 = new EditText(getContext());
        etNewPass2 = new EditText(getContext());

        etOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPass1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPass2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        etOldPass.setHint("Old password");
        etNewPass1.setHint("New password");
        etNewPass2.setHint("Repeat your new password");

        etOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etOldPass.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNewPass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etNewPass1.setError(null);
                etNewPass2.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNewPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etNewPass1.setError(null);
                etNewPass2.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.addView(etOldPass);
        view.addView(etNewPass1);
        view.addView(etNewPass2);
        return view;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        boolean validPass = true;
        if (which == DialogInterface.BUTTON_POSITIVE) {
            String oldPassEt = etOldPass.getText().toString();
            String newPass1Et = etNewPass1.getText().toString();
            String newPass2Et = etNewPass2.getText().toString();
            if (oldPassEt.length() < 1) {
                etOldPass.setError("Rellena todos los campos");
                validPass = false;
            }
            if (newPass1Et.length() < 1) {
                etNewPass1.setError("Rellena todos los campos");
                validPass = false;
            }
            if (newPass2Et.length() < 1) {
                etNewPass2.setError("Rellena todos los campos");
                validPass = false;
            }
            if (validPass) {
                String passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
                String oldPass = getStringPreference(passKey, "");
                if (oldPass.equals("") || oldPass.equals(oldPassEt)) {
                    if (newPass1Et.length() < 6) {
                        etNewPass1.setError("Mínimo 6 caracteres");
                        validPass = false;
                    } else if (newPass1Et.length() > 20) {
                        etNewPass1.setError("Máximo 20 caracteres");
                        validPass = false;
                    } else if (!newPass1Et.equals(etNewPass2.getText().toString())) {
                        etNewPass1.setError("Las contraseñas no coinciden");
                        etNewPass2.setError("Las contraseñas no coinciden");
                        validPass = false;
                    } else { //All fields were validated
                        validPass = true;
                    }

                } else {
                    etOldPass.setError("Contraseña errónea");
                    validPass = false;
                }
            }
        }
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, validPass); //Dismiss the dialog if new pass is okay or user clicks cancel
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
            FirebaseManager.getInstance().changeUserPassword(etNewPass1.getText().toString(), passKey);
        }
    }
}
