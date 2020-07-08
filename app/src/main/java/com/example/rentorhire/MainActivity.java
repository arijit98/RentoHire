package com.example.rentorhire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    enum State{
        SIGNUP,LOGIN
    }
    private State mState;
    private EditText mUsername, mPassword;
    private RadioButton mRent1,mRent2,mHire1,mHire2;
    private Button mBtnSignLog,mBtnOneTime;
    private TextView mTxtSignLog,mTxtAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mState=State.LOGIN;
        mUsername=findViewById(R.id.edtUsername);
        mPassword=findViewById(R.id.edtPassword);
        mRent1=findViewById(R.id.radBtnRent);
        mRent2=findViewById(R.id.radBtnRent2);
        mHire1=findViewById(R.id.radBtnHire);
        mHire2=findViewById(R.id.radBtnHire2);
        mBtnOneTime=findViewById(R.id.btnOneTime);
        mBtnSignLog=findViewById(R.id.btnLogin);
        mTxtAccount=findViewById(R.id.txtAccount);
        mTxtSignLog=findViewById(R.id.txtSignUp);

        mTxtSignLog.setOnClickListener(this);
        mBtnSignLog.setOnClickListener(this);
        mBtnOneTime.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
           transitionToHire();


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (mState == State.SIGNUP) {

                    if (mRent1.isChecked() == false && mHire1.isChecked() == false) {
                        Toast.makeText(MainActivity.this, "Do you want to rent a car or hire a car?", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(mUsername.getText().toString());
                    appUser.setPassword(mPassword.getText().toString());
                    if (mRent1.isChecked()) {
                        appUser.put("to", "Rent");

                    } else if (mHire1.isChecked()) {
                        appUser.put("to", "Hire");
                    }
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                                transitionToHire();
//                                transitionToRent;

                            }
                        }
                    });

                } if (mState == State.LOGIN) {


                    ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            if (user != null && e == null) {

                                Toast.makeText(MainActivity.this, "User Logged in", Toast.LENGTH_SHORT).show();

                                transitionToHire();
//                                transitionToRent;
                            }
                        }
                    });

                }
                break;



        case R.id.txtSignUp:

                if (mState == State.SIGNUP) {
                    mState = State.LOGIN;
                    mTxtAccount.setText("Don't have a account?");
                    mTxtSignLog.setText("Sign Up");
                    mBtnSignLog.setText("Log In");
                } else if (mState == State.LOGIN) {

                    mState = State.SIGNUP;
                    mTxtAccount.setText("Already have a account?");
                    mTxtSignLog.setText("Log In");
                    mBtnSignLog.setText("Sign Up");
                }

                break;


        case R.id.btnOneTime:
            if (mRent2.isChecked() == true || mHire2.isChecked() == true) {

                if (ParseUser.getCurrentUser() == null) {
                    ParseAnonymousUtils.logIn(new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {

                                if (mHire2.isChecked()) {
                                    user.put("to", "Hire");

                                } else if (mRent2.isChecked()) {
                                    user.put("to", "Rent");
                                }
                                Toast.makeText(MainActivity.this, "We have an anonymous user", Toast.LENGTH_SHORT).show();
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        transitionToHire();
//                                        transitionToRent();
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(MainActivity.this, "Do you want to rent a car or hire a car?", Toast.LENGTH_SHORT).show();
                return;
            }
            
        break;
    }

    }

    private void transitionToHire(){
        if (ParseUser.getCurrentUser()!=null){
            if(ParseUser.getCurrentUser().get("to").equals("Hire")){
                Intent intent=new Intent(MainActivity.this,Hire.class);
                startActivity(intent);
            }
        }
    }
//    private void transitionToRent(){
//        if (ParseUser.getCurrentUser()!=null){
//            if(ParseUser.getCurrentUser().get("to").equals("Rent")){
//                Intent intent=new Intent(MainActivity.this,Rent.class);
//                startActivity(intent);
//            }
//        }
//    }
}