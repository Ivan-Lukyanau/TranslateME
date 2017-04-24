package com.example.ivan_lukyanau.translateme.Http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */
/**  used to before i knew about AsyncTask **/
public class Get extends HttpClientWrapper {
    @Override
    public void run(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
