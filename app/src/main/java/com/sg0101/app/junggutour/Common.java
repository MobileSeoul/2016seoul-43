package com.sg0101.app.junggutour;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sg0101.app.junggutour.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lee on 2016-08-18.
 */
public class Common extends Application {

    private static final String TAG = "Common";

    private static SharedPreferences prefs;

    public static final String FIRST_LAUNCH = "first_launch";

    public static final String TOUR_LIST_ID = "tour_list_id";
    public static final String ITEM_TITLE = "item_title";
    public static final String ITEM_COORDINATE = "item_coordinate";
    public static final String PLAY_INDEX = "play_index";
    public static final String TRANSLATED_TEXT = "translated_text";
    public static final String BROADCAST_INTENT_ACTION = "com.sg0101.app.junggutour.action.TRANSLATED_TEXT";

    public static final String SETTING_LANGUAGE_VALUE = "language";
    public static final String SETTING_VOICE_VALUE = "voice";

    public static final int OUTLINE_CONTENTS = 1;
    public static final int DETAIL_CONTENTS = 2;

    // SEOUL DATA OPEN API
    public static final String JST_REQUEST_PARAMS_KEY = "KEY";
    public static final String JST_REQUEST_PARAMS_TYPE = "TYPE";
    public static final String JST_REQUEST_PARAMS_SERVICE = "SERVICE";
    public static final String JST_REQUEST_PARAMS_START_INDEX = "START_INDEX";
    public static final String JST_REQUEST_PARAMS_END_INDEX = "END_INDEX";
    public static final String JST_REQUEST_PARAMS_MAIN_KEY = "MAIN_KEY";
    public static final String JST_REQUEST_PARAMS_SERVICE_KOR = "SebcStoryTellingKor";
    public static final String JST_REQUEST_PARAMS_SERVICE_ENG = "SebcStoryTellingEng";
    public static final String JST_REQUEST_PARAMS_SERVICE_CNG = "SebcStoryTellingCng";

    // NAVER TTS
    public static final String NAVER_OPEN_API_BASE_URL = "https://openapi.naver.com/";
    public static final String NAVER_OPEN_API_TTS_URL = "v1/voice/tts.bin";
    public static final String NAVER_OPEN_API_TRANSLATE_URL ="v1/language/translate";
    public static final String NAVER_OPEN_API_HEADER_CLIENT_ID_TEXT = "X-Naver-Client-Id";
    public static final String NAVER_OPEN_API_HEADER_CLIENT_SECRET_TEXT = "X-Naver-Client-Secret";
    public static final String NAVER_OPEN_API_HEADER_CONTENT_TYPE_TEXT = "Content-Type";
    public static final String NAVER_OPEN_API_HEADER_CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded";

    public static final String NAVER_TTS_REQUEST_PARAMS_SPEAKER = "speaker";
    public static final String NAVER_TTS_REQUEST_PARAMS_SPEED = "speed";
    public static final String NAVER_TTS_REQUEST_PARAMS_TEXT = "text";

    public static final String NAVER_TTS_SPEAKER_KO_MIJIN = "mijin";
    public static final String NAVER_TTS_SPEAKER_KO_JINHO = "jinho";
    public static final String NAVER_TTS_SPEAKER_EN_CLARA = "clara";
    public static final String NAVER_TTS_SPEAKER_EN_MATT = "matt";
    public static final String NAVER_TTS_SPEAKER_CN_MEIMEI = "meimei";

    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE = "source";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_TARGET = "target";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_TEXT = "text";

    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_KO = "ko";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_EN = "en";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_ZHCN = "zh-CN";

    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_TARGET_KO = "ko";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_TARGET_EN = "en";
    public static final String NAVER_TRANSLATE_REQUEST_PARAMS_TARGET_ZHCN = "zh-CN";

    // setting
    public static final int SETTING_CHOICE_LANGUAGE = 1;
    public static final int SETTING_CHOICE_VOICE = 2;

    public static final int SETTING_CHOICE_LANGUAGE_KOR = 1;
    public static final int SETTING_CHOICE_LANGUAGE_ENG = 2;
    public static final int SETTING_CHOICE_LANGUAGE_CNG = 3;

    public static final int SETTING_CHOICE_VOICE_MALE = 1;
    public static final int SETTING_CHOICE_VOICE_FEMALE = 2;

    private static final String NAVER_CLIENT_ID = "your_client_id";           // "your_client_id"
    private static final String NAVER_CLIENT_SECRET = "your_client_secret";                 // "your_client_secret"
    private static final String SEOUL_DATA_KEY = "your_data_key";  // "your_data_key"

    public static final String STORE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() +"/JST/";


