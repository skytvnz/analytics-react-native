package com.example.segmentanalyticsreactnative;

import android.app.Application;
import android.content.Context;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.sovranreactnative.Sovran;
import android.util.Log;

import com.segmentanalyticsreactnative.AnalyticsReactNativePackage;

public class MainApplication extends Application implements ReactApplication {

  private Sovran sovran = new Sovran();

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for AnalyticsReactNativeExample:
          // packages.add(new MyReactNativePackage());
          packages.add(new AnalyticsReactNativePackage());

          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager()); // Remove this line if you don't want Flipper enabled
  }

  @Override
  public  void onActivityCreated(Activity activity) {
    trackDeepLinks(activity);
  }

  /**
   * Loads Flipper in React Native templates.
   *
   * @param context
   */
  private static void initializeFlipper(Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.segmentanalyticsreactnativeExample.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }

  private  void trackDeepLinks(Activity activity) {
    Intent intent = activity.getIntent();
    if (intent == null || intent.getData() == null) {
      return;
    }

    Properties properties = new Properties();

    Uri referrer = Utils.getReferrer(activity)
      if (referrer != null) {
        properties.putReferrer(referrer.toString());
      }

    Uri uri = intent.getData();
      try {
        for (String parameter : uri.getQueryParameterNames()) {
          String value = uri.getQueryParameter(parameter);
          if (value != null && !value.trim().isEmpty()) {
            properties.put(parameter, value);
          }
        }
      } catch (Exception e) {
        Log.i(e);
      }

      properties.put("url", uri.toString());
      sovran.dispatch("add-deepLink-data", properties);
  }
}
