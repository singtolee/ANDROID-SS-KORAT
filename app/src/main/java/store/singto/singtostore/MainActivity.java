package store.singto.singtostore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import store.singto.singtostore.AllProductsTab.TabAllProductsFragment;
import store.singto.singtostore.HomeTab.TabHomeFragment;
import store.singto.singtostore.MeTab.TabMeFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment homeTab, allProductTab, meTab;



    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_home:
                    setFragment(0);
                    return true;
                case R.id.tab_allproducts:
                    setFragment(1);
                    return true;
                case R.id.tab_me:
                    setFragment(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationViewEx navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.tabs_container, new TabHomeFragment()).commit();

        Log.d("MainTab", "Started LIQ");

        navigation.enableAnimation(false);
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        navigation.setTextVisibility(false);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragments(transaction);
        switch(index) {
            case 0:
                if(homeTab == null){
                    homeTab = new TabHomeFragment();
                    transaction.add(R.id.tabs_container, homeTab);
                } else {
                    transaction.show(homeTab);
                    Log.d("MainTab", "home is shown");
                }
                break;
            case 1:
                if(allProductTab == null ){
                    allProductTab = new TabAllProductsFragment();
                    transaction.add(R.id.tabs_container, allProductTab);
                } else {
                    transaction.show(allProductTab);
                    Log.d("MainTab", "prd is shown");
                }
                break;
            case 2:
                if(meTab == null){
                    meTab = new TabMeFragment();
                    transaction.add(R.id.tabs_container, meTab);
                } else {
                    transaction.show(meTab);
                    Log.d("MainTab", "me is shown");
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction transaction){
        if (homeTab != null) {
            transaction.hide(homeTab);
            Log.d("MainTab", "homeTab is Hidden");
        }
        if(allProductTab != null) {
            transaction.hide(allProductTab);
            Log.d("MainTab", "Product is Hidden");
        }
        if(meTab != null){
            transaction.hide(meTab);
            Log.d("MainTab", "Me is Hidden");
        }
    }

}
