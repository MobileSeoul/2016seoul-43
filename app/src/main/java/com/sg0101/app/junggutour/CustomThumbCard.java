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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by lee on 2016-08-28.
 */
public class CustomThumbCard extends CardThumbnail {
    public CustomThumbCard(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        if (viewImage!=null){
            //viewImage.getLayoutParams().width=250;
            //viewImage.getLayoutParams().height=250;

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)viewImage.getLayoutParams();
            params.setMargins(5, 0, 0, 0);
            viewImage.setLayoutParams(params);
            DisplayMetrics metrics=parent.getResources().getDisplayMetrics();
            viewImage.getLayoutParams().width= (int)(100*metrics.density);
            viewImage.getLayoutParams().height = (int)(100*metrics.density);
        }
    }

}
