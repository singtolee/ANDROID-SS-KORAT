package store.singto.singtostore.loginRegister;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.Tools;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView returnBtn;
    private Button sendemailBtn;
    private EditText emailField;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        returnBtn = findViewById(R.id.returntologinpageImageViewAsBtn);
        emailField = findViewById(R.id.emailtoresetpasswordEditText);
        sendemailBtn = findViewById(R.id.resetpasswordBtn);
        progressDialog = new ProgressDialog(ResetPasswordActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.submitting));

        returnBtn.setOnClickListener(this);
        sendemailBtn.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        coordinatorLayout = findViewById(R.id.resetpasswordactivity);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.returntologinpageImageViewAsBtn:
                finish();
                break;
            case R.id.resetpasswordBtn:
                //send email
                sendresetpasswordemail();
                break;
            default:
                break;
        }
    }

    private void sendresetpasswordemail(){
        progressDialog.show();
        String email = emailField.getText().toString().trim();
        if(Tools.isEmail(email)){
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        //user Snackbar to show msg
                        Snackbar.make(coordinatorLayout, getString(R.string.resetpasswordemailsended),Snackbar.LENGTH_LONG).setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();

                    }else {
                        emailField.setError(task.getException().getLocalizedMessage());
                    }
                }
            });
        }else {
            progressDialog.dismiss();
            emailField.setError(getString(R.string.invalidemail));
        }
    }
}
