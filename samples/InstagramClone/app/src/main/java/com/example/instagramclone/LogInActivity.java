package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    // Initialize Ui variables
    private EditText edtLogInEmail, edtLogInPassword;
    private Button btnLoginLogin, btnLoginSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize UI components with variables
        setTitle("Log In");
        edtLogInEmail = findViewById(R.id.edtLoginEmail);
        edtLogInPassword = findViewById(R.id.edtLoginPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginSignUp = findViewById(R.id.btnLoginSignup);

        //Set actions for clicking UI buttons
        btnLoginLogin.setOnClickListener(LogInActivity.this);
        btnLoginSignUp.setOnClickListener(LogInActivity.this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnLoginLogin:
                if (edtLogInEmail.getText().toString().equals("") || edtLogInPassword.getText().toString().equals("")){

                    FancyToast.makeText(LogInActivity.this,"Email and Password required!",
                            Toast.LENGTH_LONG, FancyToast.INFO, false).show();

                } else {

                    ParseUser.logInInBackground(edtLogInEmail.getText().toString(),
                            edtLogInPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (user != null && e == null) {
                                        FancyToast.makeText(LogInActivity.this, user.getUsername() + " is logged in successfully!",
                                                Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                        transitionToSocialMediaActivity();

                                    } else {
                                        FancyToast.makeText(LogInActivity.this, "There was an error: " + e.getMessage(),
                                                Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                    }
                                }
                            });
                }
                break;

            case R.id.btnLoginSignup:
                Intent intent = new Intent(LogInActivity.this, SignUp.class);
                startActivity(intent);
                break;
        }
    }

    public void rootLIActivityTapped(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LogInActivity.this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}