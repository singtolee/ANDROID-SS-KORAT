package store.singto.singtostore.MeTab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import store.singto.singtostore.R;

import static store.singto.singtostore.TOOLS.Tools.tintMenuIcon;

public class EditAvatarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView userAvatar;
    private Button btn;


    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        toolbar = findViewById(R.id.editavatartoolbar);
        toolbar.setTitle(R.string.editavatar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        userAvatar = findViewById(R.id.biguseravatar);
        userAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        btn = findViewById(R.id.updateuseravatarbtn);
        btn.setVisibility(View.INVISIBLE);



    }

    //show open album icon


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opengalleryicon, menu);
        MenuItem menuItem = menu.findItem(R.id.opengallery);
        if(menuItem!=null){
            tintMenuIcon(this, menuItem, R.color.colorAccent);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.xml.slide_from_left, R.xml.slide_to_right);
            return true;
        }
        if(item.getItemId() == R.id.opengallery){
            userAvatar.setImageDrawable(null);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.slide_from_left,R.xml.slide_to_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AVATAR", "ON START");
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Picasso.with(this).load(user.getPhotoUrl()).error(R.mipmap.ic_useravatar).into(userAvatar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AVATAR", "ON RESUME");
    }
}
