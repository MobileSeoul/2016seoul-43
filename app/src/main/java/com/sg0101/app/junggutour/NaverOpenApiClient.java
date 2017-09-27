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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NaverOpenApiClient {
    private static final String BASE_URL = Common.NAVER_OPEN_API_BASE_URL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postTts(String url, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        setClient();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postTranslate(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setClient();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void setClient(){
        client.addHeader(Common.NAVER_OPEN_API_HEADER_CLIENT_ID_TEXT, Common.getNaverClientID());
        client.addHeader(Common.NAVER_OPEN_API_HEADER_CLIENT_SECRET_TEXT, Common.getNaverClientSecret());
        client.addHeader(Common.NAVER_OPEN_API_HEADER_CONTENT_TYPE_TEXT, Common.NAVER_OPEN_API_HEADER_CONTENT_TYPE_APPLICATION);
    }
}
