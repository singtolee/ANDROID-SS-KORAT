package store.singto.singtostore.MeTab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import store.singto.singtostore.R;

import static store.singto.singtostore.TOOLS.Tools.tintMenuIcon;

public class EditAvatarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView userAvatar;
    private Button btn;
    private ProgressBar progressBar;
    private TextView progresshint;


    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference reference;




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
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("USERPROFILEPHOTOS");


        userAvatar = findViewById(R.id.biguseravatar);
        userAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        btn = findViewById(R.id.updateuseravatarbtn);
        btn.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });
        progressBar = findViewById(R.id.uploadavatarprogressbar);
        progressBar.setVisibility(View.INVISIBLE);

        progresshint = findViewById(R.id.progresshint);


        //call only once
        loadCurrentAvatar();



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
            Crop.pickImage(this);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.slide_from_left,R.xml.slide_to_right);
    }

    private void loadCurrentAvatar(){
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Picasso.with(this).load(user.getPhotoUrl()).error(R.mipmap.ic_useravatar).into(userAvatar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AVATAR", "ON START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AVATAR", "ON RESUME");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Crop.REQUEST_PICK){
            //crop image
            beginCrop(data.getData());
        }else if(requestCode == Crop.REQUEST_CROP){
            //handleCrop
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            userAvatar.setImageURI(Crop.getOutput(result));
            btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progresshint.setVisibility(View.INVISIBLE);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImg(){
        btn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            progressBar.setVisibility(View.VISIBLE);
            progresshint.setVisibility(View.VISIBLE);
            final String uid = user.getUid();
            String ref = uid + ".png";
            StorageReference avatarRef = reference.child(ref);
            userAvatar.setDrawingCacheEnabled(true);
            userAvatar.buildDrawingCache();
            Bitmap bitmap = userAvatar.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = avatarRef.putBytes(data);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress(((int) progress));
                    progresshint.setText((int) progress + "%");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //failed to upload ,then display error message
                    progresshint.setText(e.getLocalizedMessage());

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //String url = taskSnapshot.getDownloadUrl().toString();
                    //update user profile photo url
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(taskSnapshot.getDownloadUrl()).build();
                    user.updateProfile(profileChangeRequest);
                }
            });

        }
    }


}
