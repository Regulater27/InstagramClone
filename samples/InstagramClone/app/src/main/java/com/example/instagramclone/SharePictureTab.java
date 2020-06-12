package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SharePictureTab extends Fragment implements View.OnClickListener {

    // Initialize UI variables
    private ImageView imgShareImageView;
    private EditText edtShareDescription;
    private Button btnShareImage;
    Bitmap receivedImageBitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container,
                false);

        // Initialize UI components with variables
        imgShareImageView = view.findViewById(R.id.imgShareImageView);
        edtShareDescription = view.findViewById(R.id.edtShareDescription);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        imgShareImageView.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // We need to ask the user for permission to access external storage.  Check if the device
            // is android 23 or higher and if the user has granted permission.  If not, ask for
            // external storage permission
            case R.id.imgShareImageView:

                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                } else {

                    getChosenImage();
                }

                break;
            case R.id.btnShareImage:

                if (receivedImageBitmap != null){

                    if (edtShareDescription.getText().toString().equals("")) {

                        FancyToast.makeText(getContext(), "Error: You must specify an image description",
                                Toast.LENGTH_LONG, FancyToast.ERROR, false).show();

                    } else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtShareDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null){

                                    FancyToast.makeText(getContext(), "Done!",
                                            Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                                } else {

                                    FancyToast.makeText(getContext(), "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                }
                                dialog.dismiss();
                            }

                        });

                    }

                } else {
                    FancyToast.makeText(getContext(), "Error: You must select and image",
                            Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

                break;
        }

    }

    /**
     * allows the user to search for an image on their device and select one
     */
    private void getChosenImage() {

        //FancyToast.makeText(getContext(), "Now we can access the Images", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    /**
     * Method that will respond to the user's choice of access permissions from the onClick method
     * @param requestCode the code produced from the image view onclick listener
     * @param permissions array of permissions, indicating what permissions the user has given the app
     * @param grantResults array that holds permissions and request codes, same as requestPermissions in
     *                     onClick method-case imageView clicked
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getChosenImage();
            }
        }
    }

    /**
     *
     * @param requestCode the code produced from the getChosenImage method
     * @param resultCode
     * @param data the image the user selects
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {

            if (resultCode == Activity.RESULT_OK) {

                // Do something with your captured image
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);

                    imgShareImageView.setImageBitmap(receivedImageBitmap);

                } catch (Exception e){

                    e.printStackTrace();
                }
            }
        }
    }
}