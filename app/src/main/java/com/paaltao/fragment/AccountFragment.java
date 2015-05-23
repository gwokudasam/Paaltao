package com.paaltao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.paaltao.activity.AddressActivity;
import com.paaltao.activity.HomeActivity;
import com.paaltao.activity.IntroPageActivity;
import com.paaltao.activity.ProfileActivity;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_OPEN_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SELLER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_OUT;
import static com.paaltao.extras.urlEndPoints.OPEN_SHOP;
import static com.paaltao.extras.urlEndPoints.SIGN_OUT;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

//This is a user account fragment.
public class AccountFragment extends Fragment {
    RelativeLayout accountLink,my_address,signOut;
    View view;
    String accessToken;
    SharedPreferenceClass preferenceClass;
    SweetAlertDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initialize();
        onItemClick();
        return view;
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + SIGN_OUT;

    }

    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject signOut = new JSONObject();
        try{
            jsonObject.put("accessToken","67drd56g");
            signOut.put("signOut", jsonObject);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getRequestUrl(),signOut,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("error", jsonObject.toString());
                Log.e("json", signOut.toString());


                parseJSONResponse(jsonObject);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(getActivity())
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
            JSONObject signOutObject = jsonObject.getJSONObject(KEY_SIGN_OUT);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);


            accessToken = signOutObject.getString(KEY_ACCESS_TOKEN);



            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

            if (errorCode.equals("200")){
                preferenceClass.clearAccessToken();
                Log.e("accessToken",accessToken);
                Intent intent = new Intent(getActivity(),IntroPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();

            }
            else{
                new SnackBar.Builder(getActivity())
                        .withMessage("Error in signing out")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    public void initialize(){
        accountLink = (RelativeLayout)view.findViewById(R.id.account_link);
        my_address = (RelativeLayout)view.findViewById(R.id.my_address);
        signOut = (RelativeLayout)view.findViewById(R.id.signOut);
        preferenceClass = new SharedPreferenceClass(getActivity());
    }

    public void onItemClick(){
        accountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
        my_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSignOut();
            }
        });
    }

    public void confirmSignOut(){
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("Signout")
                .setContentText("Are you sure you want to sign out?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sendJsonRequest();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }



    }




