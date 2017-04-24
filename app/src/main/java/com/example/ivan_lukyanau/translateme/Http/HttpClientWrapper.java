package com.example.ivan_lukyanau.translateme.Http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */

/**  used to before i knew about AsyncTask **/
public abstract class HttpClientWrapper {
    protected OkHttpClient client;
    protected HttpClientWrapper(){
        client = new OkHttpClient();
    }

    public abstract void run(String url, Callback callback);
}
