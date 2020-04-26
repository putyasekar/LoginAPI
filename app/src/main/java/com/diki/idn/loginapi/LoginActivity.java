package com.diki.idn.loginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diki.idn.loginapi.model.ResponseLogin;
import com.diki.idn.loginapi.network.InterfaceClient;
import com.diki.idn.loginapi.network.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    Button btnLogin;
    ProgressDialog progressDialog;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        if (sharedPreferencesManager.getSpSigned()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        progressDialog = new ProgressDialog(this);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageEmail = "Email tidak boleh kosong";
                String messagePassword = "Password tidak boleh kosong";
                progressDialog.setMessage("Loading..");
                progressDialog.show();

                if (etEmail.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, messageEmail, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPass.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, messagePassword, Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = etEmail.getText().toString().trim();
                String pass = etPass.getText().toString().trim();

                InterfaceClient interfaceClient = RetrofitConfig.createService(InterfaceClient.class);
                Call<ResponseLogin> requestLogin = interfaceClient.loginSiswa("siswa", "login", email, pass);

                requestLogin.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        progressDialog.dismiss();
                        if (response.body().getHasil().equals("success")) {
                            sharedPreferencesManager.saveBoolean(SharedPreferencesManager.SP_SIGNED, true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
