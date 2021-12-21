package com.example.a7asynctaskv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.download);
        imageView = findViewById(R.id.image);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        //OnPreExecute
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                p = new ProgressDialog(MainActivity.this);
                                p.setMessage("Downloading...");
                                p.setIndeterminate(false);
                                p.setCancelable(false);
                                p.show();
                            }
                        });

                        // doInbackground method of asyntask
                        try {
                            ImageUrl = new URL("https://stis.ac.id/media/source/up.png");
                            HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            is = conn.getInputStream();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            bmImg = BitmapFactory.decodeStream(is, null,
                                    options);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //doPostExecute Methode
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (imageView != null) {
                                    p.hide();
                                    imageView.setImageBitmap(bmImg);
                                } else {
                                    p.show();
                                }


                            }
                        });

                    }
                });

            }
        });
    }


}
