package store.singto.singtostore.MeTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import store.singto.singtostore.R;

public class EditUserNameActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private Toolbar toolbar;
    private Button btn;
    private EditText editText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.updateusernametoolbar);
        toolbar.setTitle(R.string.editusername);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        btn = findViewById(R.id.updateusernamebtn);
        editText = findViewById(R.id.updateusernameedittext);

        Intent intent = getIntent();
        editText.setText(intent.getStringExtra("name"));
        editText.setSingleLine();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.updating));



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn.setOnClickListener(updatename);
    }

    View.OnClickListener updatename = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressDialog.show();
            String newname = editText.getText().toString().trim();
            if(newname.length()>0){
                //save this new name then finish
                FirebaseUser cuser = auth.getCurrentUser();
                if(cuser != null){
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(newname).build();
                    cuser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                //call finish();
                                finish();
                                overridePendingTransition(R.xml.slide_from_left, R.xml.slide_to_right);

                            }else {
                                //edit text set error
                                editText.setError(task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }else {
                progressDialog.dismiss();
                //name at least one char
                editText.setError(getString(R.string.namecannotbeempty));
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.slide_from_left,R.xml.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.xml.slide_from_left, R.xml.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
