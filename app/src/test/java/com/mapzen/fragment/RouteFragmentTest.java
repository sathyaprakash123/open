package com.mapzen.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.mapzen.MapzenTestRunner;
import com.mapzen.R;
import com.mapzen.activity.BaseActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oscim.core.GeoPoint;
import org.robolectric.Robolectric;

import static org.fest.assertions.api.ANDROID.assertThat;

@RunWith(MapzenTestRunner.class)
public class RouteFragmentTest {
    private BaseActivity act;
    private RouteFragment fragment;

    @Before
    public void setUp() throws Exception {
        act = Robolectric.buildActivity(BaseActivity.class).create().get();
        fragment = new RouteFragment();
        fragment.setDestination(new GeoPoint(1.0, 2.0));
        fragment.setFrom(new GeoPoint(3.0, 4.0));
        fragment.setAct(act);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertThat(fragment).isNotNull();
    }

    @Test
    public void shouldNotBeAdded() throws Exception {
        assertThat(fragment).isNotAdded();
    }

    @Test
    public void shouldHideActionBar() throws Exception {
        fragment.attachToActivity();
        assertThat(act.getActionBar()).isNotShowing();
    }

    @Test
    public void shouldKeepScreenOn() throws Exception {
        LayoutInflater inflater = act.getLayoutInflater();
        View view = inflater.inflate(R.layout.route_widget, null, false);
        assertThat(view).isKeepingScreenOn();
    }
}