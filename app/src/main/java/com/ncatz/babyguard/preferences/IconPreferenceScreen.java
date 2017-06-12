/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.utils.Utils;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.setStringPreference;

public class IconPreferenceScreen extends DialogPreference {

    private TextView tvTitle;
    private CircleImageView ivIcon;

    private Drawable mIcon;
    private String title;

    private EditText etDialog;

    // Whether or not the text and icon should be highlighted (as selected)
    private boolean mHighlight;

    public IconPreferenceScreen(Context context) {
        this(context, null);
    }

    public IconPreferenceScreen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconPreferenceScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.preference_icon);
        title = "";
        etDialog = new EditText(context, attrs);

        // Give it an ID so it can be saved/restored
        @IdRes int id = 62467247;
        etDialog.setId(id);

        /*
         * The preference framework and view framework both have an 'enabled'
         * attribute. Most likely, the 'enabled' specified in this XML is for
         * the preference framework, but it was also given to the view framework.
         * We reset the enabled state.
         */
        etDialog.setEnabled(true);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        ivIcon = (CircleImageView) view.findViewById(R.id.icon_preferenceIcon);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle_iconPreference);

        tvTitle.setText(title);
        if (mIcon != null) {
            ivIcon.setImageDrawable(mIcon);
        }

        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPhoto();
            }
        });
        ivIcon.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ivIcon.setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    ivIcon.setColorFilter(Color.argb(0, 0, 0, 0));
                } else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        ivIcon.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void setIcon(@DrawableRes int iconResId) {
        super.setIcon(iconResId);
        mIcon = getContext().getResources().getDrawable(iconResId);
    }

    /**
     * Sets the icon for this Preference with a Drawable.
     *
     * @param icon The icon for this Preference
     */
    public void setIcon(Drawable icon) {
        if (icon != null) {
            mIcon = icon;
            if (ivIcon != null && !icon.equals(mIcon)) {
                ivIcon.setImageDrawable(mIcon);
            }
        }
    }

    /**
     * Returns the icon of this Preference.
     *
     * @return The icon.
     * @see #setIcon(Drawable)
     */
    public Drawable getIcon() {
        return mIcon;
    }

    public void setHighlighted(boolean highlight) {
        mHighlight = highlight;
        notifyChanged();
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title.toString();
        if (tvTitle != null && title != null && !tvTitle.getText().toString().equals(title.toString())) {
            tvTitle.setText(title);
            notifyChanged();
        }
    }

    private void setNewPhoto() {
    }

    @Override
    protected void onBindDialogView(View view) {
        setPersistent(false);
        super.onBindDialogView(view);

        EditText editText = etDialog;
        editText.setText(title);

        ViewParent oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
    }

    /**
     * Adds the EditText widget of this preference to the dialog's view.
     *
     * @param dialogView The dialog view.
     */
    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        @IdRes int id = 63626;
        ViewGroup container = (ViewGroup) dialogView
                .findViewById(id);
        if (container != null) {
            container.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /*@Override
    protected View onCreateDialogView() {
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int marginTopBot = (int) Utils.dpToPx(48);
        int padding = (int) Utils.dpToPx(20);
        params.setMargins(0,marginTopBot,0,marginTopBot);
        view.setLayoutParams(params);
        view.setPadding(padding,padding,padding,padding);
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
    }*/

    @Override
    public void onClick(DialogInterface dialog, int which) {
        boolean validPass = true;
        if (which == DialogInterface.BUTTON_POSITIVE)
        {
            /*String oldPassEt = etOldPass.getText().toString();
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
            }*/
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

        if(positiveResult) {

            /*SharedPreferences.Editor editor = getEditor();
            String passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
            if (FirebaseManager.getInstance().changeUserPassword()) {
                setStringPreference(passKey,etNewPass1.getText().toString());
            }*/
        }
    }
}