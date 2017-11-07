package com.example.user.facebookalbums;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/10/2017.
 */

public class Accueil extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.content_main);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.get("name").toString();
        String surname = bundle.get("surname").toString();
        String imageurl = bundle.getString("imageurl").toString();
        final String userid = bundle.getString("userid").toString();
        TextView nameView =(TextView) findViewById(R.id.nameuser);
        nameView.setText(""+name+" "+surname+"  ");
        Button logout =(Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(Accueil.this,MainActivity.class);
                startActivity(login);
                finish();
            }
        });
        Button album =(Button) findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent alb = new Intent(Accueil.this,FacebookAlbumDemoActivity.class);
                alb.putExtra("userid",userid);
                startActivity(alb);

            }
        });
        new Accueil.DownloadImage((ImageView)findViewById(R.id.profileimage)).execute(imageurl);

    }
    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
        ImageView image;
        public DownloadImage(ImageView image){
            this.image = image;
        }
        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mi =null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mi = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return  mi;
        }


        protected void onPostExecute(Bitmap result){
            image.setImageBitmap(result);

        }

    }
}
