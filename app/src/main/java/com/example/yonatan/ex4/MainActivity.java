package com.example.yonatan.ex4;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<JsonElement> {

    Button showImageBtn;
    GridView gv;
    ImageAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showImageBtn = findViewById(R.id.showImagesBtn);
        gv = findViewById(R.id.gv);
        adapter = new ImageAdapter(this);
        gv.setAdapter(adapter);

        showImageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.imgur.com/3/")
                .build();
        ImgurService service = restAdapter.create(ImgurService.class);
        service.getAlbumImages("z9LRJQV", this);

    }

    @Override
    public void success(JsonElement jsonElement, Response response) {
        JsonObject jo = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jo.get("data").getAsJsonObject().get("images").getAsJsonArray();

        String images[] = new String[12];
        for (int i = 0; i <jsonArray.size() ; i++) {
            images[i] = jsonArray.get(i).getAsJsonObject().get("link").getAsString();
        }
        new GliderTask(this).execute(images);
    }

    @Override
    public void failure(RetrofitError error) {

    }

    class GliderTask extends AsyncTask<String, Void, Integer> {
        Context context;

        public GliderTask(Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            int position = 0;
            for (String url : urls) {
                try {
                    Bitmap bitmap = Glide.with(MainActivity.this)
                            .load(url)
                            .asBitmap().into(-1, -1).get();
                    adapter.setImage(position, bitmap);
                    position++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return 5;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
        }
    }
}
