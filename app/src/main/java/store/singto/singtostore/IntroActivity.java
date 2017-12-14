package store.singto.singtostore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

import store.singto.singtostore.TOOLS.LocaleManager;
import store.singto.singtostore.TOOLS.PreManager;
import store.singto.singtostore.TOOLS.SaveLocale;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private PreManager preManager;
    private SaveLocale saveLocale;
    private Button btnENG, btnTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check is first time launch
        preManager = new PreManager(this);
        saveLocale = new SaveLocale(this);

        if(!preManager.isFirstTimeLaunch()){
            //get default language
            Map<String, String> data = saveLocale.read();
            String currentlocale = data.get("locale");
            gotomainactivity(currentlocale);
            finish();
        }

        setContentView(R.layout.activity_intro);


        btnENG = findViewById(R.id.btnENG);
        btnTH = findViewById(R.id.btnTH);
        btnENG.setOnClickListener(this);
        btnTH.setOnClickListener(this);
    }

    private void gotomainactivity(String language){
        preManager.setFirstTimeLaunch(false);

        LocaleManager.setNewLocale(this, language);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnENG:
                //set eng as default language, set not first time launch
                saveLocale.save(getString(R.string.en));
                gotomainactivity(getString(R.string.en));
                break;

            case R.id.btnTH:
                saveLocale.save(getString(R.string.th));
                gotomainactivity(getString(R.string.th));
                break;
            default:
                break;
        }
    }
}
