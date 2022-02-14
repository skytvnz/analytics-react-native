package com.example.segmentanalyticsreactnative;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;

import com.facebook.react.ReactActivity;
import com.sovranreactnative.Sovran;
import com.zoontek.rnbootsplash.RNBootSplash;

import java.util.Hashtable;

public class MainActivity extends ReactActivity {

 Sovran sovran = MainApplication.sovran;

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "AnalyticsReactNativeExample";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RNBootSplash.init(R.drawable.bootsplash, MainActivity.this);
    trackDeepLinks(this);

  }

  @Override
  protected  void  onResume() {
    super.onResume();
    Log.i("ReactNativeJS", "XXXXXXXXONRESUMEXXXXXXX");
    trackDeepLinks(this);
  }

  public void trackDeepLinks(Activity activity) {
    Intent intent = activity.getIntent();
    if (intent == null || intent.getData() == null) {
      return;
    }

    Hashtable<String, String> properties = new Hashtable<>();

    Uri referrer = getReferrer(activity);

    if (referrer != null) {
      String referring_application = referrer.toString();
      properties.put("referring_application", referring_application);
    }

    Uri uri = intent.getData();
    try {
      properties.put("url", uri.toString());
      for (String parameter : uri.getQueryParameterNames()) {
        String value = uri.getQueryParameter(parameter);
        if (value != null && !value.trim().isEmpty()){
          properties.put(parameter, value);
        }
      }
    } catch (Exception e) {
      //handle error
      System.out.println(e);
    }
    Log.i("ReactNativeJS", "XXXXXXXXXXXXTRACKDLXXXXXXXXX");
    Log.i("ReactNativeJS", properties.toString());
    System.out.println("*************DLDDLDLDLDLDL*******************");
    sovran.dispatch("add-deepLink-data", properties);
  }

  /** Returns the referrer who started the Activity. */
  public static Uri getReferrer(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      return activity.getReferrer();
    } else {
      return null;
    }
  }
}



