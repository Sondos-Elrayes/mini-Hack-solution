package com.azapps.moviesreviewer.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.azapps.moviesreviewer.R;
import com.azapps.moviesreviewer.pojo.Results;
import com.bumptech.glide.Glide;

import java.io.*;

import static com.azapps.moviesreviewer.repository.Constant.RESULT_EXTRA;


public class MovieDescriptionActivity extends AppCompatActivity {
    private ImageView cover, backArrow;
    private Button btSave;
    private OutputStream outputStream;
    private TextView title, date, rateNumber, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cover = findViewById(R.id.movie_img_cover);
        title = findViewById(R.id.description_title);
        date = findViewById(R.id.description_release_date);
        btSave = findViewById(R.id.bt_save);
        rateNumber = findViewById(R.id.description_rating_number);
        description = findViewById(R.id.overview_description);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) cover.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                File filePath  = Environment.getExternalStorageDirectory();
                File dir = new File(filePath.getAbsolutePath()+"/Demo/");
                dir.mkdir();
                File file = new File(dir,System.currentTimeMillis()+".jpg");
                try {
                    outputStream = new FileOutputStream(file );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(getApplicationContext(), "Image saved    ", Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        if (getIntent().hasExtra(RESULT_EXTRA)) {
            Results result = (Results) getIntent().getSerializableExtra(RESULT_EXTRA);

            Uri image_url = Uri.parse(result.getPoster_path());
            Glide.with(this).load(image_url).into(cover);

            title.setText(result.getTitle());
            date.setText(result.getRelease_date());
            rateNumber.setText(result.getVote_average());
            description.setText(result.getOverview());
        } else {
            Toast.makeText(this, "Can not load the data", Toast.LENGTH_SHORT).show();
        }
    }
}