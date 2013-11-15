package com.mapzen.activity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mapquest.android.maps.*;
import com.mapzen.R;

import java.nio.BufferUnderflowException;

public class BaseActivity extends MapActivity {
    @Override
    protected boolean isLocationDisplayed() {
        return super.isLocationDisplayed();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private MapView mapView;
    private SlidingMenu slidingMenu ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        setupMap();
        setupSlidingMenu();
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar ab = getActionBar();
        ab.setTitle(R.string.application_name);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
    }

    @Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()
                ) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.slidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        super.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            BoundingBox boundingBox = mapView.getBoundingBox(null);
            double[] box = {
                boundingBox.ul.getLongitude(),
                boundingBox.ul.getLatitude(),
                boundingBox.lr.getLongitude(),
                boundingBox.lr.getLongitude()
            };
            intent.putExtra("box", box);
        }

        super.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void setupMap() {
        mapView = (MapView) findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);

        final MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);

        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                GeoPoint currentLocation = myLocationOverlay.getMyLocation();
                mapView.getController().animateTo(currentLocation);
                mapView.getController().setZoom(14);
                mapView.getOverlays().add(myLocationOverlay);
                myLocationOverlay.setFollowing(true);
            }
        });

    }
}