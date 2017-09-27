/*
 * Copyright 2016 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sg0101.app.junggutour;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapCircleData;
import com.nhn.android.maps.overlay.NMapCircleStyle;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.sg0101.app.junggutour.NMap.NMapPOIflagType;
import com.sg0101.app.junggutour.NMap.NMapViewerResourceProvider;

import java.util.List;

public class NMapTourListViewActivity extends NMapActivity {

    private static final String LOG_TAG = "NMapTourListView";
    private static final boolean DEBUG = false;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private NMapView mMapView;
    private NMapController mMapController;

    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;
    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;

    private String title;
    private String coordinate;
    private Double pointX;
    private Double pointY;

    private List<JungguStoryTelling> jstList;

    private String mTitle;

    private static boolean USE_XML_LAYOUT = true;

    private Toolbar toolbar;
    private boolean tourListMode = false;

    private int permissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( getIntent().getStringExtra(Common.ITEM_COORDINATE) != null ){
            coordinate = getIntent().getStringExtra(Common.ITEM_COORDINATE);
            String[] temp = coordinate.split(",");
            String tempX = temp[0];
            String tempY = temp[1];
            Log.d("===== " , "temp x = " + tempX);
            Log.d("===== " , "temp y = " + tempY);
            pointX = Double.parseDouble(tempX.substring(tempX.indexOf('E')+1));
            pointY = Double.parseDouble(tempY.substring(tempY.indexOf('N')+1));
            tourListMode = false;
        }else{
            tourListMode = true;
        }

        if (USE_XML_LAYOUT) {
            setContentView(R.layout.activity_nmap_tour_list_view);
            mMapView = (NMapView)findViewById(R.id.mapView);

            toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    overridePendingTransition(0,0);
                    finish();
                }
            });

            //toolbar.setLogo(R.drawable.ic_main_logo_shade);
            toolbar.inflateMenu(R.menu.map_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_view_map_n:
                            mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
                            return true;

                        case R.id.action_view_map_s:
                            mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
                            return true;

                        case R.id.action_go_my_location:
                            if( checkPermission() == false ){
                                return true;
                            }else{
                                showSnackBarForMyLocation("Go My Location");
                            }
                            return true;

                        default:
                            return false;
                    }
                }
            });

        } else {
            // create map view
            mMapView = new NMapView(this);
            setContentView(mMapView);
        }

        mMapView.setClientId(Common.getNaverClientID());

        // initialize map view
        mMapView.setClickable(true);
        // use built in zoom controls
        mMapView.setBuiltInZoomControls(true, null);

        // register listener for map state changes
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        // location manager
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager
        mMapCompassManager = new NMapCompassManager(this);
        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {

        stopMyLocation();

        super.onStop();
    }

    private void startDetailPage(String id){
        Intent intent = new Intent(this, DetailDesignSupportActivity.class);
        intent.putExtra(Common.TOUR_LIST_ID, id);
        startActivity(intent);
    }

    private void initJSTMapViewList(){
        int markerId = NMapPOIflagType.PIN;
        jstList = Common.getJSTList(getBaseContext(),Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE));

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(jstList.size(), mMapViewerResourceProvider);
        poiData.beginPOIdata(jstList.size());
        for(int i=0; i<jstList.size(); i++){
            if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_KOR ){
                mTitle = jstList.get(i).getName_kor();
            }else if( Common.getSettingValue(Common.SETTING_CHOICE_LANGUAGE) == Common.SETTING_CHOICE_LANGUAGE_ENG ){
                mTitle = jstList.get(i).getName_eng();
            }else{
                mTitle = jstList.get(i).getName_cng();
            }
            poiData.addPOIitem(Common.pointX(jstList.get(i).getCoordinate()),
                    Common.pointY(jstList.get(i).getCoordinate()),
                    mTitle,
                    markerId, 0);
        }
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        // show all POI data
        //poiDataOverlay.showAllPOIdata(0);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                if(tourListMode == true){
                    initJSTMapViewList();
                    mMapController.setMapCenter(Common.pointX(jstList.get(60).getCoordinate()), Common.pointY(jstList.get(60).getCoordinate()),11);
                }else{
                    setPathDataOverlay();
                    mMapController.setMapCenter( pointX, pointY, 15);
                }

            } else { // fail
                Log.e(LOG_TAG, "onMapInitHandler: error=" + errorInfo.toString());
            }

        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {

        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

    };

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());

            }

            startDetailPage(String.valueOf(item.getOrderId()-1));
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };

    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            // stop location updating
            //			Runnable runnable = new Runnable() {
            //				public void run() {
            //					stopMyLocation();
            //				}
            //			};
            //			runnable.run();

            Toast.makeText(NMapTourListViewActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(NMapTourListViewActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };

    private void startMyLocation() {

        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            if (mMapLocationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    mMyLocationOverlay.setCompassHeadingVisible(true);

                    mMapCompassManager.enableCompass();

                    mMapView.setAutoRotateEnabled(true, false);

//                    mMapContainerView.requestLayout();
                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(this, "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);

                    return;
                }
            }
        }
    }

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

//                mMapContainerView.requestLayout();
            }
        }
    }

    private void setPathDataOverlay() {

        // set path data points
        NMapPathData pathData = new NMapPathData(1);

        pathData.initPathData();
        pathData.addPathPoint(pointX, pointY, NMapPathLineStyle.TYPE_DASH);
        pathData.endPathData();

        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
        if (pathDataOverlay != null) {

            // add circle data
            NMapCircleData circleData = new NMapCircleData(1);
            circleData.initCircleData();
            circleData.addCirclePoint(pointX, pointY, 70.0F);
            circleData.endCircleData();
            pathDataOverlay.addCircleData(circleData);
            // set circle style
            NMapCircleStyle circleStyle = new NMapCircleStyle(mMapView.getContext());
            circleStyle.setLineType(NMapPathLineStyle.TYPE_DASH);
            circleStyle.setFillColor(0x000000, 0x00);
            circleData.setCircleStyle(circleStyle);

            // show all path data
            //pathDataOverlay.showAllPathData(0);
        }
    }

    private void showSnackBarForMyLocation(String str){

        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), str, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startMyLocation();
                    }
                });
        View snackbarView = snackbar.getView();

        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(2);

        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
        snackbarActionTextView.setTextColor(Color.CYAN);
        snackbarActionTextView.setTypeface(null, Typeface.BOLD);

        snackbar.show();

    }

    private boolean checkPermission(){
        // permission check for android 6.0 higher
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        }else{
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    showSnackBarForMyLocation("Go My Location");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.text_permission_grant_title))
                            .setMessage(getString(R.string.text_permission_grant_msg))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String packageName = "com.sg0101.app.junggutour";
                                    try {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + packageName));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        //Open the generic Apps page:
                                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        startActivity(intent);
                                    }
                                }
                            })
                            /*
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })*/
                            .create().show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
