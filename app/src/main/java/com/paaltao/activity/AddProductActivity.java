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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.paaltao.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductActivity extends ActionBarActivity implements ImageChooserListener{
    private ImageChooserManager imageChooserManager;
    String imagePath;
    ImageView product_select1,product_select2,product_select3,product_select4;
    private SweetAlertDialog dialog;
    private int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Spinner spinner = (Spinner) findViewById(R.id.category_selector);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.quantity,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
        onItemClick();

    }


    // Create an ArrayAdapter using the string array and a default spinner layout


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       switch (id){
           case R.id.home:
               finish();
       }
        return super.onOptionsItemSelected(item);
    }


    public void initialize(){
        product_select1 = (ImageView)findViewById(R.id.product_pic1);
        product_select2 = (ImageView)findViewById(R.id.product_pic2);
        product_select3 = (ImageView)findViewById(R.id.product_pic3);
        product_select4 = (ImageView)findViewById(R.id.product_pic4);
    }

    public void onItemClick(){
        product_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG","image chooser selected!");
                 item = v.getId();
            }
        });
        product_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG","image chooser selected!");
                item = v.getId();
            }
        });
        product_select3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG","image chooser selected!");
                item = v.getId();
            }
        });
        product_select4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG","image chooser selected!");
                item = v.getId();
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
        dialog.setTitleText("Choose your Product Image")
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
                    Log.d("TAG", "PATH is" + imagePath);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);

                    ImageView myImage1 = (ImageView) findViewById(R.id.product_pic1);
                    ImageView myImage2 = (ImageView) findViewById(R.id.product_pic2);
                    ImageView myImage3 = (ImageView) findViewById(R.id.product_pic3);
                    ImageView myImage4 = (ImageView) findViewById(R.id.product_pic4);

                    switch (item){
                        case R.id.product_pic1:
                            myImage1.setImageBitmap(myBitmap);
                            myImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                        case R.id.product_pic2:
                            myImage2.setImageBitmap(myBitmap);
                            myImage2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                        case R.id.product_pic3:
                            myImage3.setImageBitmap(myBitmap);
                            myImage3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;

                        case R.id.product_pic4:
                            myImage4.setImageBitmap(myBitmap);
                            myImage4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                    }
//                    myImage1.setImageBitmap(myBitmap);
//                    myImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
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


