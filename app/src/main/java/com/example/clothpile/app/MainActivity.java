package com.example.clothpile.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.clothpile.R;
import com.example.clothpile.app.entity.ClothesList;
import com.example.clothpile.app.fragments.AddEditListFragment;
import com.example.clothpile.app.fragments.LaundryListsFragment;
import com.example.clothpile.app.fragments.SettingsFragment;
import com.example.clothpile.app.helper.ClothesListRecyclerview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm myRealm;
    private RecyclerView recyclerView;
    private ClothesListRecyclerview clothesListRecyclerview;
    private LinearLayoutManager mLinearLayoutManager;
    private static final int SIGN_IN_REQUEST_CODE = 0;
    FloatingActionButton addListFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new LaundryListsFragment());

        //super.onCreate();
        /*Realm.init(this);
        setContentView(R.layout.activity_main);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        addListFab = findViewById(R.id.add_fab_button);

        addListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddEditListFragment());
            }
        });

        Log.v("hello", "HELLLLLLLLLLLLLO");
        myRealm = Realm.getDefaultInstance();
        displayLists();*/
    }

    /*protected void onResume() {
        super.onResume();
        if (clothesListRecyclerview != null)
            clothesListRecyclerview.notifyDataSetChanged();
        displayLists();
    }

    private void displayLists(){

        RealmResults<ClothesList> realmResults = myRealm.where(ClothesList.class).findAll();

        recyclerView = findViewById(R.id.my_lists);

        mLinearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);

        clothesListRecyclerview = new ClothesListRecyclerview(this, myRealm, realmResults);
        recyclerView.setAdapter(clothesListRecyclerview);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.settings_menu) {
            loadFragment(new SettingsFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
    displayLists();
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }*/
}