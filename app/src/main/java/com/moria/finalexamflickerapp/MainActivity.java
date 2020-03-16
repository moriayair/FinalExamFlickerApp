package com.moria.finalexamflickerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends BaseActivity implements GetFlickerJsonData.OnDataAvailable,RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private FlickerRecyclerViewAdapter flickerRecyclerViewAdapter;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar(false);

        prefs = this.getSharedPreferences("com.moria.finalexamflickerapp", Context.MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

        flickerRecyclerViewAdapter = new FlickerRecyclerViewAdapter(new ArrayList<Photo>(),this);
        recyclerView.setAdapter(flickerRecyclerViewAdapter);

//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=android,nougat,sdk&tagmode=any&format=json&nojsoncallback=1");
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume:  starts");
        super.onResume();

       String queryResult = prefs.getString(FLICKER_QUERY, "android");

       if (queryResult.length() > 0){
           GetFlickerJsonData getFlickerJsonData = new GetFlickerJsonData(this,"https://www.flickr.com/services/feeds/photos_public.gne","en-us",true);
//        getFlickerJsonData.executeOnSameThread("android, nougat");
           getFlickerJsonData.execute(queryResult);
       }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu: returned " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK){
           flickerRecyclerViewAdapter.loadNewData(data);
        }else {
            Log.e(TAG, "onDataAvailable: failed with status " + status );
        }
    }
    //        Toast.makeText(MainActivity.this,"Normal tap at postion " + position,Toast.LENGTH_SHORT).show();
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent2 = new Intent(this,ImageFullScreenActivity.class);
        intent2.putExtra(PHOTO_TRANSFER,flickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent2);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,flickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}
