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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sg0101.app.junggutour.util.Log;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by lee on 2016-08-28.
 */
public class CustomExpandCard extends CardExpand {

    private TextView tvText;
    private Button btnPlay;
    private Button btnStop;
    private String outline;
    private int index;

    //Use your resource ID for your inner layout
    public CustomExpandCard(Context context) {
        super(context, R.layout.custom_expand_card_layout);
    }

    public CustomExpandCard(Context context, String text, int idx) {
        super(context, R.layout.custom_expand_card_layout);
        outline = text;
        index = idx;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) { Log.e("View == null , index = " + index); return; }

        //Retrieve TextView elements
        tvText = (TextView) view.findViewById(R.id.custom_expand_text1);
        if (tvText != null) {
            tvText.setText(outline);
        }
        btnPlay = (Button) view.findViewById(R.id.custom_expand_btnPlay);
        if( btnPlay == null ){
            Log.e("btnPlay == null , index = " + index);
        }
        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Common.startTts(view.getContext(), index, Common.OUTLINE_CONTENTS);
            }
        });
        btnStop = (Button) view.findViewById(R.id.custom_expand_btnStop);
        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Common.pauseSound();
            }
        });

    }

}
