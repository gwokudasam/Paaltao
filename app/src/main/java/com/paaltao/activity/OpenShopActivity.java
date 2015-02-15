package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.paaltao.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OpenShopActivity extends ActionBarActivity implements ImageChooserListener {
    Button selectCoverButton;
    private  ImageChooserManager imageChooserManager;
    String imagePath;
    ImageView coverImageArea;
    private SweetAlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shop);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        initialize();
        onItemClick();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        selectCoverButton = (Button)findViewById(R.id.select_cover_btn);
        coverImageArea = (ImageView)findViewById(R.id.shop_cover_image);
    }

    public void onItemClick(){
        selectCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG","image chooser selected!");
            }
        });

    }
    public void chooseImage(){
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE);
        imageChooserManager.setImageChooserListener(this);
        try {
            imageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void snapImage(){
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE);
        imageChooserManager.setImageChooserListener(this);
        try {
            imageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseImageDialog(){
                dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Choose your Cover Image")
                .setContentText("Choose from gallery or take a camera snapshot!")
                .setConfirmText("Gallery")
                .setCancelText("Camera")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        chooseImage();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        snapImage();
                    }
                })
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            imageChooserManager.submit(requestCode, data);
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    // Use the image
                    imagePath = image.getFileThumbnail();
                    Log.d("TAG","PATH is"+imagePath);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);

                    ImageView myImage = (ImageView) findViewById(R.id.shop_cover_image);

                    myImage.setImageBitmap(myBitmap);

                    coverImageArea.setVisibility(View.VISIBLE);
                    dialog.hide();


                    // image.getFilePathOriginal();
                    // image.getFileThumbnail();
                    // image.getFileThumbnailSmall();
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Show error message
            }
        });
    }
}
