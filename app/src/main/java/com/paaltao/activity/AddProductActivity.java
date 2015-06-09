package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.paaltao.R;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.paaltao.extras.urlEndPoints.ADD_PRODUCT;
import static com.paaltao.extras.urlEndPoints.OPEN_SHOP;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class AddProductActivity extends AppCompatActivity implements ImageChooserListener{
    private ImageChooserManager imageChooserManager;
    String imagePath,encodedImage,sellerId;
    ImageView product_select1,product_select2,product_select3,product_select4;
    private SweetAlertDialog dialog;
    private int item;
    SharedPreferenceClass preferenceClass;
    private ArrayList<String> productImages = new ArrayList<>();
    private JSONArray jsonArray;
    private EditText productName,productPrice,shippingPrice,productDescription,productWeight,productQuantity,shippingDetails;
    private Spinner category;

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
        this.setTitle("Add a product");
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
           case R.id.action_done:
               if (validationCheck()) {
                   sendJsonRequest();
               }
       }
        return super.onOptionsItemSelected(item);
    }


    public void initialize(){
        product_select1 = (ImageView)findViewById(R.id.product_pic1);
        product_select2 = (ImageView)findViewById(R.id.product_pic2);
        product_select3 = (ImageView)findViewById(R.id.product_pic3);
        product_select4 = (ImageView)findViewById(R.id.product_pic4);
        productName = (EditText)findViewById(R.id.product_name);
        productPrice = (EditText)findViewById(R.id.product_price);
        productQuantity = (EditText)findViewById(R.id.product_quantity);
        productDescription = (EditText)findViewById(R.id.product_description);
        productWeight = (EditText)findViewById(R.id.product_weight);
        category = (Spinner)findViewById(R.id.category_selector);
        shippingPrice = (EditText)findViewById(R.id.shipping_price);
        shippingDetails = (EditText)findViewById(R.id.shipping_details);
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        sellerId = preferenceClass.getSellerId();

    }

    public boolean validationCheck(){
        if(productName.getText().toString().length() == 0)
            productName.setError("Please provide a product name");
        else if (productDescription.getText().toString().length() == 0)
            productDescription.setError("Please provide some info about your product");
        else if(productQuantity.getText().toString().length() == 0)
            productQuantity.setError("Please provide product quantity");
        else if(productWeight.getText().toString().length() == 0)
            productWeight.setError("Please provide product weight");
        else if (shippingPrice.getText().toString().length() == 0)
            shippingPrice.setError("Please provide a shipping price");
        else if (productPrice.getText().toString().length() == 0)
            productPrice.setError("Please provide a product price");
        else if (shippingDetails.getText().toString().length() == 0)
            shippingDetails.setError("Please provide shipping details");
        else return true;
        return false;
    }
    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject addProduct = new JSONObject();
        final JSONArray images = new JSONArray();
        jsonArray = new JSONArray(productImages);
        try{
            jsonObject.put("accessToken",preferenceClass.getAccessToken());
            jsonObject.put("name",productName.getText());
            jsonObject.put("description",productDescription.getText());
            jsonObject.put("weight",productWeight.getText());
            jsonObject.put("quantity",productQuantity.getText());
            jsonObject.put("price",productPrice.getText());
            jsonObject.put("category",category.getSelectedItem().toString());
            jsonObject.put("shippingCost",shippingPrice.getText());
            jsonObject.put("shippingDetails",shippingDetails.getText());
            jsonObject.put("vendorId",sellerId);
            jsonObject.put("productImages",jsonArray);
            addProduct.put("addProduct", jsonObject);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getRequestUrl(),addProduct,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("count",String.valueOf(productImages.size()));
                Log.e("error",jsonObject.toString());
                Log.e("JSONARRAY",jsonArray.toString());
                Log.e("json", addProduct.toString());
                L.m(sellerId);




            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(AddProductActivity.this)
                            .withMessage("No Internet Connection!")
                            .withTextColorId(R.color.white)
                            .withDuration((short) 6000)
                            .show();

                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {

                    //TODO
                } else if (volleyError instanceof ParseError) {

                    //TODO
                }

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        9000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
    }


    public static String getRequestUrl() {

        return UAT_BASE_URL
                + ADD_PRODUCT;

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
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                    byte[] byteArr = byteArray.toByteArray();
                    encodedImage = Base64.encodeToString(byteArr, Base64.DEFAULT);

                    ImageView myImage1 = (ImageView) findViewById(R.id.product_pic1);
                    ImageView myImage2 = (ImageView) findViewById(R.id.product_pic2);
                    ImageView myImage3 = (ImageView) findViewById(R.id.product_pic3);
                    ImageView myImage4 = (ImageView) findViewById(R.id.product_pic4);

                    switch (item){
                        case R.id.product_pic1:
                            myImage1.setImageBitmap(myBitmap);
                            myImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            productImages.add(encodedImage);
                            break;
                        case R.id.product_pic2:
                            myImage2.setImageBitmap(myBitmap);
                            myImage2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            productImages.add(encodedImage);
                            break;
                        case R.id.product_pic3:
                            myImage3.setImageBitmap(myBitmap);
                            myImage3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            productImages.add(encodedImage);
                            break;

                        case R.id.product_pic4:
                            myImage4.setImageBitmap(myBitmap);
                            myImage4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            productImages.add(encodedImage);
                            break;
                    }

                    dialog.hide();
                    dialog.dismiss();





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


