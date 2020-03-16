package com.moria.finalexamflickerapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.moria.finalexamflickerapp.Utilitys.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFullScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);


        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo)intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null){

            ImageView photoImage = findViewById(R.id.imageView2);
            Picasso.get()
                    .load(photo.getLink())
                    .transform(new RoundedCornersTransformation(10,1))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(photoImage);
        }

    }
}
