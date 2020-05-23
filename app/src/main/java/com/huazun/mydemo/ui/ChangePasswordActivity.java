package com.huazun.mydemo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.huazun.mydemo.Globals;
import com.huazun.mydemo.R;
import com.huazun.mydemo.server.user.UserAPI;
import com.huazun.mydemo.server.user.UserLoginResponse;
import com.huazun.mydemo.ui.views.SimpleMessageDialog;
import com.huazun.mydemo.ui.views.TogglePasswordVisibilityEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private String userName;
    private TextView hintView;
    private EditText codeEdit;
    private TogglePasswordVisibilityEditText passwordEdit;
    private ProgressBar loadBar;
    private MaterialButton cancelBtn, yesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        hintView = (TextView)findViewById(R.id.changepwdHintText);
        codeEdit = (EditText)findViewById(R.id.changepwdCodeEdit);
        passwordEdit = (TogglePasswordVisibilityEditText)findViewById(R.id.changepwdPasswordEdit);
        loadBar = (ProgressBar)findViewById(R.id.changepwdLoadingBar);
        cancelBtn = (MaterialButton)findViewById(R.id.changepwdCancelBtn);
        yesBtn = (MaterialButton)findViewById(R.id.changepwdYesBtn);

        userName = getIntent().getStringExtra("userName");

        initPage();

        cancelBtn.setOnClickListener(this);
        yesBtn.setOnClickListener(this);
    }

    private void initPage(){
        String hintText = getResources().getString(R.string.changepwd_hint);
        hintText = String.format(hintText, userName);
        hintView.setText(hintText);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changepwdCancelBtn:
                back();
                break;
            case R.id.changepwdYesBtn:
                changePassword();
                break;
        }
    }

    private void changePassword(){
        String code = codeEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if(!verify(code, password))
            return;
        loadBar.setVisibility(View.VISIBLE);
        UserAPI.changePassword(userName, password, code, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()){
                    UserLoginResponse userLoginResponse =response.body();
                    if (userLoginResponse.isResponseOK()){
                        //back to Login activity
                        changeSuccess();
                    } else {
                        new SimpleMessageDialog(ChangePasswordActivity.this, "Error", userLoginResponse.getResponseCode().getDesc())
                                .getDialog().show();
                    }
                }
                loadBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                new SimpleMessageDialog(ChangePasswordActivity.this, "Error", t.getMessage())
                        .getDialog().show();
                loadBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void changeSuccess(){
        setResult(Globals.RESULT_OK);
        finish();
    }

    private boolean verify(String code, String password){
        if (code == null || code.length() == 0){
            new SimpleMessageDialog(this, "Error", getResources().getString(R.string.empty_code_warn))
                    .getDialog().show();
            return false;
        }
        if (password == null || password.length() == 0){
            new SimpleMessageDialog(this, "Error", getResources().getString(R.string.empty_password_warn))
                    .getDialog().show();
            return false;
        }
        return true;
    }

    private void back(){
        finish();
    }
}
