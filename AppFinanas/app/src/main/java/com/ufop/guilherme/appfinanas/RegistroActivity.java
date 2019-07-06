package com.ufop.guilherme.appfinanas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    public static String guardaEmail;
    private FirebaseAuth mAuth;
    private Button mCadastrar;
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.mEmail = ((EditText)findViewById(R.id.Cademail));
        this.mPassword = ((EditText)findViewById(R.id.Cadpassword));
        this.mCadastrar = ((Button)findViewById(R.id.buttonCad));
        this.mAuth = FirebaseAuth.getInstance();
        this.mCadastrar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>()
                {
                    public void onComplete(@NonNull Task<AuthResult> paramAnonymous2Task)
                    {
                        if (paramAnonymous2Task.isSuccessful())
                        {
                            if (RegistroActivity.this.mAuth.getCurrentUser() != null)
                            {
                                Intent it = new Intent(RegistroActivity.this, AfterLoginActivity.class);
                                startActivity(it);
                                Toast.makeText(RegistroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } else{
                            Toast.makeText(RegistroActivity.this, "Falha na criação de conta", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
