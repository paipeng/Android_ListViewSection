package com.paipeng.listviewsectionindex;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.paipeng.listviewsectionindex.adapters.ContactAdatper;
import com.paipeng.listviewsectionindex.model.Contact;
import com.paipeng.listviewsectionindex.views.FastScrollRecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        ArrayList<Contact> contactList = new ArrayList<>();
        long id = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            Contact contact = new Contact();
            contact.setFirstName("Test");
            contact.setLastName(String.valueOf(c) + "_Test");
            contact.setId(id);
            contactList.add(contact);
            id ++;
        }

        RecyclerView listView = (RecyclerView) findViewById(R.id.listView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        listView.setHasFixedSize(true);
        listView.setAdapter(new ContactAdatper(this, contactList));

        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
        listView.addItemDecoration(decoration);
        listView.setItemAnimator(new DefaultItemAnimator());
        */

        test();
    }

    private void test() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Contact> contactList = new ArrayList<>();
        long id = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            for (int i = 0; i < 5; i++) {
                Contact contact = new Contact();
                contact.setFirstName("Test " + String.valueOf(i));
                contact.setLastName(String.valueOf(c) + "_Test");
                contact.setId(id);
                contactList.add(contact);
                id ++;
            }
        }

        HashMap<String, Integer> mapIndex = calculateIndexesForName(contactList);

        ContactAdatper mAdapter = new ContactAdatper(this, contactList);
        mRecyclerView.setAdapter(mAdapter);
        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }


    private HashMap<String, Integer> calculateIndexesForName(ArrayList<Contact> items){
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i<items.size(); i++){
            String name = items.get(i).getLastName();
            String index = name.substring(0,1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
