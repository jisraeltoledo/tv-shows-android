package com.example.tvshows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tvshows.model.TVShow;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        final TVShow show = (TVShow) intent.getSerializableExtra("show");
        ImageView poster = findViewById(R.id.poster);
        Glide.with(this).load(show.getPoster()).into(poster);

        TextView name = findViewById(R.id.name);
        name.setText(show.getName());

        WebView summary = findViewById(R.id.summary);
        summary.getSettings().setJavaScriptEnabled(true);
        summary.loadDataWithBaseURL("", show.getSummary(), "text/html", "UTF-8", "");

        Button imdb_button = findViewById(R.id.imdb_button);
        configureImdbBUtton (imdb_button, show);


        ImageView favorites = findViewById(R.id.action_favorites);
        configureFavButton (favorites, show);
    }

    private void configureFavButton(ImageView favorites, TVShow show) {
        SharedPreferences sharedPref = getSharedPreferences("shows", MODE_PRIVATE);
        String json = sharedPref.getString("ID" + show.getId(), "");
        SharedPreferences.Editor editor = sharedPref.edit();

        if (json.isEmpty()){
            favorites.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_save));
            favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    String json = gson.toJson(show);
                    editor.putString("ID" + show.getId(), json).commit();
                    Toast.makeText(
                            getApplicationContext(),
                            show + " saved in favorites",
                            Toast.LENGTH_SHORT
                    ).show();
                    favorites.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorites));
                }
            });
        }
        else {
            favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.remove("ID" + show.getId()).commit();
                    Toast.makeText(getApplicationContext(), show + " removed from favorites", Toast.LENGTH_SHORT).show();
                    favorites.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_save));
                }
            });

        }
    }

    private void configureImdbBUtton(Button imdb_button, TVShow show) {
        if (show.getImdb() == null || show.getImdb().isEmpty()){
            imdb_button.setVisibility(View.INVISIBLE);
        }
        else{
            imdb_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.imdb.com/title/"+show.getImdb()));
                    startActivity(i);
                }
            });
        }
    }
}
