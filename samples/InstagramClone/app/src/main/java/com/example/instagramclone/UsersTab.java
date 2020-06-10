package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersTab extends Fragment {

    private ListView listView;
    private ArrayList arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView txtUserDownloading;

    public UsersTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        txtUserDownloading = view.findViewById(R.id.txtUserDownloading);

        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().get("username"));

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {

                    if (users.size() > 0){

                        for (ParseUser user : users) {

                            arrayList.add(user.getUsername());

                        }

                        listView.setAdapter(arrayAdapter);
                    }
                }
                txtUserDownloading.animate().alpha(0f).setDuration(2000);
                listView.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }
}