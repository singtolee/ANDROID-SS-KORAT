
package store.singto.singtostore.MeTab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.RoundedImg;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth auth;
    private RelativeLayout editavatar, editname;
    private ImageView smalluseravatar;
    private TextView smallusername;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupview();

    }

    void setupview(){

        toolbar = findViewById(R.id.userprofiletoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();
        editavatar = findViewById(R.id.editUserAvatar);
        editavatar.setOnClickListener(this);
        editname = findViewById(R.id.editUserName);
        editname.setOnClickListener(this);

        smalluseravatar = findViewById(R.id.smalluseravater);
        smallusername = findViewById(R.id.smallusername);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editUserAvatar:
                //change profile photo
                break;
            case R.id.editUserName:
                //change name
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null ){
            smallusername.setText(auth.getCurrentUser().getDisplayName());
            Picasso.with(this).load(auth.getCurrentUser().getPhotoUrl()).transform(new RoundedImg()).error(R.mipmap.ic_useravatar).into(smalluseravatar);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.slide_from_left,R.xml.slide_to_right);
    }
}
