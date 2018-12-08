package xyz.vinayak.scrumup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    SharedPreferences sharedpreferences;

    public static int TOTAL_TASKS = 0; //independent
    public static int COMPLETED_TASKS = 0; //depends on toggle done
    public static int PENDING_TASKS = TOTAL_TASKS - COMPLETED_TASKS; // depends on toggle done
    public static int URGENT_IMPORTANT_TASKS = 0; // independent
    public static int URGENT_TASKS = 0; // independent
    public static int IMPORTANT_TASKS = 0; // independent
    public static int DEFAULT_TASKS = 0; // independent

    ViewPager pager;
    SmartTabLayout tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedpreferences = getSharedPreferences("xyz.vinayak.scrumup", Context.MODE_PRIVATE);
        TOTAL_TASKS = sharedpreferences.getInt("TOTAL_TASKS",0);
        COMPLETED_TASKS = sharedpreferences.getInt("COMPLETED_TASKS",0);
        PENDING_TASKS = sharedpreferences.getInt("PENDING_TASKS",0);
        URGENT_IMPORTANT_TASKS = sharedpreferences.getInt("URGENT_IMPORTANT_TASKS",0);
        URGENT_TASKS = sharedpreferences.getInt("URGENT_TASKS",0);
        IMPORTANT_TASKS = sharedpreferences.getInt("IMPORTANT_TASKS",0);
        DEFAULT_TASKS = sharedpreferences.getInt("DEFAULT_TASKS",0);

        pager = findViewById(R.id.viewPager);
        tabStrip = findViewById(R.id.tabStrip);




        setViewPager();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    //user is signed-in
                    Toast.makeText(MainActivity.this, "Welcome, You are Signed-In now!", Toast.LENGTH_SHORT).show();
                } else{
                    //user is signed-out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.web_hi_res_512)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedOutCleanUp() {
        if(ChatFragment.childEventListener != null){
            ChatFragment.databaseReference.removeEventListener(ChatFragment.childEventListener);
            ChatFragment.childEventListener = null;
        }
        //ChatFragment.chatView.clearMessages();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign In Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default : return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
        onSignedOutCleanUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void setViewPager() {
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1);
        tabStrip.setViewPager(pager);
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt("TOTAL_TASKS", TOTAL_TASKS);
        editor.putInt("COMPLETED_TASKS", COMPLETED_TASKS);
        editor.putInt("PENDING_TASKS", PENDING_TASKS);
        editor.putInt("URGENT_IMPORTANT_TASKS", URGENT_IMPORTANT_TASKS);
        editor.putInt("URGENT_TASKS", URGENT_TASKS);
        editor.putInt("IMPORTANT_TASKS", IMPORTANT_TASKS);
        editor.putInt("DEFAULT_TASKS", DEFAULT_TASKS);
        editor.commit();
        super.onStop();

    }

    public static void taskCountAdd(int category){
        switch (category) {
            case 0:
                MainActivity.URGENT_IMPORTANT_TASKS++;
                break;
            case 1:
                MainActivity.URGENT_TASKS++;
                break;
            case 2:
                MainActivity.IMPORTANT_TASKS++;
                break;
            case 3:
                MainActivity.DEFAULT_TASKS++;
                break;
            default:
                break;
        }
    }

    public static void taskCountSubtract(int category){
        switch (category) {
            case 0:
                if(MainActivity.URGENT_IMPORTANT_TASKS > 0) MainActivity.URGENT_IMPORTANT_TASKS--;
                break;
            case 1:
                if(MainActivity.URGENT_TASKS > 0 ) MainActivity.URGENT_TASKS--;
                break;
            case 2:
                if(MainActivity.IMPORTANT_TASKS > 0) MainActivity.IMPORTANT_TASKS--;
                break;
            case 3:
                if(MainActivity.DEFAULT_TASKS > 0) MainActivity.DEFAULT_TASKS--;
                break;
            default:
                break;
        }
    }
}
