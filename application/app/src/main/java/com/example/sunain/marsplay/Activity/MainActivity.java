package com.example.sunain.marsplay.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sunain.marsplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button upload_button,list_button,signout_button;
    TextView tv_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        upload_button=findViewById(R.id.main_upload_button);
        list_button=findViewById(R.id.main_list_button);
        signout_button=findViewById(R.id.main_signout_button);
        tv_id=findViewById(R.id.text_id);
    }


    @Override
    protected void onStart() {
        super.onStart();
        try
        {
            set_button();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            set_button();
        }
        catch (Exception e)
        {

        }
    }

    private void set_button() throws Exception
    {

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            tv_id.setText("Welcome! Guest");
            tv_id.setBackgroundColor(getResources().getColor(R.color.red1));
            signout_button.setVisibility(View.GONE);
            upload_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.red1)
                            .setButtonsColorRes(R.color.colorPrimary)
                            .setIcon(R.drawable.ic_info_outline_white_24dp)
                            .setTitle("Oops! Login Required")
                            .setMessage("To upload a product authentication is required")
                            .setPositiveButton("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    startActivity(new Intent(MainActivity.this,UploadProductActivity.class));
                                    startActivity(new Intent(MainActivity.this,AuthActivity.class));
                                }
                            })
                            .show();
//                            .setNegativeButton(android.R.string.no, null)
                }
            });
            list_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.red1)
                            .setButtonsColorRes(R.color.colorPrimary)
                            .setIcon(R.drawable.ic_info_outline_white_24dp)
                            .setTitle("Oops! Login Required")
                            .setMessage("To check product list authenication is required")
                            .setPositiveButton("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    startActivity(new Intent(MainActivity.this,ProductListActivity.class));
                                    startActivity(new Intent(MainActivity.this,AuthActivity.class));
                                }
                            })
                            .show();
                }
            });
        }
        else
            {
                signout_button.setVisibility(View.VISIBLE);
                tv_id.setText("Welcome!\n"+mAuth.getCurrentUser().getEmail().toString());
                tv_id.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upload_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,UploadProductActivity.class));
                    }
                });
                list_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,ProductListActivity.class));
                    }
                });
                signout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                        finish();
                    }
                });
            }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
