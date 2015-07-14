package com.paipeng.listviewsectionindex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paipeng.listviewsectionindex.R;
import com.paipeng.listviewsectionindex.model.Contact;
import com.paipeng.listviewsectionindex.views.FastScrollRecyclerViewInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
        public int type;
        public ViewHolder(View v, final Context context) {
            super(v);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Item click nr: " + mTextView.getText(), Toast.LENGTH_SHORT).show();
                }
            });

            mTextView = (TextView) v.findViewById(R.id.nameTextView);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);

            if (mTextView == null) {

                mTextView = (TextView) v.findViewById(R.id.sectionTextView);
            }
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
            mapIndex.put(ch, x+ mapIndex.keySet().size() +1);
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
    public ContactAdatper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = null;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact, parent, false);
            // set the view's size, margins, paddings and layout parameters

        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_section, parent, false);
            // set the view's size, margins, paddings and layout parameters

        }
        ViewHolder vh = new ViewHolder(v, parent.getContext());

        vh.setType(viewType);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder.getType() == 0) {
            int positionWithMapIndex = getPositionWithMapIndex(position);
            Contact contact = contactList.get(positionWithMapIndex);
            holder.mTextView.setText(contact.getLastName());
            holder.titleTextView.setText(String.valueOf(contact.getId()));

        } else {
            String sectionString = getSectionString(position);
            if (sectionString != null) {
                holder.mTextView.setText(sectionString);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return getFirstElement(position)?1:0;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactList.size() + sections.length;
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mapIndex;
    }

    private boolean getFirstElement(int position) {
        int factor = 0;
        for (String key : mapIndex.keySet()) {
            if (position == 0) {
                return true;
            }

            Integer number = mapIndex.get(key);
            int c = number.intValue();
            if (position == (c + factor)) {
                return true;
            }

            if (position < (c + factor)) {
                break;
            }
            //factor++;
        }
        return false;
    }

    private String getSectionString(int position) {
        //Log.i(TAG, "getSectionString " + position);
        int factor = 0;
        int pre = 0;
        for (String key : mapIndex.keySet()) {

            Integer number = mapIndex.get(key);
            int c = number.intValue();

            //Log.i(TAG, "check " + (pre + " - " +  factor));

            if (position == (pre + factor)) {
                return key;
            }

            if (position < (pre+ factor)) {
                break;
            }
            pre = c;
            //factor++;
        }
        return null;
    }

    private int getPositionWithMapIndex(int position) {
        //Log.i(TAG, "getPositionWithMapIndex " + position);
        int c = 0;
        int factor = 0;
        for (String key : mapIndex.keySet()) {
            Integer number = mapIndex.get(key);
            c = number.intValue();

            //Log.i(TAG, "check " + (c + " - " +  factor));

            if (position < (c + 0)) {
                return position-factor-1;
            }

            factor ++;
        }
        return position;
    }



}
