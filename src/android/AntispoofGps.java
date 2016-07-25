package com.saurabhnemade.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.app.Activity;
import android.view.WindowManager;
import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.Class;
import java.lang.Object;
import java.lang.Boolean;
import java.util.List;
import java.util.HashMap;
import java.lang.reflect.Field;

public class AntispoofGps extends CordovaPlugin {
    public static Activity appActivity = null;
    public static Context appContext = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        AntispoofGps.appContext = this.cordova.getActivity().getApplicationContext();
        AntispoofGps.appActivity = this.cordova.getActivity();
        JSONObject result = new JSONObject();

        if (action.equals("checkGpsSpoof")) {
          if(AntispoofGps.isMockSettingsON(AntispoofGps.appContext) == false){
            if(AntispoofGps.areThereMockPermissionApps(AntispoofGps.appContext) == false){
                result.put("code", 1);
                result.put("success", true);
                result.put("message", "ALLOW_MOCK_LOCATION is disabled & no apps with ALLOW_MOCK_LOCATION permission are installed.");
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
                return true;
            }
            else{
                result.put("code", 2);
                result.put("success", false);
                result.put("message", "ALLOW_MOCK_LOCATION is disabled but some app(s) present with ALLOW_MOCK_LOCATION permission");
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, result));
                return true;
            }
          }
          else{
            result.put("code", 3);
            result.put("success", false);
            result.put("message", "ALLOW_MOCK_LOCATION is enabled");
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, result));
            return true;
          }
        }
        else if(action.equals("checkAllowMockLocationEnabled")){
          if(AntispoofGps.isMockSettingsON(AntispoofGps.appContext) == true){
            result.put("ALLOW_MOCK_LOCATION", true);
            result.put("enabled", true);
            result.put("message", "Allow Mock Location is enabled");
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
            return true;
          }
          else{
            result.put("ALLOW_MOCK_LOCATION", false);
            result.put("enabled", false);
            result.put("message", "Allow Mock Locaion is disabled");
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
            return true;
          }
        }
        else if(action.equals("checkMockPermissionApps")){
          try{
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, AntispoofGps.getMockPermissionApps(AntispoofGps.appContext)));
            return true;
          }
          catch(Exception ex){
            result.put("success", false);
            result.put("message", "Failed to check Mock permission");
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, result));
            return true;
          }
        }
        else{
          result.put("success", false);
          result.put("message", "Specified Method not defined in API");
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, result));
          return true;
        }
    }

    public static boolean isMockSettingsON(Context context) {
      if (Settings.Secure.getString(context.getContentResolver(),
                                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
         return false;
      else
         return true;
    }

    public static boolean areThereMockPermissionApps(Context context) {
      int count = 0;

      PackageManager pm = context.getPackageManager();
      List<ApplicationInfo> packages =
         pm.getInstalledApplications(PackageManager.GET_META_DATA);

      for (ApplicationInfo applicationInfo : packages) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                                                       PackageManager.GET_PERMISSIONS);

            String[] requestedPermissions = packageInfo.requestedPermissions;

            if (requestedPermissions != null) {
              for (int i = 0; i < requestedPermissions.length; i++) {
                 if (requestedPermissions[i]
                     .equals("android.permission.ACCESS_MOCK_LOCATION")
                     && !applicationInfo.packageName.equals(context.getPackageName())) {
                    System.out.println("found app: " +  packageInfo.toString());
                    count++;
                 }
              }
            }
        } catch (NameNotFoundException e) {
           Log.e("Got exception ",e.getMessage());
        }
     }
     if (count > 0)
        return true;
     return false;
   }

   public static JSONObject getMockPermissionApps(Context context) throws Exception {
     int count = 0;
     JSONObject result = new JSONObject();
     JSONArray apps = new JSONArray();

     PackageManager pm = context.getPackageManager();
     List<ApplicationInfo> packages =
        pm.getInstalledApplications(PackageManager.GET_META_DATA);

     for (ApplicationInfo applicationInfo : packages) {
       try {
           PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                                                      PackageManager.GET_PERMISSIONS);

           String[] requestedPermissions = packageInfo.requestedPermissions;

           if (requestedPermissions != null) {
             for (int i = 0; i < requestedPermissions.length; i++) {
                if (requestedPermissions[i]
                    .equals("android.permission.ACCESS_MOCK_LOCATION")
                    && !applicationInfo.packageName.equals(context.getPackageName())) {
                   System.out.println("found app: " +  packageInfo.toString());
                   apps.put(new JSONObject().put("APP_NAME", packageInfo.toString()));
                   count++;
                }
             }
           }
       } catch (NameNotFoundException e) {
          Log.e("Got exception ",e.getMessage());
       }
     }

    result.put("APP_COUNT", count);
    result.put("APPS", apps);
    return result;
    }
}
