package com.example.memeapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.memeapp.databinding.ActivityMainBinding;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    static String memeUrl;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //CALLING LOAD MEME APP
        loadMeme();

        //functionality for share btn
        binding.btnShare.setOnClickListener(view -> {
            //calling clickShare method
            clickShare();
        });

        //functionality for next btn
        binding.btnNext.setOnClickListener(view -> {

            //calling clickNext method
            clickNext();
        });
    }

    //method to load API
    private void loadMeme() {
        final String url = "https://meme-api.herokuapp.com/gimme";

        //making  a jsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
//                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                memeUrl = response.getString("url");
                //here setting Image to ImageView through GLIDE library
                Glide.with(this).load(memeUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).placeholder(R.drawable.placeholder).into(binding.img1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity.this, "Please !! Check Your Internet Connection", Toast.LENGTH_LONG).show());

        //adding request to the requestQueue to get Response
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    //method for share button
    private void clickShare() {
          /* ACTION_SEND: Deliver some data to someone else.
        createChooser (Intent target, CharSequence title): Here, target- The Intent that the user will be selecting an activity to perform.
            title- Optional title that will be displayed in the chooser.
        Intent.EXTRA_TEXT: A constant CharSequence that is associated with the Intent, used with ACTION_SEND to supply the literal data to be sent.
        */
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "hey" + memeUrl);
        Intent.createChooser(intent, "share Via");
        startActivity(intent);


    }

    //method for next button
    private void clickNext() {
        //setting progress bar visible
        binding.progressBar.setVisibility(View.VISIBLE);
        //calling loadMeme Method
        loadMeme();

    }

}