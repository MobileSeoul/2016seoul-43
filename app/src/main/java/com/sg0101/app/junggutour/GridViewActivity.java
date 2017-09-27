/*
    Copyright 2013-2014 Gabriele Mariotti

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package com.sg0101.app.junggutour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sg0101.app.junggutour.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.gmariotti.cardslib.library.cards.topcolored.TopColoredCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;

public class GridViewActivity extends AppCompatActivity {

    public static final String TOUR_LIST_ID = "tour_list_id";

    CardGridArrayAdapter mCardArrayAdapter;
    List<JungguStoryTelling> jstList;

    private String title;
    private String subTitle;
    private String outline;

    private int randomPlayIndex = 0;

    BroadcastReceiver translatedTextReceiver = null;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setLogo(R.drawable.ic_main_logo_shade);

        //initCard();

        //Set the arrayAdapter
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        //Set the empty view
        CardGridView gridView = (CardGridView)findViewById(R.id.myGrid);
        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }

        //Load cards
        new LoaderAsyncTask().execute();

        // broadcast receiver
        intentFilter = new IntentFilter();
        intentFilter.addAction(Common.BROADCAST_INTENT_ACTION);
        translatedTextReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(" onReceive - ");
                if(intent.getStringExtra(Common.TRANSLATED_TEXT) != null){
                    showSnackBar(intent.getStringExtra(Common.TRANSLATED_TEXT));
                }
            }
        };
        registerReceiver(translatedTextReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_naver_map:
                startMapTourListPage();
                return true;

            case R.id.action_naver_tts:
                startNaverTtsForContentsOutline();
                return true;

            case R.id.action_go_setting:
                startSettingsPage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        //initCard();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(translatedTextReceiver);
        super.onDestroy();
    }

    class LoaderAsyncTask extends AsyncTask<Void, Void, ArrayList<Card>> {

        LoaderAsyncTask() {
        }

        @Override
        protected ArrayList<Card> doInBackground(Void... params) {
            //elaborate images
//            SystemClock.sleep(1000); //delay to simulate download, don't use it in a real app
            //      if (isAdded()) {
            ArrayList<Card> cards = initCard();
            return cards;
            //          }else
            //              return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Card> cards) {
            //Update the adapter
            updateAdapter(cards);
            //displayList();
        }
    }

    private void updateAdapter(ArrayList<Card> cards) {
        if (cards != null) {
            mCardArrayAdapter.addAll(cards);
        }
    }

    private ArrayList<Card> initCard() {

        jstList = Common.getJSTList(this, Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE));

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < jstList.size(); i++) {

            //Create a CardHeader
            CardHeader header = new CardHeader(getBaseContext());
            header.setButtonExpandVisible(true);
            header.setTitle("No. " + (i+1) );

            if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_CNG ){
                title = jstList.get(i).getName_cng();
                subTitle = "";
                outline = "";
            }else if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_ENG ){
                title = jstList.get(i).getName_eng();
                subTitle = "";
                outline = "";
            }else{
                title = jstList.get(i).getName_kor();
                subTitle = jstList.get(i).getAdd_kor_road();
                outline = jstList.get(i).getOutline();
            }

            TypedArray img;
            img = getResources().obtainTypedArray(R.array.junggu_tour_img);

            TopColoredCard card = TopColoredCard.with(getBaseContext())
                    .setColorResId(R.color.card_color_1)
                    .setTitleOverColor(title)
                    .setSubTitleOverColor(subTitle)
                    .build();
            //Create thumbnail
            //CardThumbnail thumb = new CardThumbnail(getBaseContext());
            CustomThumbCard thumb = new CustomThumbCard(getBaseContext());
            //Set resource
            thumb.setDrawableResource(img.getResourceId(i, 1));
            //Add thumbnail to a card
            card.addCardThumbnail(thumb);


            if( i%5 == 1 ) {
                card.setColorResourceId(R.color.card_color_2);
            }else if(i%5 == 2){
                card.setColorResourceId(R.color.card_color_3);
            }else if( i%5 == 3 ){
                card.setColorResourceId(R.color.card_color_4);
            }else if( i%5 == 4 ){
                card.setColorResourceId(R.color.card_color_5);
            }else{
            //    card.setBackgroundColorResourceId(R.color.card_color_1);
            }

            card.addCardHeader(header);
            card.setId(String.valueOf(i));

            //This provide a simple (and useless) expand area
            //CardExpand expand = new CardExpand(getBaseContext());
            CustomExpandCard expand = new CustomExpandCard(getBaseContext(), outline, i);
            //Set inner title in Expand Area
            expand.setTitle(outline);


            //Add expand to a card
            card.addCardExpand(expand);

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    startDetailDesignSupportPage(card.getId());
                }
            });

            cards.add(card);
        }

        return cards;
    }

    private void startMapTourListPage(){
        Intent intent = new Intent(this, NMapTourListViewActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startDetailDesignSupportPage(String id){
        Intent intent = new Intent(this, DetailDesignSupportActivity.class);
        intent.putExtra(TOUR_LIST_ID, id);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startSettingsPage(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startNaverTtsForContentsOutline(){
        Random rand = new Random(System.currentTimeMillis());
        randomPlayIndex = Math.abs(rand.nextInt(100));
        Common.startTts(getBaseContext(), randomPlayIndex, Common.OUTLINE_CONTENTS);
    }

    private void showSnackBar(String str){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), str, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                    }
                });
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        TextView sbActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
        textView.setMaxLines(11);

        sbActionTextView.setTextColor(Color.CYAN);
        sbActionTextView.setTypeface(null, Typeface.BOLD);

        snackbar.show();

    }
/*
    private void saveFile(){

        //String tempTitle = jstList.get(saveFileIdx).getOutline().toString();
        //String tempTitle = jstList.get(saveFileIdx).getName_eng().toString();
        String tempTitle = jstList.get(saveFileIdx).getName_cng().toString();
//        String tempContents = jstList.get(saveFileIdx).getName_kor().toString() + System.getProperty("line.separator") + tempTitle;
        mainKey = jstList.get(saveFileIdx).getMain_key();
        String tempKorOutline = getOutlineKorString(saveFileIdx);
        saveFileIdx++;

        //startNaverTts(tempContents);

        try {
            StartNaverTranslate(tempTitle, tempKorOutline, Common.NAVER_TRANSLATE_REQUEST_PARAMS_SOURCE_ZHCN);
            return;
        }catch (JSONException e){
        }
        showSnackBar(String.valueOf(saveFileIdx));
    }
*/
}
