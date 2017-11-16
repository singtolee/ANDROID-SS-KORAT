package store.singto.singtostore.loginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Map;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.SaveEmailPassword;
import store.singto.singtostore.TOOLS.Tools;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //sharedpreference
    private SaveEmailPassword ep;
    private Context context;

    private FirebaseAuth mAuth;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button fbLoginBtn, gotoRegisterBtn, loginwithEmailBtn;
    private ImageView returnBtn;
    private TextView gotoResetPasswordBtn;

    private EditText emailField, passwordField;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        ep = new SaveEmailPassword(context);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.islogin));

        mAuth = FirebaseAuth.getInstance();
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //facebook login success, exchange credential with firebase;
                progressDialog.show();
                exchangeCredential(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.facebooklogincancelled, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                    finish();
                }
            };

        setupUI();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, String> data = ep.read();
        emailField.setText(data.get("email"));
        passwordField.setText(data.get("password"));

        Log.d("LOGIN", "SHOW ME");

        //if user already login, call finish();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void exchangeCredential(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //login firebase failed
                    progressDialog.dismiss();
                }else {
                    //login firebase success
                    progressDialog.dismiss();
                    finish();
                }
            }
        });
    }

    private void setupUI(){

        emailField = findViewById(R.id.userEmailEditText);
        passwordField = findViewById(R.id.userPasswordEditText);

        fbLoginBtn = findViewById(R.id.loginwithfacebookBtn);
        returnBtn = findViewById(R.id.returnImgAsBtn);
        gotoRegisterBtn = findViewById(R.id.gotoregisterBtn);
        loginwithEmailBtn = findViewById(R.id.loginWithEmailBtn);
        gotoResetPasswordBtn = findViewById(R.id.forgetpasswordTextViewAsBtn);
        //click listener
        fbLoginBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
        gotoRegisterBtn.setOnClickListener(this);
        loginwithEmailBtn.setOnClickListener(this);
        gotoResetPasswordBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginwithfacebookBtn:
                loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                break;
            case R.id.returnImgAsBtn:
                finish();
                break;
            case R.id.gotoregisterBtn:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.loginWithEmailBtn:
                //login with email and password
                loginwithemail();
                break;
            case R.id.forgetpasswordTextViewAsBtn:
                //go to reset password
                Intent intent1 = new Intent(this,ResetPasswordActivity.class);
                startActivity(intent1);
                break;
            default:
                break;

        }
    }

    private void loginwithemail(){
        progressDialog.show();
        final String email = emailField.getText().toString().trim();
        final String password = passwordField.getText().toString().trim();

        if(Tools.isEmail(email) && !password.isEmpty()) {
            //email and password are ok
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //sign in success
                        progressDialog.dismiss();
                        ep.save(email, password);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        String err = task.getException().getLocalizedMessage();
                        Toast.makeText(LoginActivity.this, err, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            progressDialog.dismiss();
            if(!Tools.isEmail(email)){
                emailField.setError(getString(R.string.invalidemail));
                emailField.requestFocus();
            }
            if(password.isEmpty()){
                passwordField.setError(getString(R.string.passwordisempty));
                passwordField.requestFocus();
            }
        }
    }


}
