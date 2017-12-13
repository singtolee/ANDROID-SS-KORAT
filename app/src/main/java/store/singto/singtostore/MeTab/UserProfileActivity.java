
package store.singto.singtostore.MeTab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Map;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.BaseActivity;
import store.singto.singtostore.TOOLS.RoundedImg;
import store.singto.singtostore.TOOLS.SaveLocale;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener{
    private FirebaseAuth auth;
    private RelativeLayout editavatar, editname, editlocale;
    private ImageView smalluseravatar;
    private TextView smallusername, currentlocale;
    private Toolbar toolbar;
    private RadioGroup mMode;

    //SharedPreference to read and save Locale language
    private SaveLocale saveLocale;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupview();
        setdefaultlocale();

    }

    //if no default Locale, set English
    private void setdefaultlocale(){
        Map<String, String> data = saveLocale.read();
        if(getString(R.string.nll).equals(data.get("locale"))){
            saveLocale.save(getString(R.string.en));
        }
    }

    void setupview(){

        toolbar = findViewById(R.id.userprofiletoolbar);
        toolbar.setTitle(R.string.userprofile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();
        editavatar = findViewById(R.id.editUserAvatar);
        editavatar.setOnClickListener(this);
        editname = findViewById(R.id.editUserName);
        editname.setOnClickListener(this);
        editlocale = findViewById(R.id.editLocale);
        editlocale.setOnClickListener(this);

        smalluseravatar = findViewById(R.id.smalluseravater);
        smallusername = findViewById(R.id.smallusername);
        currentlocale = findViewById(R.id.currentlocale);

        context = getApplicationContext();
        saveLocale = new SaveLocale(context);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editUserAvatar:
                //change profile photo
                Intent i = new Intent(this, EditAvatarActivity.class);
                startActivity(i);
                overridePendingTransition(R.xml.slide_from_right, R.xml.slide_to_left);
                break;
            case R.id.editUserName:
                //change name, initiate a dialog fragment
                Intent intent = new Intent(this, EditUserNameActivity.class);
                intent.putExtra("name", smallusername.getText());
                startActivity(intent);
                overridePendingTransition(R.xml.slide_from_right, R.xml.slide_to_left);
                break;
            case R.id.editLocale:
                //pop up dialog to choose language
                popoupwindow();
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

        Map<String, String> data = saveLocale.read();
        currentlocale.setText(data.get("locale"));
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

    private void popoupwindow(){

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupwindow_layout, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = popupView.findViewById(R.id.applybtnselectlocale);
        Button btnCancel = popupView.findViewById(R.id.cancelbtnselectlocale);

        mMode = popupView.findViewById(R.id.radioScreenMode);
        //read from sharepreference to check
        Map<String, String> data = saveLocale.read();
        if(getString(R.string.en).equals(data.get("locale"))) {
            mMode.check(R.id.radioeng);
        }else {
            mMode.check(R.id.radioth);
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = mMode.getCheckedRadioButtonId();
                if (id == R.id.radioeng ) {
                    saveLocale.save(getString(R.string.en));
                    currentlocale.setText(R.string.en);
                }
                if (id == R.id.radioth ) {
                    saveLocale.save(getString(R.string.th));
                    currentlocale.setText(R.string.th);
                }

                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
}
