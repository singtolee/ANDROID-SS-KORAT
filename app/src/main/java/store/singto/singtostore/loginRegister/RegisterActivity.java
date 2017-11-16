package store.singto.singtostore.loginRegister;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.Tools;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerBtn;
    private EditText emailField, passwordField;
    private ImageView returnBtn;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();
    }

    private void setupUI(){

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.isregistering));

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.userEmailRegister);
        passwordField = findViewById(R.id.userPasswordRegister);
        registerBtn = findViewById(R.id.registerBtn);
        returnBtn = findViewById(R.id.returnLoginPageImageViewAsBtn);

        registerBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                //register with email
                registerwithEmail();
                break;
            case R.id.returnLoginPageImageViewAsBtn:
                finish();
                break;
            default:
                break;
        }
    }

    private void registerwithEmail(){
        progressDialog.show();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if(Tools.isEmail(email) && password.length()>5) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        emailField.setError(task.getException().getLocalizedMessage());
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                        finish();
                    }
                }
            });
        }else {
            progressDialog.dismiss();
            if(!Tools.isEmail(email)){
                emailField.setError(getString(R.string.invalidemail));
            }
            if(password.length()<=5){
                passwordField.setError(getString(R.string.passwordatleast6chars));
            }
        }
    }
}
