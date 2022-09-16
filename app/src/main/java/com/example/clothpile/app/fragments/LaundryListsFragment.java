package com.example.clothpile.app.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.clothpile.R;
import com.example.clothpile.app.entity.ClothesList;
import com.example.clothpile.app.helper.ClothesListRecyclerview;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class LaundryListsFragment extends Fragment {

    private Realm myRealm;
    private RecyclerView recyclerView;
    private ClothesListRecyclerview clothesListRecyclerview;
    private static final int SIGN_IN_REQUEST_CODE = 0;
    FloatingActionButton addListFab;
    ImageView qrCode;
    QRGEncoder qrgEncoder;

    Context context;
    Button refresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.laundry_lists_fragment, container, false);
        recyclerView = view.findViewById(R.id.my_lists);
        qrCode=view.findViewById(R.id.LaundryQrCode);
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        addListFab = view.findViewById(R.id.add_fab_button);
        refresh = view.findViewById(R.id.RefreshButton);
       refresh.setOnClickListener(view1 -> {
            displayLists();
       });
        addListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddEditListFragment());
            }
        });

        //Log.v("hello", "HELLLLLLLLLLLLLO VAISH");
        myRealm = Realm.getDefaultInstance();
        displayLists();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        refresh.setVisibility(View.VISIBLE);
        displayLists();
    }

    public void onResume() {
        super.onResume();
        refresh.setVisibility(View.VISIBLE);
        if (clothesListRecyclerview != null)
            clothesListRecyclerview.notifyDataSetChanged();

        displayLists();
    }

    private void displayLists(){

        Log.d("Workinfr", "displayLists");
        RealmResults<ClothesList> realmResults = myRealm.where(ClothesList.class).findAll();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
        String number="";
        if(!myRealm.isEmpty()) {
            qrCode.setVisibility(View.VISIBLE);
            Log.d("Workinfr", "displayLists-->in if condotion");
            ClothesList c = realmResults.get(0);
            RealmList<String> itemsList = c.getItemsList();
            RealmList<Integer> in = c.getNumberOfItems();
            for (int i = 0; i < itemsList.size(); i++) {
                number += itemsList.get(i) + " " + in.get(i) + "\n";

            }
            number+=c.getCollectionDate();
//        Log.d("xyz", "onCreateView: "+number);
//        qrgEncoder=new QRGEncoder(number, QRGContents.Type.TEXT,500);
//        Bitmap qrBits= qrgEncoder.getBitmap();
//        qrCode.setImageBitmap(qrBits);

            QRGEncoder qrgEncoder = new QRGEncoder(number, null, QRGContents.Type.TEXT, 500);

            try {
                // Getting QR-Code as Bitmap
                Bitmap bitmap = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                qrCode.setImageBitmap(bitmap);
                Log.d("Workinfr", "displayLists-->in try catch");

            } catch (Exception e) {
            }
        }else{
            qrCode.setVisibility(View.GONE);
        }


            recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        clothesListRecyclerview = new ClothesListRecyclerview(context, myRealm, realmResults);
        recyclerView.setAdapter(clothesListRecyclerview);
    }

    private void loadFragment(Fragment fragment){
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
