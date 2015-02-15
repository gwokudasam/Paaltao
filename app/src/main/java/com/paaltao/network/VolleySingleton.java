package com.paaltao.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.paaltao.classes.APIManager;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(APIManager.getAppContext());

    }
    public static VolleySingleton getsInstance(){
        if (sInstance == null){
            sInstance = new VolleySingleton();
        }
        return  sInstance;

    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
