package com.sg0101.app.junggutour;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class DetailDesignSupportActivity extends AppCompatActivity {

    public static final String TOUR_LIST_ID = "tour_list_id";
    public static final String ITEM_TITLE = "item_title";
    public static final String ITEM_COORDINATE = "item_coordinate";

    private String tourListId;
    private List<JungguStoryTelling> jstList;

    private int listId;
    private String title;
    private String coordinate;
    private String address;
    private String tel;
    private String designState;
    private String openHour;
    private String parking;
    private String contents;
    private String forTranslateText;

    private TextView tvDetailNicknameText;
    private TextView tvDetailContentsText;
    private TextView tvDetailMoreInfoText1;
    private TextView tvDetailMoreInfoText2;
    private TextView tvDetailMoreInfoText3;
    private TextView tvDetailMoreInfoText4;
    private TextView tvDetailMoreInfoText5;
    private LinearLayout layoutMoreInfoCardHeader;
    private ImageView ivMoreInfoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_design_support);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.detail_page_collapsing_toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ctl.setCollapsedTitleTextColor(getResources().getColor(R.color.md_black_1000, this.getTheme()));
        }else{
//            ctl.setCollapsedTitleTextColor(getResources().getColor(R.color.md_black_1000));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_previous, null));
        }else{
//            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_previous));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.msg_wait_guide_voice), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startNaverTtsForDeatilContents();
            }
        });

        if( getIntent().getStringExtra(TOUR_LIST_ID) != null ){
            tourListId = getIntent().getStringExtra(TOUR_LIST_ID);
            ctl.setBackgroundResource(getResourceId(Integer.parseInt(tourListId)));
        }

        jstList = Common.getJSTList(this, Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE));

        listId = Integer.parseInt(tourListId);
        if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_CNG ){
            title = jstList.get(listId).getName_cng();
            contents = jstList.get(listId).getContents();
            coordinate = jstList.get(listId).getCoordinate();
            address = jstList.get(listId).getAdd_kor_road();
            tel = jstList.get(listId).getTel();
            designState = jstList.get(listId).getDesign_state();
            openHour = jstList.get(listId).getOpen_hour();
            parking = jstList.get(listId).getParking();
            tel = jstList.get(listId).getTel();
            forTranslateText = getContentsKorString(listId);
        }else if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_ENG ){
            title = jstList.get(listId).getName_eng();
            contents = jstList.get(listId).getContents();
            coordinate = jstList.get(listId).getCoordinate();
            address = jstList.get(listId).getAdd_kor_road();
            tel = jstList.get(listId).getTel();
            designState = jstList.get(listId).getDesign_state();
            openHour = jstList.get(listId).getOpen_hour();
            parking = jstList.get(listId).getParking();
            tel = jstList.get(listId).getTel();
            forTranslateText = getContentsKorString(listId);
        }else{
            title = jstList.get(listId).getName_kor();
            contents = jstList.get(listId).getContents();
            coordinate = jstList.get(listId).getCoordinate();
            address = jstList.get(listId).getAdd_kor_road();
            tel = jstList.get(listId).getTel();
            designState = jstList.get(listId).getDesign_state();
            openHour = jstList.get(listId).getOpen_hour();
            parking = jstList.get(listId).getParking();
        }
        if( contents == null ){
            contents = System.getProperty("line.separator");
            TextView tvDetailNoContentsText;
            tvDetailNoContentsText = (TextView) findViewById(R.id.jungu_tour_detail_view_no_text);
            tvDetailNoContentsText.setVisibility(View.VISIBLE);

        }

        tvDetailNicknameText = (TextView) findViewById(R.id.jungu_tour_detail_view_nickname_text);
        tvDetailContentsText = (TextView) findViewById(R.id.jungu_tour_detail_view_text);

        tvDetailMoreInfoText1 = (TextView) findViewById(R.id.more_info_card_text1);
        tvDetailMoreInfoText2 = (TextView) findViewById(R.id.more_info_card_text2);
        tvDetailMoreInfoText3 = (TextView) findViewById(R.id.more_info_card_text3);
        tvDetailMoreInfoText4 = (TextView) findViewById(R.id.more_info_card_text4);
        tvDetailMoreInfoText5 = (TextView) findViewById(R.id.more_info_card_text5);

        layoutMoreInfoCardHeader = (LinearLayout) findViewById(R.id.more_info_card_header_layout);
        layoutMoreInfoCardHeader.setBackgroundResource(getResourceId(Integer.parseInt(tourListId)));
        ivMoreInfoCard = (ImageView) findViewById(R.id.img_more_info_card_body);
        ivMoreInfoCard.setBackgroundResource(getResourceId(Integer.parseInt(tourListId)));

        ctl.setTitle(title);
        initDetailCard();
        initMoreInfoCard();

        if( listId%5 == 1 ){
            fab.setBackgroundTintList(ColorStateList.valueOf(0x703e802f));
        }else if( listId%5 == 2 ){
            fab.setBackgroundTintList(ColorStateList.valueOf(0x70f4b400));
        }else if( listId%5 == 3 ){
            fab.setBackgroundTintList(ColorStateList.valueOf(0x70427fed));
        }else if( listId%5 == 4 ){
            fab.setBackgroundTintList(ColorStateList.valueOf(0x70ff6d00));
        }else {
            fab.setBackgroundTintList(ColorStateList.valueOf(0x70b39ddb));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_map:
                startNMapActivity(title, coordinate);
                return true;

            case R.id.action_do_share:
                doShare();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDetailCard(){
        tvDetailContentsText.setText(contents);
        if( jstList.get(listId).getNickname() == null ){
            tvDetailNicknameText.setVisibility(View.GONE);
        }else{
            tvDetailNicknameText.setText(jstList.get(listId).getNickname());
        }
    }

    private void initMoreInfoCard(){
        if( address != null ){
            tvDetailMoreInfoText1.setText( address );
        }else{
            tvDetailMoreInfoText1.setVisibility(View.GONE);
        }

        if( tel != null ){
            tvDetailMoreInfoText2.setText( tel );
        }else{
            tvDetailMoreInfoText2.setVisibility(View.GONE);
        }

        if( designState != null ){
            tvDetailMoreInfoText3.setText( designState );
        }else{
            tvDetailMoreInfoText3.setVisibility(View.GONE);
        }

        if( openHour != null ){
            tvDetailMoreInfoText4.setText( openHour );
        }else{
            tvDetailMoreInfoText4.setVisibility(View.GONE);
        }

        if( parking != null ){
            tvDetailMoreInfoText5.setText( parking );
        }else{
            tvDetailMoreInfoText5.setVisibility(View.GONE);
        }
    }

    private int getResourceId(int colorNumber){
        if( colorNumber%5 == 1 ){
            return R.drawable.list_color_1_bg;
        }else if( colorNumber%5 == 2 ){
            return R.drawable.list_color_2_bg;
        }else if( colorNumber%5 == 3 ){
            return R.drawable.list_color_3_bg;
        }else if( colorNumber%5 == 4 ){
            return R.drawable.list_color_4_bg;
        }else {
            return R.drawable.list_color_0_bg;
        }
    }

    private void startNMapActivity(String title, String location){
        Intent intent = new Intent(this, NMapTourListViewActivity.class);
        intent.putExtra(ITEM_TITLE, title);
        intent.putExtra(ITEM_COORDINATE, location);
        startActivity(intent);
    }

    private void startNaverTtsForDeatilContents(){
        Common.startTts(getBaseContext(), listId, Common.DETAIL_CONTENTS);
    }

    private void doShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title +
                                                System.getProperty("line.separator") +
                                                System.getProperty("line.separator") +
                                                contents);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private String getContentsKorString(int index){
        return Common.getJSTList(this, Common.SETTING_CHOICE_LANGUAGE_KOR).get(index).getContents();
    }

}
