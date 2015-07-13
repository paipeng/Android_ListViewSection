package com.paipeng.listviewsectionindex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.paipeng.listviewsectionindex.R;
import com.paipeng.listviewsectionindex.model.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.paipeng.listviewsectionindex.views.FastScrollRecyclerViewInterface;

/**
 * Created by paipeng on 13/07/15.
 */
public class ContactAdatper extends RecyclerView.Adapter<ContactAdatper.ViewHolder> implements FastScrollRecyclerViewInterface {
    private static final String TAG = ContactAdatper.class.getSimpleName();
    private String[] sections;
    private HashMap<String, Integer> mapIndex;

    private List<Contact> contactList;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView titleTextView;
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;


            mTextView = (TextView) v.findViewById(R.id.nameTextView);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        }
    }

    public ContactAdatper(Context context, List<Contact> objects) {
        contactList = objects;
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < objects.size(); x++) {
            Contact contact = objects.get(x);
            String ch = contact.getLastName().substring(0, 1);
            ch = ch.toUpperCase(Locale.US);

            // HashMap will prevent duplicates
            mapIndex.put(ch, x);
        }

        Set<String> sectionLetters = mapIndex.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);

        Log.d(TAG, "sections " + sections[0]);


    }



    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdatper.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ...

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(contactList.get(position).getLastName());
        holder.titleTextView.setText(String.valueOf(contactList.get(position).getId()));
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mapIndex;
    }
}
