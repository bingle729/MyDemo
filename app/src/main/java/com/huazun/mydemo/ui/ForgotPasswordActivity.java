package com.huazun.mydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.huazun.mydemo.R;
import com.huazun.mydemo.server.user.UserAPI;
import com.huazun.mydemo.server.user.UserLoginResponse;
import com.huazun.mydemo.ui.views.SimpleMessageDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private String userName;
    private EditText userEdit;
    private ProgressBar loadBar;
    private MaterialButton cancelBtn, yesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEdit = (EditText)findViewById(R.id.forgotpwdNameEdit);
        loadBar = (ProgressBar)findViewById(R.id.forgotpwdLoadingBar);
        cancelBtn = (MaterialButton)findViewById(R.id.forgotpwdCancelBtn);
        yesBtn = (MaterialButton)findViewById(R.id.forgotpwdYesBtn);


        initPage();

        cancelBtn.setOnClickListener(this);
        yesBtn.setOnClickListener(this);
    }

    private void initPage(){
        userName = getIntent().getStringExtra("userName");
        userEdit.setText(userName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgotpwdCancelBtn:
                finish();
                break;
            case R.id.forgotpwdYesBtn:
                forgotPassword();
                break;
        }
    }

    private void forgotPassword(){
        String userName = userEdit.getText().toString();
        if(!verify(userName))
            return;
        loadBar.setVisibility(View.VISIBLE);

        UserAPI.forgotPassword(userName, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()){
                    UserLoginResponse userLoginResponse =response.body();
                    if (userLoginResponse.isResponseOK()){
                        forgotSuccess();
                    } else {
                        new SimpleMessageDialog(ForgotPasswordActivity.this, "Error", userLoginResponse.getResponseCode().getDesc())
                                .getDialog().show();
                    }
                }
                loadBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                new SimpleMessageDialog(ForgotPasswordActivity.this, "Error", t.getMessage())
                        .getDialog().show();
                loadBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean verify(String userName){
        if (userName == null || userName.length() == 0){
            new SimpleMessageDialog(this, "Error", getResources().getString(R.string.empty_username_warn))
                    .getDialog().show();
            return false;
        }
        return true;
    }

    private void forgotSuccess(){
        Intent intent = new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class);
        intent.putExtra("userName", userEdit.getText().toString());
        startActivity(intent);
        finish();
    }
}
