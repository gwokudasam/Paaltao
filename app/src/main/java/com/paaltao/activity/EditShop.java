package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_OPEN_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SELLER_ID;
import static com.paaltao.extras.urlEndPoints.EDIT_SHOP;
import static com.paaltao.extras.urlEndPoints.OPEN_SHOP;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class EditShop extends AppCompatActivity implements ImageChooserListener {

    Button selectCoverButton,uploadPassbookPhoto;
    private ImageChooserManager imageChooserManager;
    String imagePath,imagePath1,imagePath2,sellerID,accessToken,encodedImage;
    ImageView coverImageArea,passbookPhoto;
    private SweetAlertDialog dialog;
    private Bitmap myBitmap;
    EditText shopName,aboutShop,contactNo,shopAddress,city,state,postalCode,bankAccountNo,bankName,chequeName,bankAddress,bankIFSC;
    SharedPreferenceClass preferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Edit Shop");
        initialize();
        onItemClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_launch){
            if (validationCheck()) {
                sendJsonRequest();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        selectCoverButton = (Button)findViewById(R.id.select_cover_button);
        coverImageArea = (ImageView)findViewById(R.id.shop_cover_image);
        shopName = (EditText)findViewById(R.id.shop_name);
        aboutShop = (EditText)findViewById(R.id.about_shop);
        contactNo = (EditText)findViewById(R.id.shop_contact);
        shopAddress = (EditText)findViewById(R.id.shop_street_name);
        city = (EditText)findViewById(R.id.shop_city_name);
        state = (EditText)findViewById(R.id.shop_state);
        postalCode = (EditText)findViewById(R.id.shop_pincode);
        preferenceClass = new SharedPreferenceClass(this);
        sellerID = preferenceClass.getSellerId();
        bankAccountNo = (EditText)findViewById(R.id.bank_account_no);
        bankName = (EditText)findViewById(R.id.bank_name);
        bankAddress = (EditText)findViewById(R.id.bank_address);
        bankIFSC = (EditText)findViewById(R.id.bank_IFSC);
        chequeName = (EditText)findViewById(R.id.bank_cheque_name);
    }

    public void onItemClick(){
        selectCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog();
                Log.d("TAG", "image chooser selected!");
            }
        });


    }

    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject openShop = new JSONObject();
        try{
            jsonObject.put("accessToken",preferenceClass.getAccessToken());
            jsonObject.put("sellersName",preferenceClass.getFirstName()+""+preferenceClass.getLastName());
            jsonObject.put("userEmail",preferenceClass.getUserEmail());
            jsonObject.put("shopName",shopName.getText().toString());
            jsonObject.put("aboutShop",aboutShop.getText().toString());
            jsonObject.put("contactNo",contactNo.getText().toString());
            jsonObject.put("street",shopAddress.getText().toString());
            jsonObject.put("city",city.getText().toString());
            jsonObject.put("vendorId",sellerID);
            jsonObject.put("state",city.getText().toString());
            jsonObject.put("country","India");
            jsonObject.put("bank_account_no",bankAccountNo.getText());
            jsonObject.put("bank_name",bankName.getText());
            jsonObject.put("bank_address",bankAddress.getText());
            jsonObject.put("bank_ifsc",bankIFSC.getText());
            jsonObject.put("cheque_name",chequeName.getText());
            jsonObject.put("pincode",postalCode.getText().toString());
            if (encodedImage != null){
                jsonObject.put("newShopImage",encodedImage);}
            else
                jsonObject.put("shopImage","");
            openShop.put("editShop", jsonObject);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getRequestUrl(),openShop,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (sellerID != null){
                Log.e("sellerId",sellerID);}
                Log.e("error",jsonObject.toString());
                Log.e("json", openShop.toString());
                if (encodedImage != null){
                    Log.e("base64",encodedImage);
                }




                parseJSONResponse(jsonObject);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(EditShop.this)
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
        requestQueue.add(jsonObjectRequest);
    }


    public static String getRequestUrl() {

        return UAT_BASE_URL
                + EDIT_SHOP;

    }


    public void parseJSONResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);


            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

            if (errorCode.contains("200")) {
                new SnackBar.Builder(EditShop.this)
                        .withMessage("Your shop details have been updated successfully")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }


            else{
                new SnackBar.Builder(EditShop.this)
                        .withMessage("Your shop could not be updated")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }

    } catch (JSONException e) {
            e.printStackTrace();
        }}


    public boolean validationCheck(){

        if(shopName.getText().toString().length() == 0)
            shopName.setError("Please provide a shop name");
        else if (aboutShop.getText().toString().length() == 0)
            aboutShop.setError("Please provide some info about your shop");
        else if(contactNo.getText().toString().length() == 0 && contactNo.getText().toString().length()>10)
            contactNo.setError("Please provide 10 digit contact number");
        else if(postalCode.getText().toString().length() == 0)
            postalCode.setError("Please provide a postal code");
        else return true;
        return false;
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

                // Use the image
                imagePath = image.getFileThumbnail();
                imagePath1 = image.getFilePathOriginal();
                imagePath2 = image.getFileThumbnailSmall();

                Bitmap myBitmap = BitmapFactory.decodeFile(image.getFileThumbnail());
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                byte[] byteArr = byteArray.toByteArray();
                encodedImage = Base64.encodeToString(byteArr, Base64.DEFAULT);
                Log.d("Data", encodedImage);


                ImageView myImage = (ImageView) findViewById(R.id.shop_cover_image);

                myImage.setImageBitmap(myBitmap);

                coverImageArea.setVisibility(View.VISIBLE);
                dialog.hide();
                dialog.dismiss();


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
