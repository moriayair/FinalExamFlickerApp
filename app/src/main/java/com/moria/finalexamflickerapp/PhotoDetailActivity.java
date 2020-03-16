package com.moria.finalexamflickerapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.moria.finalexamflickerapp.Utilitys.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo)intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null){
            TextView photoTitle = findViewById(R.id.photo_title);
//            photoTitle.setText("Title: " + photo.getTitle());
            Resources resources = getResources();
            String text = resources.getString(R.string.photo_title_text,photo.getTitle());
            photoTitle.setText(text);

            TextView photoTags = findViewById(R.id.photo_tags);
//            photoTags.setText("Tags: " + photo.getTags());
            photoTags.setText(resources.getString(R.string.photo_tags_text,photo.getTags()));

            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = findViewById(R.id.photo_imgae);
            Picasso.get()
                    .load(photo.getLink())
                    .transform(new RoundedCornersTransformation(10,1))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(photoImage);
        }

    }

}
