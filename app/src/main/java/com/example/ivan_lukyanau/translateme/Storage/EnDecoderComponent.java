package com.example.ivan_lukyanau.translateme.Storage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Ivan_Lukyanau on 4/8/2017.
 */

public final class EnDecoderComponent {

    public static String encodeURI(String component) {
        String result = null;

        try{
            result = URLEncoder.encode(component, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");
        }
        catch(UnsupportedEncodingException ex){

        }
        return result;
    }
}
