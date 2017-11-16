package store.singto.singtostore.MeTab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import store.singto.singtostore.R;
import store.singto.singtostore.TOOLS.RoundedImg;
import store.singto.singtostore.loginRegister.LoginActivity;

public class TabMeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginManager loginManager;

    //UI elements
    private Button loginRegisterBtn;
    private Button exitBtn;
    private ImageView userAvatarImgView;
    private TextView userNameTextView;

    public TabMeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //populate UI with user data
                    userinstate(user);
                }else {
                    //set UI to log out state
                    useroutstate();
                }
            }
        };

        loginManager = LoginManager.getInstance();

        Log.d("TabMe", "Me Tab Created");

    }

    @Override
    public void onStop(){
        super.onStop();
        if(authStateListener != null ){
            mAuth.removeAuthStateListener(authStateListener);
        }

        Log.d("TabMe", "onResume is called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("TabMe", "Me Tab View Created");

        View view = inflater.inflate(R.layout.fragment_tab_me, container, false);
        setupView(view);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("TabMe", "Me Tab Attached");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TabMe", "Me Tab Detached");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TabMe", "Me Tab onStart");
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TabMe", "onResume is called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TabMe", "onResume is called");
    }


    private void setupView(View view){
        userAvatarImgView = view.findViewById(R.id.userAvatar);
        userNameTextView = view.findViewById(R.id.userName);
        loginRegisterBtn = view.findViewById(R.id.loginRegisterBtn);
        loginRegisterBtn.setOnClickListener(loginRegisterBtnClicked);
        exitBtn = view.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnClicked);

    }

    private View.OnClickListener exitBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //log out from facebook
            loginManager.logOut();
            //log out from firebase;
            mAuth.signOut();
        }
    };

    private View.OnClickListener loginRegisterBtnClicked = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            //go to login activity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    };

    private void userinstate(FirebaseUser user){
        loginRegisterBtn.setVisibility(View.INVISIBLE);
        userNameTextView.setVisibility(View.VISIBLE);
        exitBtn.setVisibility(View.VISIBLE);

        if(user.getPhotoUrl()!=null){
            Picasso.with(getActivity()).load(user.getPhotoUrl()).transform(new RoundedImg()).error(R.mipmap.ic_useravatar).into(userAvatarImgView);
        }else {
            userAvatarImgView.setImageResource(R.mipmap.ic_useravatar);
        }
        if(user.getDisplayName() != null){
            userNameTextView.setText(user.getDisplayName());
        }else {
            userNameTextView.setText(getString(R.string.yourname));
        }


    }

    private void useroutstate(){
        userAvatarImgView.setImageResource(R.mipmap.ic_useravatar);
        loginRegisterBtn.setVisibility(View.VISIBLE);
        userNameTextView.setText(getString(R.string.yourname));
        userNameTextView.setVisibility(View.INVISIBLE);
        exitBtn.setVisibility(View.INVISIBLE);
    }

}
