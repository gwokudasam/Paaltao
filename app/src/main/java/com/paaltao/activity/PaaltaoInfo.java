package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
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
import com.paaltao.R;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_CONTENT;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_OUT;
import static com.paaltao.extras.Keys.UserCredentials.KEY_STATIC_PAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_TITLE;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.SIGN_OUT;
import static com.paaltao.extras.urlEndPoints.STATIC_PAGE;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class PaaltaoInfo extends AppCompatActivity {
    String page,accessToken,title,content;
    SharedPreferenceClass preferenceClass;
    HtmlTextView textView;
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paaltao_info);
        textView = (HtmlTextView)findViewById(R.id.html_text);
        preferenceClass = new SharedPreferenceClass(this);
        progressWheel = (ProgressWheel)findViewById(R.id.action_progress);
        accessToken = preferenceClass.getAccessToken();
        Intent intent = getIntent();
        page = intent.getStringExtra("page");
        sendJsonRequest();
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + STATIC_PAGE;

    }


    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject staticPage = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("id",page);
            jsonObject.put("type","page");
            staticPage.put("staticPage", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),staticPage, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("error", jsonObject.toString());
                Log.e("json", staticPage.toString());

                if(progressWheel.getVisibility()== View.VISIBLE){
                    progressWheel.setVisibility(View.GONE);
                }

                parseJSONResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(PaaltaoInfo.this)
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
        requestQueue.add(jsonObjectRequest);
    }


    public void parseJSONResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            JSONObject staticPageObject = dataObject.getJSONObject(KEY_STATIC_PAGE);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);

            title = staticPageObject.getString(KEY_TITLE);
            content = staticPageObject.getString(KEY_CONTENT);

            textView.setHtmlFromString(content,true);

            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

            if (errorCode.equals("200")){


            }
            else{
                new SnackBar.Builder(PaaltaoInfo.this)
                        .withMessage("Error in signing out")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
