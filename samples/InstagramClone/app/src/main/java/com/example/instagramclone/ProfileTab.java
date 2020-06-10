package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment {

    private EditText edtProfileName, edtProfileBio, edtProfileProfession, edtProfileHobbies,
            edtProfileSport;
    private Button btnProfileUpdateInfo;
    private ParseUser parseUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container,
                false);

        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileSport = view.findViewById(R.id.edtProfileSport);
        btnProfileUpdateInfo = view.findViewById(R.id.btnProfileUpdateInfo);

        parseUser = parseUser.getCurrentUser();

        if (parseUser.get("profileName") == null) {
            edtProfileName.setText("");
            edtProfileBio.setText("");
            edtProfileProfession.setText("");
            edtProfileHobbies.setText("");
            edtProfileSport.setText("");
        } else  {
            edtProfileName.setText(parseUser.get("profileName") + "");
            edtProfileBio.setText(parseUser.get("profileBio") + "");
            edtProfileProfession.setText(parseUser.get("profileProfession") + "");
            edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");
            edtProfileSport.setText(parseUser.get("profileSport") + "");
        }


        btnProfileUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parseUser.put("profileName", edtProfileName.getText().toString());
                parseUser.put("profileBio", edtProfileBio.getText().toString());
                parseUser.put("profileProfession", edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies", edtProfileHobbies.getText().toString());
                parseUser.put("profileSport", edtProfileSport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(getContext(), "Info Updated!",
                                    Toast.LENGTH_LONG, FancyToast.INFO, false).show();
                        } else {
                            FancyToast.makeText(getContext(), e.getMessage(),
                                    Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        });

        return view;

    }
}