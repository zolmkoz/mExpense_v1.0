package com.example.tabactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtUs = findViewById(R.id.edtUserName);
        EditText edtPass = findViewById(R.id.edtPassword);
        TextView tvLog = findViewById(R.id.tvLog);
        Button btnLogin = findViewById(R.id.btnLogin);


        tvLog.setText("");
        //User: zolmkoz Password: 183461
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us = String.valueOf(edtUs.getText());
                String pw = String.valueOf(edtPass.getText());
                if (us.equals("zolmkoz") && pw.equals("183461")){
                    tvLog.setText("Login Successfully");
                    Intent i = new Intent(LoginActivity.this,
                            CreateTripActivity.class);
                    startActivity(i);
                } else {
                    tvLog.setText("Login Failure");
                }
            }
        });
    }
}