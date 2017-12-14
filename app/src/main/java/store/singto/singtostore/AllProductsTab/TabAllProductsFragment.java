package store.singto.singtostore.AllProductsTab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.SaveLocale;

public class TabAllProductsFragment extends Fragment {

    private TabLayout tabbar;
    private ViewPager viewPager;

    private List<Category> categories;

    private FirebaseFirestore db;

    private SaveLocale saveLocale;
    private Context context;



    public TabAllProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_all_products, container, false);

        viewPager = view.findViewById(R.id.categoryfragmentsviewpager);
        tabbar = view.findViewById(R.id.categorytabbar);

        viewPager.setOffscreenPageLimit(3);
        tabbar.setupWithViewPager(viewPager);

        categories = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        context = getContext();
        saveLocale = new SaveLocale(context);


        loadcategories();

        return view;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadcategories(){
        db.collection("CATEGORIES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Adapter adapter = new Adapter(getFragmentManager());
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        Category category = documentSnapshot.toObject(Category.class);
                        categories.add(category);
                        CategoryprdFragment categoryprdFragment = new CategoryprdFragment();

                        Map<String, String> data = saveLocale.read();
                        if(data.get("locale").equals(getString(R.string.en))){
                            adapter.addFragment(categoryprdFragment, category.ENG);
                        }else {
                            adapter.addFragment(categoryprdFragment, category.TH);
                        }


                    }
                    viewPager.setAdapter(adapter);
                }
            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
