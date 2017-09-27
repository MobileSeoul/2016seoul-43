/*
    Copyright (c) 2014 Marek Sebera <marek.sebera@gmail.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.sg0101.app.junggutour;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class JSTClient {

    private static final String BASE_URL = "http://openapi.seoul.go.kr:8088/";
    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        //client.addHeader("Content-Type", "audio/mpeg");
        //client.setBasicAuth(Common.JST_REQUEST_PARAMS_KEY,Common.getSeoulDataKey());
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
