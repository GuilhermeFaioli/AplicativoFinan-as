package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mCriarConta;
    private EditText mEmail;
    private Button mEnterButton;
    private EditText mPassword;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mEmail = ((EditText)findViewById(R.id.Editemail));
        this.mPassword = ((EditText)findViewById(R.id.Editpassword));
        this.mEnterButton = ((Button)findViewById(R.id.buttonLogar));
        this.mCriarConta = ((Button)findViewById(R.id.buttonCadastro));
        this.mCriarConta.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent it = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(it);
                finish();
            }
        });


        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            public void onAuthStateChanged(@NonNull FirebaseAuth paramAnonymousFirebaseAuth)
            {
                if (paramAnonymousFirebaseAuth.getCurrentUser() != null)
                {
                    Toast.makeText(MainActivity.this, "Logado!!", Toast.LENGTH_SHORT).show();
                    Intent itent2 = new Intent(MainActivity.this, AfterLoginActivity.class);
                    startActivity(itent2);
                    finish();
                }
            }
        };
        this.mEnterButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        public void onComplete(@NonNull Task<AuthResult> paramAnonymous2Task)
                        {
                            if (paramAnonymous2Task.isSuccessful())
                            {
                                if (MainActivity.this.mAuth.getCurrentUser() != null)
                                {
                                    Intent intent = new Intent(MainActivity.this, AfterLoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Logado!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else{
                                    Toast.makeText(MainActivity.this, "Email ou senha incorreto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Senha ou Password não foi prenchido!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void onStart()
    {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    public String getEmail() {
        return email;
    }
}
