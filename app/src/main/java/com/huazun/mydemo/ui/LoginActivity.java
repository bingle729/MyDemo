package com.huazun.mydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.huazun.mydemo.Globals;
import com.huazun.mydemo.R;
import com.huazun.mydemo.model.user.User;
import com.huazun.mydemo.server.user.UserAPI;
import com.huazun.mydemo.server.user.UserLoginResponse;
import com.huazun.mydemo.ui.views.SimpleMessageDialog;
import com.huazun.mydemo.ui.views.TogglePasswordVisibilityEditText;
import com.huazun.mydemo.utilities.PackageUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nameEdit;
    private TogglePasswordVisibilityEditText passwordEdit;
    private TextView forgotText, versionText;
    private MaterialButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameEdit = (EditText)findViewById(R.id.loginNameEdit);
        passwordEdit = (TogglePasswordVisibilityEditText)findViewById(R.id.loginPasswordEdit);
        forgotText = (TextView)findViewById(R.id.loginForgotPwdView);
        versionText = (TextView)findViewById(R.id.loginVersionText);
        loginBtn = (MaterialButton)findViewById(R.id.loginLoginBtn);

        initPage();

        forgotText.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    private void initPage(){
        versionText.setText("Version: " +PackageUtils.getVersionName(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginForgotPwdView:
                forgotPwd();
                break;
            case R.id.loginLoginBtn:
                login();
                break;
        }
    }

    private void forgotPwd(){
        //to forgot password page
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        intent.putExtra("userName", nameEdit.getText().toString());
        startActivity(intent);
    }

    private void login(){
        String name = nameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if(!verify(name, password))
            return;
        UserAPI.userLogin(name, password, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()){
                    UserLoginResponse userLoginResponse =response.body();
                    if (userLoginResponse.isResponseOK()){
                        User user = userLoginResponse.getUserInfo();
                        Toast.makeText(LoginActivity.this, "userName = "+ user.getUserName() + ", email = " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else if (userLoginResponse.isFirstLogin()){
                        User user = userLoginResponse.getUserInfo();
                        //to New Password Activity
                        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("userName", user.getUserName());
                        startActivityForResult(intent, Globals.REQUEST_CODE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean verify(String name, String password){
        if (name == null || name.length() == 0){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialogStyle);
//            builder.setTitle("Error")
//                    .setMessage("Name shouldn't be blank.")
//                    .setPositiveButton("OK", null)
//                    .show();
            new SimpleMessageDialog(this, "Error", getResources().getString(R.string.empty_username_warn))
                    .getDialog().show();
            return false;
        }
        if (password == null || password.length() == 0){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialogStyle);
//            builder.setTitle("Error")
//                    .setMessage("Password shouldn't be blank.")
//                    .setPositiveButton("OK", null)
//                    .show();
            new SimpleMessageDialog(this, "Error", getResources().getString(R.string.empty_password_warn))
                .getDialog().show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Globals.REQUEST_CODE) {
            if (resultCode == Globals.RESULT_OK) {
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}
