package com.example.memeshare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    String imgUrl=null; //In this we have assigned a variable which stores the url of the image from the api


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadImage();
    }

    public void loadImage(){
        String url = "https://meme-api.herokuapp.com/gimme";
        final ImageView imv=findViewById(R.id.MemeImage);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//her we have make the visibility of the progress bar "visible"
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest//here we have send the request to the api to get the jsonobject,url-it showsfrom where we have to send the request,response.listener-it helps to listen the response from the request we have sent
                (Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                           imgUrl = response.getString("url");
                            Log.d("myApp", "onResponse: yeah"+response.getString("url"));
                            Glide.with(MainActivity.this).load(imgUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);//here we have make the visibility of the progress bar "none"
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).load(imgUrl).into(imv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("myApp", "onErrorResponse:oops");
                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }


    public void next(View view) {
        loadImage();
    }

    public void share(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "HEY CHECKOUT THIS MEME!! "+imgUrl);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "SEND VIA"));

    }
}