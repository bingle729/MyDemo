package com.huazun.mydemo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private String userName;
    private TextView hintView;
    private TogglePasswordVisibilityEditText passwordEdit;
    private ProgressBar loadBar;
    private MaterialButton cancelBtn, yesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        hintView = (TextView)findViewById(R.id.resetpwdHintText);
        passwordEdit = (TogglePasswordVisibilityEditText)findViewById(R.id.resetpwdPasswordEdit);
        loadBar = (ProgressBar)findViewById(R.id.resetpwdLoadingBar);
        cancelBtn = (MaterialButton)findViewById(R.id.resetpwdCancelBtn);
        yesBtn = (MaterialButton)findViewById(R.id.resetpwdYesBtn);

        userName = getIntent().getStringExtra("userName");

        initPage();

        cancelBtn.setOnClickListener(this);
        yesBtn.setOnClickListener(this);
    }

    private void initPage(){
        String hintText = getResources().getString(R.string.resetpwd_hint);
        hintText = String.format(hintText, userName);
        hintView.setText(hintText);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetpwdCancelBtn:
                back();
                break;
            case R.id.resetpwdYesBtn:
                resetPwd();
                break;
        }
    }

    private void resetPwd(){
        String password = passwordEdit.getText().toString();
        if(!verify(password))
            return;
        loadBar.setVisibility(View.VISIBLE);
        UserAPI.resetPassword(userName, password, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()){
                    UserLoginResponse userLoginResponse =response.body();
                    if (userLoginResponse.isResponseOK()){
                        //back to Login activity
                        resetSuccess();
                    } else {
                        new SimpleMessageDialog(ResetPasswordActivity.this, "Error", userLoginResponse.getResponseCode().getDesc())
                                .getDialog().show();
                    }
                }
                loadBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                new SimpleMessageDialog(ResetPasswordActivity.this, "Error", t.getMessage())
                        .getDialog().show();
                loadBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void resetSuccess(){
        setResult(Globals.RESULT_OK);
        finish();
    }

    private boolean verify(String password){
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
