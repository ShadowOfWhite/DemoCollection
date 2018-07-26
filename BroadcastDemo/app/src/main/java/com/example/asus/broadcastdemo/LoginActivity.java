package com.example.asus.broadcastdemo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText accountText;
    EditText passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountText = findViewById(R.id.Text_Account);
        passwordText = findViewById(R.id.Text_Password);
        loginButton = findViewById(R.id.Button_Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("admin".equals(accountText.getText().toString())&&"admin".equals(passwordText.getText().toString())){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Snackbar.make(v,"密码错误，请重新输入",Snackbar.LENGTH_SHORT)
                            .setAction("重新输入", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    accountText.setText(null);
                                    passwordText.setText(null);
                                }
                            }).show();
                }
            }
        });
    }
}
