package com.example.user.facebookalbums;

/**
 * Created by User on 01/11/2017.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.user.facebookalbums.R.id.album;
import static com.example.user.facebookalbums.R.id.image;

public class FacebookAlbumDemoActivity extends Activity{
    private final static String TAG="FacebookAlbumDemoActivity";
    private ArrayAdapter<AlbumItem> albumArrayAdapter;
    private String selectedAlbum;
    private String selectedImage;
    private Spinner spinnerSelectAlbum;
    private TextView textViewAlbumDescription;
    private Gallery gallerySelectedAlbum;
    private ImageView imageViewSelectedImage;
    String userid;
    ArrayList<String> albumname = new ArrayList<String>();
    ArrayList<AlbumItem> albums = new ArrayList<AlbumItem>();
    ArrayList<String> FBImages = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_layout);
        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("userid").toString();
        albumname.add("Select album");
        gallerySelectedAlbum=(Gallery)this.findViewById(R.id.GallerySelectedAlbum);
        spinnerSelectAlbum=(Spinner)this.findViewById(R.id.SpinnerSelectAlbum);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userid + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.getJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        albums.add(new AlbumItem(joAlbum.getString("id"),joAlbum.getString("name")));
                                        albumname.add(joAlbum.getString("name"));
                                    }

                                    spinnerSelectAlbum.setOnItemSelectedListener(new OnItemSelectedListener() {

                                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                   int arg2, long arg3) {
                                            selectedAlbum=spinnerSelectAlbum.getSelectedItem().toString();
                                            for(int i=0;i<albums.size();i++){
                                                if (albums.get(i).getName()==selectedAlbum)
                                            GetFacebookImages(albums.get(i).getId());}

                                        }

                                        public void onNothingSelected(AdapterView<?> arg0) {

                                        }
                                    });
                                    ArrayAdapter<String> adapter;
                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,albumname);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerSelectAlbum.setAdapter(adapter);
                                    spinnerSelectAlbum.setSelection(0);


                                }
                            } else {
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();





    }
    public void GetFacebookImages(final String albumId) {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        FBImages.clear();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try {
                            if (response.getError() == null) {


                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++){
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages=joAlbum.getJSONArray("images");
                                        if(jaImages.length()>0)
                                        {
                                            FBImages.add(jaImages.getJSONObject(0).getString("source"));
                                        }
                                    }
                                    imageViewSelectedImage = (ImageView)findViewById(R.id.imgGalleryImage);

                                    gallerySelectedAlbum.setAdapter(new AlbumImageAdapter(getApplicationContext(),FBImages));
                                    gallerySelectedAlbum.setOnItemClickListener(new OnItemClickListener()
                                    {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id)
                                        {
                                            selectedImage=(String)gallerySelectedAlbum.getItemAtPosition(position);
                                            Picasso.with(getApplicationContext()).load(selectedImage).into(imageViewSelectedImage);

                                        }
                                    });

                                }

                            }} catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();

    }
}
