/*
    The MIT License (MIT)

    Copyright (c) 2016 Chad Song

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
package com.sg0101.app.junggutour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sackcentury.shinebuttonlib.ShineButton;

public class SettingsActivity extends AppCompatActivity {

    private ShineButton mSettingLanguageKor;
    private ShineButton mSettingLanguageEng;
    private ShineButton mSettingLanguageChi;
    private ShineButton mSettingVoiceMale;
    private ShineButton mSettingVoiceFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.ic_menu_setting);

        initSetting();
    }

    @Override
    protected void onResume() {
        initSetting();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_back:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    startMain();
                    finish();
                    return true;
                }

            }
        return super.dispatchKeyEvent(event);

    }

    private void initSetting(){
        initSettingLanguage();
        initSettingVoice();
        settingLanguageBtnToggle();
        settingVoiceBtnToggle();
    }

    private void initSettingLanguage(){
        mSettingLanguageKor = (ShineButton) findViewById(R.id.setting_lang_kor);
        mSettingLanguageKor.init(this);
        mSettingLanguageKor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Common.setSettingValue(Common.SETTING_CHOICE_LANGUAGE, Common.SETTING_CHOICE_LANGUAGE_KOR);
                settingLanguageBtnToggle();
                snackbarMsg(view, getString(R.string.settings_select_korean_text));
            }
        });

        mSettingLanguageEng = (ShineButton) findViewById(R.id.setting_lang_eng);
        mSettingLanguageEng.init(this);
        mSettingLanguageEng.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Common.setSettingValue(Common.SETTING_CHOICE_LANGUAGE, Common.SETTING_CHOICE_LANGUAGE_ENG);
                settingLanguageBtnToggle();
                snackbarMsg(view, getString(R.string.settings_select_english_text));
            }
        });

        mSettingLanguageChi = (ShineButton) findViewById(R.id.setting_lang_chi);
        mSettingLanguageChi.init(this);
        mSettingLanguageChi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Common.setSettingValue(Common.SETTING_CHOICE_LANGUAGE, Common.SETTING_CHOICE_LANGUAGE_CNG);
                settingLanguageBtnToggle();
                snackbarMsg(view, getString(R.string.settings_select_chinese_text));
            }
        });
    }

    private void initSettingVoice(){
        mSettingVoiceMale = (ShineButton) findViewById(R.id.setting_voice_male);
        mSettingVoiceMale.init(this);
        mSettingVoiceMale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Common.setSettingValue(Common.SETTING_CHOICE_VOICE, Common.SETTING_CHOICE_VOICE_MALE);
                settingVoiceBtnToggle();
                snackbarMsg(view, getString(R.string.settings_select_male_voice_text));
            }
        });

        mSettingVoiceFemale = (ShineButton) findViewById(R.id.setting_voice_female);
        mSettingVoiceFemale.init(this);
        mSettingVoiceFemale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Common.setSettingValue(Common.SETTING_CHOICE_VOICE, Common.SETTING_CHOICE_VOICE_FEMALE);
                settingVoiceBtnToggle();
                snackbarMsg(view, getString(R.string.settings_select_female_voice_text));
            }
        });
    }

    private void snackbarMsg(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void settingLanguageBtnToggle(){
        int val = Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE);
        if( val == Common.SETTING_CHOICE_LANGUAGE_ENG ){
            mSettingLanguageKor.setChecked(false);
            mSettingLanguageEng.setChecked(true);
            mSettingLanguageChi.setChecked(false);
        }else if( val == Common.SETTING_CHOICE_LANGUAGE_CNG ){
            mSettingLanguageKor.setChecked(false);
            mSettingLanguageEng.setChecked(false);
            mSettingLanguageChi.setChecked(true);
        }else{
            mSettingLanguageKor.setChecked(true);
            mSettingLanguageEng.setChecked(false);
            mSettingLanguageChi.setChecked(false);
        }
    }

    private void settingVoiceBtnToggle(){
        int val = Common.getSettingValue(Common.SETTING_CHOICE_VOICE);
        if( val == Common.SETTING_CHOICE_VOICE_MALE ){
            mSettingVoiceFemale.setChecked(false);
            mSettingVoiceMale.setChecked(true);
        }else{
            mSettingVoiceFemale.setChecked(true);
            mSettingVoiceMale.setChecked(false);
        }
    }

    private void startMain(){
        Intent intent = new Intent(this, GridViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}