    private static StoryTellerPlayer stPlayer = StoryTellerPlayer.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        File storeDirectory = new File(STORE_DIRECTORY);
        if(!storeDirectory.exists()) {
            boolean success = storeDirectory.mkdirs();
            if(!success) {
                Log.e("failed to create file storage directory.");
                return;
            }
        }
    }

    public static boolean isFirstLaunch(){
        return prefs.getBoolean(FIRST_LAUNCH, true);
    }

    public static void putFirstLaunch(boolean value){
        prefs.edit().putBoolean(FIRST_LAUNCH, value).commit();
    }

    public static void setSettingValue(int which, int value){
        switch(which){
            case SETTING_CHOICE_LANGUAGE:
                prefs.edit().putInt(Common.SETTING_LANGUAGE_VALUE, value).commit();
                break;
            case SETTING_CHOICE_VOICE:
                prefs.edit().putInt(Common.SETTING_VOICE_VALUE, value).commit();
                break;
        }
    }

    public static int getSettingValue(int which){
        switch(which){
            case SETTING_CHOICE_LANGUAGE:
                return prefs.getInt(Common.SETTING_LANGUAGE_VALUE, 1);
            case SETTING_CHOICE_VOICE:
                return prefs.getInt(Common.SETTING_VOICE_VALUE, 2);
        }
        return 0;
    }

    public static List<JungguStoryTelling> getJSTList(Context context, int language){
        List<JungguStoryTelling> list = null;

        AssetManager assetManager = context.getResources().getAssets();
        InputStream source = null;

        try {
            if( language == SETTING_CHOICE_LANGUAGE_KOR ) {
                source = assetManager.open("junggu_storytelling_kor.json");
            }else if( language == SETTING_CHOICE_LANGUAGE_ENG ){
                source = assetManager.open("junggu_storytelling_eng.json");
            }
            else if( language == SETTING_CHOICE_LANGUAGE_CNG ){
                source = assetManager.open("junggu_storytelling_cng.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);

        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(reader);
        JsonArray jsonArr = jo.getAsJsonArray("DATA");

        list = gson.fromJson(jsonArr, new TypeToken<List<JungguStoryTelling>>(){}.getType());

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                JungguStoryTelling jst1 = (JungguStoryTelling) o1;
                JungguStoryTelling jst2 = (JungguStoryTelling) o2;
                return jst1.getMain_key().compareToIgnoreCase(jst2.getMain_key());
            }
        });

        return list;
    }

    public static double pointX(String coordinate){
        double pointX;
        String[] temp = coordinate.split(",");
        String tempX = temp[0];
        String tempY = temp[1];
        pointX = Double.parseDouble(tempX.substring(tempX.indexOf('E')+1));
        return pointX;
    }

    public static double pointY(String coordinate){
        double pointY;
        String[] temp = coordinate.split(",");
        String tempX = temp[0];
        String tempY = temp[1];
        pointY = Double.parseDouble(tempY.substring(tempY.indexOf('N')+1));
        return pointY;
    }

    public static String getTtsVoice(){
        if( getSettingValue(SETTING_CHOICE_LANGUAGE) == SETTING_CHOICE_LANGUAGE_KOR ){
            if( getSettingValue(SETTING_CHOICE_VOICE) == SETTING_CHOICE_VOICE_FEMALE ){
                return NAVER_TTS_SPEAKER_KO_MIJIN;
            }else{
                return NAVER_TTS_SPEAKER_KO_JINHO;
            }
        }else if( getSettingValue(SETTING_CHOICE_LANGUAGE) == SETTING_CHOICE_LANGUAGE_ENG ){
            if( getSettingValue(SETTING_CHOICE_VOICE) == SETTING_CHOICE_VOICE_FEMALE ){
                return NAVER_TTS_SPEAKER_EN_CLARA;
            }else{
                return NAVER_TTS_SPEAKER_EN_MATT;
            }
        }else{
            return NAVER_TTS_SPEAKER_CN_MEIMEI;
        }
    }

    public static void playSound(Context context, int index){
        if(stPlayer.mp == null){
            stPlayer.mp = new MediaPlayer();
        }else{
            stPlayer.mp.reset();
        }
        String src;
        if (Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_KOR) {
            src = "outline_kor_mp3/" + getMainKeyString(context, index) + "_outline_kor.mp3";
        }else if(Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_ENG){
            src = "outline_eng_mp3/" + getMainKeyString(context, index) + "_outline_eng.mp3";
        }else{
            src = "outline_cng_mp3/" + getMainKeyString(context, index) + "_outline_cng.mp3";
        }
        try{
            AssetFileDescriptor afd = context.getAssets().openFd(src);
            stPlayer.mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            stPlayer.mp.prepare();
            stPlayer.mp.start();
        }catch (IOException e){

        }
    }

    public static void playSound(String path){
        if(stPlayer.mp == null){
            stPlayer.mp = new MediaPlayer();
        }else{
            stPlayer.mp.reset();
        }

        try{
            stPlayer.mp.setDataSource(path);
            stPlayer.mp.prepare();
            stPlayer.mp.start();
        }catch (IOException e){

        }
    }

    public static void pauseSound(){
        if(stPlayer.mp == null){
            stPlayer.mp = new MediaPlayer();
        }
        stPlayer.mp.pause();
    }

    public static void startTts(Context context, int index, int what) {
        String contents;
        String ttsTitle;

        String outlineKor = getOutlineKorString(context, index);
        if (Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_ENG) {
            ttsTitle = Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_ENG).get(index).getName_eng();
            try {
                StartNaverTranslate(context, ttsTitle, outlineKor, Common.NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_EN, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_CNG) {
            ttsTitle = Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_CNG).get(index).getName_cng();
            try {
                StartNaverTranslate(context, ttsTitle, outlineKor, Common.NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_ZHCN, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if(what == OUTLINE_CONTENTS) {
                ttsTitle = Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_KOR).get(index).getName_kor();
                contents = ttsTitle + System.getProperty("line.separator") + outlineKor;
                startNaverTts(context, contents, index);
            }else{
                contents = Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_KOR).get(index).getContents();
                startNaverTts(context, contents, index);
            }
        }
    }

    public static void startNaverTts(final Context context, String text, final int index) {
        sendBroadcast(context, text);

        RequestParams params = new RequestParams();
        params.put(Common.NAVER_TTS_REQUEST_PARAMS_SPEAKER, Common.getTtsVoice());     // jinho   mijin
        params.put(Common.NAVER_TTS_REQUEST_PARAMS_SPEED, 0);                 // -5 ~ 5
        params.put(Common.NAVER_TTS_REQUEST_PARAMS_TEXT, text);

        NaverOpenApiClient.postTts(Common.NAVER_OPEN_API_TTS_URL, params, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.e("=====", "startNaverTts : onSuccess - naver play");
                Common.playSound(response.getAbsolutePath());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File response) {
                Log.e("=====", "Http Post onFailure , statusCode = " + statusCode);
                if (statusCode == 429) {
                    //Toast.makeText(getBaseContext(), " onFailure : code = 429 : Too Many Requests T.T Try again tomorrow " , Toast.LENGTH_SHORT).show();
                    Log.e("=====", "startNaverTts : onFailure : 429 - local play");
                    Common.playSound(context, index);

                } else {
                    //Toast.makeText(getBaseContext(), " onFailure :  " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void StartNaverTranslate(final Context context, final String title, String text, final String translateLanguage, final int index) throws JSONException {

        RequestParams params = new RequestParams();
        params.put(Common.NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE, Common.NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_KO);
        params.put(Common.NAVER_TRANSLATE_REQUEST_PARAMS_TARGET, translateLanguage);
        params.put(Common.NAVER_TRANSLATE_REQUEST_PARAMS_TEXT, text);

        Log.e("====", "StartNaverTranslate text = " + text);

        NaverOpenApiClient.postTranslate(Common.NAVER_OPEN_API_TRANSLATE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject result = message.getJSONObject("result");
                    String translatedText = title + System.getProperty("line.separator") + result.getString("translatedText");
                    startNaverTts(context, translatedText, index);
                } catch (JSONException e) {
                    Log.e("=====", "error " + e);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                //JSONObject firstEvent = timeline.get(0);
                //String tweetText = firstEvent.getString("text");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("=====", "statuscode = " + statusCode);
                String msg = null;
                if (statusCode == 404) {
                    //Toast.makeText(getApplicationContext(), "404 - Not FOUND!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    //Toast.makeText(getApplicationContext(), "500 !", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    //Toast.makeText(getApplicationContext(), "403!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 400) {
                    Log.e("=====", " source parameter is needed or Unsupported source language or " +
                            " target parameter is needed or Unsupported target language  or source and target must be different or " +
                            "There is no source-to-target translator or text parameter is needed or text parameter exceeds max bytes  ");

                    msg = "source parameter is needed or Unsupported source language";
                } else if (statusCode == 429) {
                    msg = "Too Many Requests T.T Try again tomorrow";
                } else {
                    Log.e("=====", throwable.toString());
                }
            }
        });
    }

    public static String getMainKeyString(Context context, int index){
        return Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_KOR).get(index).getMain_key();
    }

    public static String getOutlineKorString(Context context, int index) {
        return Common.getJSTList(context, Common.SETTING_CHOICE_LANGUAGE_KOR).get(index).getOutline();
    }

    public static String getNaverClientID(){
        return NAVER_CLIENT_ID;
    }

    public static String getNaverClientSecret(){
        return NAVER_CLIENT_SECRET;
    }

    public static String getSeoulDataKey(){
        return SEOUL_DATA_KEY;
    }

    public static void sendBroadcast(Context context, String text){
        Intent intent  =new Intent();
        intent.setAction(BROADCAST_INTENT_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        intent.putExtra(TRANSLATED_TEXT, text);
        context.sendBroadcast(intent);
    }

}
