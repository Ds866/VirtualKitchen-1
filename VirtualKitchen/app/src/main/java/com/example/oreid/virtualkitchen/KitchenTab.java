package com.example.oreid.virtualkitchen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Base Kitchen View Tab Class
 *
 * Sets up a list view showing items.
 *
 * create by Hollie on 22/02/17
 *
 */

public class KitchenTab extends AppCompatActivity implements HasListView {

    private static final String TAG = "KitchenTab";

    protected FoodStorageData db = VKData.getInstance().getFoodDB();

    private int contentViewId = R.layout.activity_kitchen_tab;
    private int listViewId  = R.id.list_view;
    private String tabName = "Kitchen";
    private StorageArea storageArea = null;
    public static final int REQUEST_CODE = 5;

    private ArrayList<FoodItem> listData = new ArrayList<FoodItem>();

    ListView listView;
    FoodItemAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewId);

        listView = (ListView)findViewById(listViewId);
        listAdapter = new FoodItemAdapter(this,
                R.layout.food_list_item,
                listData);
        listView.setAdapter(listAdapter);


    }


    protected void onCreate(Bundle savedInstanceState, String sl) {
        if (sl == "ShoppingList") {
            super.onCreate(savedInstanceState);
            setContentView(contentViewId);

            listView = (ListView) findViewById(listViewId);
            listAdapter = new FoodItemAdapter(this,
                    R.layout.activity_shoppinglist,
                    listData);
            listView.setAdapter(listAdapter);
        }

    }

    /**
     * Set the list's data
     * @param d list's new data
     */
    public void setListData(ArrayList<FoodItem> d) {
        this.listData = d;
    }
    public ArrayList<FoodItem> getListData() { return this.listData; }

    public void setUpdatedList(ArrayList<FoodItem> newFood) {
        setListData(newFood);
        updateUI();
    }

    /**
     * Updates the list view.
     */
    public void updateUI() {
        listAdapter.clear();
        listAdapter.addAll(listData);
        listAdapter.notifyDataSetChanged();
    }



    public void deleteButtonHandler(View v) {

        // index of where the item can be found in the list's data.
        int position = listView.getPositionForView((View)v.getParent());

        db.decrement(position, this.storageArea);

        updateUI();

    }
    //adds one of the selected items to shopping list. Doesn't work in ALL tab as no location is set
    public void addToShoppingListHandler(View v) {
        int position = listView.getPositionForView((View)v.getParent());
        db.storageItemToShoppingList(position, this.storageArea);
        updateUI();
    }
    //Takes all items selected from shopping list and adds to storage
    public void checkOffShoppingListHandler(View v) {
        int position = listView.getPositionForView((View)v.getParent());
        db.shoppingListItemToStorage(position);
        updateUI();
    }

    public void setContentViewId(int id) {
        contentViewId = id;
    }

    public void setListViewId(int id) {
        listViewId = id;
    }

    public void setTabName(String name) {
        tabName = name;
    }

    /**
     * Sets storage area associated with this tab. Also updates the list based on this data,
     * depending on the boolean passed in.
     * @param sa storage area associated with this tab
     * @param updateList update the list based on the storage area?
     */
    public void setStorageArea(StorageArea sa) {
        this.storageArea = sa;
        db.setListUpdater(this.storageArea, this);
    }

    public StorageArea getStorageArea() {
        return this.storageArea;
    }
}
