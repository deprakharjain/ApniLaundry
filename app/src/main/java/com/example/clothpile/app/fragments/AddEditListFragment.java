package com.example.clothpile.app.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.clothpile.R;
import com.example.clothpile.app.MainActivity;
import com.example.clothpile.app.UserUtil;
import com.example.clothpile.app.entity.ClothesList;
import com.example.clothpile.app.helper.ItemAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;

public class AddEditListFragment extends Fragment {


    Button addListButton, pickDateButton;
    ElegantNumberButton elegantNumberButton;
    TextView roomNumber, collectionDateTxt;
    String collectionDate;
    ListView listView;
    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;
    Calendar c;
    DatePickerDialog datePickerDialog;
    UserUtil userUtil;

    ClothesList listToEdit;
    String editId;
    Boolean isNewList = true;

    private RealmList<String> itemsList;
    private RealmList<Integer> priceList, numberOfItems;
    private ItemAdapter itemAdapter;

    public AddEditListFragment(String id){ //edit list object
        editId = id;
        isNewList = false;
    }

    public  AddEditListFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_list_fragment, container, false);
        roomNumber = view.findViewById(R.id.room_number);
        addListButton = view.findViewById(R.id.add_list);
        pickDateButton = view.findViewById(R.id.pick_date_btn);
        collectionDateTxt = view.findViewById(R.id.date);
        elegantNumberButton = view.findViewById(R.id.elegant_number_button);
        listView = view.findViewById(R.id.items_list_view);

        myRealm = Realm.getDefaultInstance();

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserUtil.sumOfArray(numberOfItems) == 0 || collectionDate == null){
                    Toast.makeText(getContext(), "Fields empty!", Toast.LENGTH_SHORT).show();

                } else {
                    insertRecords();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new LaundryListsFragment()).addToBackStack(null).commit();
                }
            }
        });
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });



        userUtil = new UserUtil();
        roomNumber.setText(userUtil.roomNumber);

        if(isNewList) {
            itemsList = userUtil.clothItemsList;
            numberOfItems = userUtil.numberOfItems;
            priceList = userUtil.clothItemsPriceList;
        } else{
            listToEdit = myRealm.where(ClothesList.class).equalTo("listId", editId).findFirst();
            itemsList = listToEdit.getItemsList();
            numberOfItems = listToEdit.getNumberOfItems();
            priceList = listToEdit.getPriceList();
            collectionDateTxt.setText(listToEdit.getCollectionDate());
        }

        itemAdapter = new ItemAdapter(getContext(), itemsList, numberOfItems);
        listView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        return view;
    }

    private void insertRecords() {

        final String id = UUID.randomUUID().toString();

        realmAsyncTask = myRealm.executeTransactionAsync(
                new Realm.Transaction() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void execute(Realm realm) {
                        ClothesList clothesList = realm.createObject(ClothesList.class, id);
                        //clothesList.setTotalClothes(totalClothes);
                        clothesList.setRoomNumber(userUtil.roomNumber);
                        clothesList.setNumberOfItems(numberOfItems);
                        clothesList.setItemsList(itemsList);
                        clothesList.setPriceList(priceList);
                        clothesList.setCollectionDate(collectionDate);
                        clothesList.calculateBill();
                    }
                },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "List added", Toast.LENGTH_SHORT).show();
                        // TODO : refresh
                    }
                },
                new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getContext(), "Error in adding list! Please try again", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pickDate(){
        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = day + "/" + month;
                collectionDateTxt.setText(date);
                collectionDate = date;
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (realmAsyncTask != null && realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }

    private class CustomTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... param) {
            //Do some work
            return null;
        }

        protected void onPostExecute(Void param) {
            //Print Toast or open dialog
        }
    }
}
