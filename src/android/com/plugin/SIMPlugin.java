package com.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shipit.android.SIM_ActTracker;
import com.shipit.android.SIM_Docker;
import com.shipit.android.SIM_Cfg;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.sql.Date;

public class SIMPlugin extends CordovaPlugin{

	public static CordovaWebView gwebView;
	public final SIM_Docker shipit = SIM_Docker.sharedInstance();
	public final SIM_Cfg cfg_local = SIM_Cfg.sharedInstance();
	public static JSONObject pendingMessage=null;
	public static String pendingFunctionName;

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
	    throws JSONException {

		gwebView = this.webView;
		if (action.equals("initialize")) {
		    this.initialize(args, callbackContext);
		    return true;
		}
		else if (action.equals("pushEnable"))
		{
		    this.pushEnable(args, callbackContext);
		    return true;
		}
		else if(action.equals("setTag"))
		{
		    this.setTag(args, callbackContext);
		    return true;
		}
		else if(action.equals("deleteTag"))
		{
		    this.deleteTag(args, callbackContext);
		    return true;
		}

		return false;
	}

	public static void sendJavascript(  String funName,  JSONObject _json )
	{
		String _d =  "javascript:shipit."+funName+"(" + _json.toString() + ")";
		if(gwebView !=null){
		    gwebView.sendJavascript( _d );
		}else {
		    pendingMessage = _json;
		    pendingFunctionName = funName;
		}
	}

	private void initialize(JSONArray data, CallbackContext callbackContext)
	{
		JSONObject params = null;
		try
		{
			params = data.getJSONObject(0);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			Log.e("SIMPlugin", "No parameters has been passed to onDeviceReady function. Follow guide correctly?");
			callbackContext.error(e.getMessage());
			return;
		}
		try {
			gwebView = this.webView;
			if(pendingMessage != null){
				sendJavascript(pendingFunctionName, pendingMessage);
				pendingMessage = null;
			}
			cfg_local.projID = params.getString("projectid");
			cfg_local.SIM_AppKey = params.getString("shipitAppID");
			if(params.has("buildType") && params.getString("buildType").equalsIgnoreCase("dev"))
				cfg_local.buildType = "DEV";
			cfg_local.richPushEnable = true;
			cfg_local.pushEnable = true;
			shipit.init(cordova.getActivity());
			SIM_ActTracker.init(cordova.getActivity().getApplication());
			callbackContext.success();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("SIMPlugin", "Error initializing the app.");
			callbackContext.error(e.getMessage());
			return;
		}
	}

	private void pushEnable(JSONArray data, CallbackContext callbackContext)
	{
		JSONObject params = null;
		try
		{
			params = data.getJSONObject(0);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			Log.e("SIMPlugin", "No parameters has been passed for pushEnable");
			callbackContext.error(e.getMessage());
			return;
		}
		try {
			gwebView = this.webView;
			if(pendingMessage != null){
				sendJavascript(pendingFunctionName, pendingMessage);
				pendingMessage = null;
			}
			cfg_local.pushEnable = params.getBoolean("NotificationEnable");
			shipit.SIM_setPush(cordova.getActivity(), cfg_local.pushEnable);
			callbackContext.success();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("SIMPlugin", "Error pushEnable.");
			callbackContext.error(e.getMessage());
			return;
		}
	}

	private void setTag(JSONArray data, CallbackContext callbackContext)
	{
		JSONObject params = null;

		try
		{
		    params = data.getJSONObject(0);
		}
		catch (JSONException e)
		{
		    e.printStackTrace();
		    Log.e("SIMPlugin", "No parameters has been passed for setTag");
		    callbackContext.error(e.getMessage());
		    return;
		}
		try {
			gwebView = this.webView;
			if(pendingMessage != null){
				sendJavascript(pendingFunctionName, pendingMessage);
				pendingMessage = null;
			}
		    
			String	tagName = params.getString("tagName");
			String	tagType = params.getString("tagType");

			if(tagType.equals("String")) {
				shipit.SIM_setTag(tagName, params.getString("tagValue"));
			}
			else if(tagType.equals("List")) {
				shipit.SIM_setTag(tagName, params.getString("tagValue"));
			}
			else if(tagType.equals("Boolean")) {
				shipit.SIM_setTag(tagName, params.getBoolean("tagValue"));
			}
			else if(tagType.equals("Integer")) {
				shipit.SIM_setTag(tagName, params.getInt("tagValue"));
			}
			else if(tagType.equals("Date")) {
				shipit.SIM_setTag(tagName, (new Date(params.getLong("tagValue"))));
			}
			else{
				shipit.SIM_setTag(tagName, params.getString("tagValue"));
			}

			callbackContext.success();
		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("SIMPlugin", "Error setTag.");
		    callbackContext.error(e.getMessage());
		    return;
		}
	}

	private void deleteTag(JSONArray data, CallbackContext callbackContext)
	{
		JSONObject params = null;

		try
		{
		    params = data.getJSONObject(0);
		}
		catch (JSONException e)
		{
		    e.printStackTrace();
		    Log.e("SIMPlugin", "No parameters has been passed for deleteTag");
		    callbackContext.error(e.getMessage());
		    return;
		}
		try {
			gwebView = this.webView;
			if(pendingMessage != null){
				sendJavascript(pendingFunctionName, pendingMessage);
				pendingMessage = null;
			}
		    
			String tagName = params.getString("tagName");
			shipit.SIM_deleteTag(tagName);
			callbackContext.success();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("SIMPlugin", "Error deleteTag.");
			callbackContext.error(e.getMessage());
			return;
		}
	}
}
