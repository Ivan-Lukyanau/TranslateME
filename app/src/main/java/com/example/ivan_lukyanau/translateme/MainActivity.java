package com.example.ivan_lukyanau.translateme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan_lukyanau.translateme.Http.JSONResponseAnalyzer;
import com.example.ivan_lukyanau.translateme.Models.InputValidationModelResult;
import com.example.ivan_lukyanau.translateme.Models.TranslationModelResult;
import com.example.ivan_lukyanau.translateme.Models.WordDescription;
import com.example.ivan_lukyanau.translateme.Providers.HistoryCacheProvider;
import com.example.ivan_lukyanau.translateme.Providers.TranslatorAPIProvider;
import com.example.ivan_lukyanau.translateme.Providers.YandexTranslatorAPIProvider;
import com.example.ivan_lukyanau.translateme.Storage.EnDecoderComponent;
import com.example.ivan_lukyanau.translateme.Storage.StorageHelper;
import com.example.ivan_lukyanau.translateme.Validator.InputTextValidator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private TextView yandexLinkText;
    private EditText et;
    private ImageView image;

    private SharedPreferences myPreferences;
    public static final String APP_PREFERENCES = "MainActivity";
    public static final String APP_PREFERENCES_FAVORITES = "favorites";
    public static final String APP_PREFERENCES_HISTORY = "history";
    // key for MS Cognitive service - search for images
    public static final String APP_PREFERENCES_IMAGE_SERVICE_KEY = "83ead77a728045c39dc8f315331546c0";

    private CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);

        viewPager.setAdapter(customPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                String tabSelected = "";
                switch (position){
                    case 0: {
                        tabSelected = "Translator";

                    }break;
                    case 1: {
                        tabSelected = "History";
                        customPagerAdapter.notifyDataSetChanged();
                    }break;
                    case 2: {
                        tabSelected = "Favorites";
                        customPagerAdapter.notifyDataSetChanged();
                    }break;
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /*
    * EVENT HANDLERS
    * ============================================================================================ */
    ///
    /// CLEAN HISTORY
    ///
    public void cleanHistoryOnClick(View view){
        StorageHelper helper = new StorageHelper(myPreferences);
        helper.cleanHistory();
        Snackbar.make(view, "History was cleaned", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        customPagerAdapter.notifyDataSetChanged();
    }

    ///
    /// CLEAN FAVORITES
    ///
    public void cleanFavoritesOnClick(View view) {
        StorageHelper helper = new StorageHelper(myPreferences);
        // clear favorites
        helper.cleanFavorites();
        // untick all words in History
        helper.untickAllInCollection(MainActivity.APP_PREFERENCES_HISTORY);

        Snackbar.make(view, "Favorites was cleaned", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        customPagerAdapter.notifyDataSetChanged();
    }

    ///
    /// TRANSLATE WORD
    ///
    public void translateOnClick(View view){

        image = (ImageView) findViewById(R.id.image_thumb);
        et = (EditText) findViewById(R.id.tr_input);
        tv = (TextView) findViewById(R.id.tr_result);
        yandexLinkText = (TextView) findViewById(R.id.yandex_link_text);

        String inputText = et.getText().toString();

       // VALIDATION logic
        InputTextValidator inputValidator = new InputTextValidator();
        InputValidationModelResult validResult = inputValidator.Validate(inputText);
        if(validResult != null){
            // notify about problem of input parameters
            Snackbar.make(view, validResult.GetMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else{
             /* fine we can proceed*/

            // let's hide keyboard after click 'TRANSLATE'
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

            // let's search for this word in cache before
            HistoryCacheProvider historyHache = new HistoryCacheProvider(myPreferences);
            WordDescription foundWord = historyHache.FindInCache(inputText);
            if(foundWord != null){
                String resultString = String.format("%s - %s", inputText, foundWord.getTranslation());
                tv.setText(resultString);
                SetVisibilityForYandexLinkBar(yandexLinkText);

                // add to history if word was found in cache
                StorageHelper helper = new StorageHelper(myPreferences);
                helper.addToHistory(foundWord.getWord(), foundWord.getTranslation());

                // get image using microsoft's cognitive services --- 2 STEP
                GetThumbnailImageUrlAsync getterImageURL = new GetThumbnailImageUrlAsync();
                getterImageURL.execute(inputText);
            } else {
                // translate using YANDEX API --- 1 STEP
                YandexTranslator translator = new YandexTranslator();
                translator.execute(inputText);
            }
        }
    }

    ///
    /// using GetThumbnailImageUrlAsync try to apply this url to element ImageView result
    ///
    private class DownloadImageByLinkAsync extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bm = null;
            try{
                InputStream is = new URL(url).openStream();
                bm = BitmapFactory.decodeStream(is);

            }catch (Exception e){
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            super.onPostExecute(result);
            // a try to make a circle
//            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), result);
//            roundedBitmapDrawable.setCircular(true);
//            image.setImageDrawable(roundedBitmapDrawable);
            //set image
            image.setImageBitmap(result);
        }
    }

    ///
    /// get thumbnail image result url
    ///
    private class GetThumbnailImageUrlAsync extends AsyncTask<String, Void, String> {
        // microsoft cognitive service link
        private final String  url = "https://api.cognitive.microsoft.com/bing/v5.0/images/search?q=";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            //prepare REQUEST
            String word = params[0];
            String readyToRequest = this.prepareWordToRequest(word);
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "multipart/form-data")
                    .addHeader("Ocp-Apim-Subscription-Key", MainActivity.APP_PREFERENCES_IMAGE_SERVICE_KEY)
                    .url(readyToRequest)
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                // create analyzer to parse result
                JSONResponseAnalyzer respAnalyze = new JSONResponseAnalyzer();
                try {
                    // get the image result url
                    String imageResult = respAnalyze.getImageResult(response.body().string());

                    result = imageResult;
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            new DownloadImageByLinkAsync().execute(result);
        }

        private String prepareWordToRequest(String word){
            return this.url+ EnDecoderComponent.encodeURI(word);
        }
    }

    ///
    /// get translation word using yandex API with AUTO DETECTON of word translation
    ///
    private class YandexTranslator extends AsyncTask<String, Void, TranslationModelResult>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected TranslationModelResult doInBackground(String... params) {
            TranslationModelResult result = null;

            //prepare REQUEST
            String word = params[0];
            // get endpoint
            TranslatorAPIProvider apiProvider = new YandexTranslatorAPIProvider(word);
            String url = apiProvider.getEndpoint(); // get yandex api endpoint
            // prepare request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                JSONResponseAnalyzer respAnalyze = new JSONResponseAnalyzer();
                try {
                    // get translated text using analyzer
                    result = respAnalyze.getYandexTranslationResult(response.body().string());
                }
                catch (ParseException e) {
                    e.printStackTrace();
                    result = new TranslationModelResult(false, "Sorry, something went wrong. Please, try again.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = new TranslationModelResult(false, "Sorry, something went wrong. Please, try again.");
            }
            return result;
        }

        @Override
        protected void onPostExecute(TranslationModelResult result){
            super.onPostExecute(result);

            if(result != null){
                if(result.isResolved()){ /*if there is some translation */

                    String resultString = String.format("%s - %s", et.getText(), result.getResult());
                    tv.setText(resultString);

                    // set visible for yandex link bar on the bottom of main screen
                    SetVisibilityForYandexLinkBar(yandexLinkText);

                    // let's add info about word into storage history
                    StorageHelper helper = new StorageHelper(myPreferences);
                    helper.addToHistory(et.getText().toString(), result.getResult());

                    new GetThumbnailImageUrlAsync().execute(et.getText().toString());
                } else { /* there is some errors */

                    Snackbar.make(getWindow().getCurrentFocus(), result.getErrorMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }

    ///
    /// Set link to Yandex API element visible
    ///
    private void SetVisibilityForYandexLinkBar(TextView yandexLinkText){
        // show link to translator page
        if(yandexLinkText.getVisibility() != View.VISIBLE) {
            yandexLinkText.setVisibility(View.VISIBLE);
            yandexLinkText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }


}


