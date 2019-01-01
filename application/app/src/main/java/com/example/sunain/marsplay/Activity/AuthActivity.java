package com.example.sunain.marsplay.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunain.marsplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.truizlop.fabreveallayout.FABRevealLayout;

import es.dmoral.toasty.Toasty;

public class AuthActivity extends AppCompatActivity {

    EditText login_email,login_pass,signup_email,signup_pass,signup_cpass;
    Button login,signup;
    TextView forget_pass,back_login;
    FABRevealLayout Flayout;
    FloatingActionButton fabutton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        initview();
        initbuttons();
    }
    private void initview()
    {
        login_email=findViewById(R.id.login_email);
        login_pass=findViewById(R.id.login_password);
        signup_email=findViewById(R.id.signup_email);
        signup_pass=findViewById(R.id.signup_password);
        signup_cpass=findViewById(R.id.signup_confirm_password);
        login=findViewById(R.id.login_button);
        signup=findViewById(R.id.signup_button);
        forget_pass=findViewById(R.id.forget_password);
        back_login=findViewById(R.id.back_to_login);
        Flayout=findViewById(R.id.fab_reveal_layout);
        fabutton=findViewById(R.id.fab_reveal_button);
    }
    private void initbuttons()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    login_validation();
                }catch (Exception e)
                {
                 Toasty.error(AuthActivity.this,"Some error occurred",Toast.LENGTH_LONG).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_validation();
            }
        });
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flayout.revealMainView();
            }
        });
    }
    private void signup_validation()
    {
        if(signup_email.getText().toString()==null)
        {
            signup_email.setError("Error in email");
            return;
        }
        if(signup_email.getText().toString().length()==0)
        {
            signup_email.setError("Enter Email");
            return;
        }
        if(!isEmailValid(signup_email.getText().toString()))
        {
            signup_email.setError("Enter valid email");
            return;
        }
        if(signup_pass.getText().toString()==null||signup_cpass.getText().toString()==null)
        {
            signup_pass.setError("Error in password");
            signup_cpass.setError("Error in password");
            return;
        }
        if(signup_pass.getText().toString().length()==0||signup_cpass.getText().toString().length()==0)
        {
            signup_pass.setError("Enter Password");
            signup_cpass.setError("Enter Confirm Password");
            return;
        }
        if(!signup_pass.getText().toString().equals(signup_cpass.getText().toString()))
        {
            signup_cpass.setError("Confirm Password does not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(signup_email.getText().toString(), signup_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN UP", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                            {
//                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                        .setDisplayName(name).build();
//                                user.updateProfile(profileUpdates);
                                FirebaseAuth.getInstance().signOut();
                                Toasty.success(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT, true).show();
                                Flayout.revealMainView();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN UP", "createUserWithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Error in Sign Up", Toast.LENGTH_SHORT, true).show();
            }
        });

    }
    private void login_validation() throws Exception
    {
        if(login_email.getText().toString()==null)
        {
            login_email.setError("Error in email");
            return;
        }
        if(login_email.getText().toString().length()==0)
        {
            login_email.setError("Enter Email");
            return;
        }
        if(!isEmailValid(login_email.getText().toString()))
        {
            login_email.setError("Enter valid email");
            return;
        }
        if(login_pass.getText().toString()==null)
        {
            login_pass.setError("Error in password");
            return;
        }
        if(login_pass.getText().toString().length()==0)
        {
            login_pass.setError("Enter Password");
            return;
        }
        mAuth.signInWithEmailAndPassword(login_email.getText().toString(), login_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOGIN", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent i=new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(i);
                            finish();

                        } else {
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(), "Incorrect email or password",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Authentication Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
