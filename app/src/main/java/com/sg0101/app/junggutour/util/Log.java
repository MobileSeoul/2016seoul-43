/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * package-level logging flag
 */

package com.sg0101.app.junggutour.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public final static String LOGTAG = "JungguTour";

    public static final boolean LOGV = true;
    public static final boolean LOGD = true;
    public static final boolean LOGI = true;
    public static final boolean LOGW = true;
    public static final boolean LOGE = true;
    public static final boolean LOGV_TAG = true;
    public static final boolean LOGD_TAG = true;
    public static final boolean LOGI_TAG = true;
    public static final boolean LOGW_TAG = true;
    public static final boolean LOGE_TAG = true;

    static int mLine=0;
    static String mName="";
    static String mPkg="";
    public static void v(String logMe) {
    	if(LOGV) {
	    	StackTraceElement[] stack = (new Throwable()).getStackTrace();
	    	mLine = stack[1].getLineNumber();
	    	mName = stack[1].getMethodName();
	    	mPkg = stack[1].getFileName();
	    	android.util.Log.v(LOGTAG, ""+mPkg+", "+mLine+" line, "+mName+"(), "+logMe);
    	}
    }

    public static void d(String logMe) {
    	if(LOGD) {
	    	StackTraceElement[] stack = (new Throwable()).getStackTrace();
	    	mLine = stack[1].getLineNumber();
	    	mName = stack[1].getMethodName();
	    	mPkg = stack[1].getFileName();
	    	android.util.Log.d(LOGTAG, ""+mPkg+", "+mLine+" line, "+mName+"(), "+logMe);
    	}
    }

    public static void d(String tag, String logMe) {
    	if(LOGD_TAG) android.util.Log.d(LOGTAG, logMe);
    }

    public static void i(String logMe) {
    	if(LOGI) {
	    	StackTraceElement[] stack = (new Throwable()).getStackTrace();
	    	mLine = stack[1].getLineNumber();
	    	mName = stack[1].getMethodName();
	    	mPkg = stack[1].getFileName();
	    	android.util.Log.i(LOGTAG, ""+mPkg+", "+mLine+" line, "+mName+"(), "+logMe);
    	}
    }

    public static void i(String tag, String logMe) {
    	if(LOGI_TAG) android.util.Log.i(LOGTAG, logMe);
    }

    public static void w(String logMe) {
    	if(LOGW) {
	    	StackTraceElement[] stack = (new Throwable()).getStackTrace();
	    	mLine = stack[1].getLineNumber();
	    	mName = stack[1].getMethodName();
	    	mPkg = stack[1].getFileName();
	    	android.util.Log.w(LOGTAG, ""+mPkg+", "+mLine+" line, "+mName+"(), "+logMe);
    	}
    }

    public static void w(String tag, String logMe) {
    	if(LOGW_TAG) android.util.Log.w(LOGTAG, logMe);
    }

    public static void e(String logMe) {
    	if(LOGE) {
	    	StackTraceElement[] stack = (new Throwable()).getStackTrace();
	    	mLine = stack[1].getLineNumber();
	    	mName = stack[1].getMethodName();
	    	mPkg = stack[1].getFileName();
	    	android.util.Log.e(LOGTAG, ""+mPkg+", "+mLine+" line, "+mName+"(), "+logMe);
    	}
    }

    public static void e(String logMe, Exception ex) {
    	if(LOGE_TAG) android.util.Log.e(LOGTAG, logMe, ex);
    }

    public static void e(String tag, String logMe) {
    	if(LOGE_TAG) android.util.Log.e(LOGTAG, logMe);
    }

    public static void e(String tag, String logMe, Exception ex) {
    	if(LOGE_TAG) android.util.Log.e(LOGTAG, logMe, ex);
    }

    public static void wtf(String logMe) {
        android.util.Log.wtf(LOGTAG, logMe);
    }

    public static String formatTime(long millis) {
        return new SimpleDateFormat("HH:mm:ss.SSS aaa").format(new Date(millis));
    }
}
