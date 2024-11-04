package com.example.gps_android

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.annotation.NonNull

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class GpsAndroidPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private var activity: Activity? = null
  private val REQUEST_CHECK_SETTINGS = 1001

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "gps_android")
    context = flutterPluginBinding.applicationContext
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "enableGPS") {
      enableGPS(result)
    } else {
      result.notImplemented()
    }
  }

  private fun enableGPS(result: Result) {
    val locationRequest = LocationRequest.create().apply {
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(context)

    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
      result.success(true)
    }

    task.addOnFailureListener { exception ->
      if (exception is ResolvableApiException) {
        try {
          activity?.let {
            exception.startResolutionForResult(it, REQUEST_CHECK_SETTINGS)
          } ?: run {
            result.error("ERROR", "Activity is null", null)
          }
        } catch (sendEx: IntentSender.SendIntentException) {
          result.error("ERROR", "Cannot start resolution for GPS", null)
        }
      } else {
        result.error("ERROR", "GPS unavailable", null)
      }
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener { requestCode, resultCode, data ->
      if (requestCode == REQUEST_CHECK_SETTINGS) {
        if (resultCode == Activity.RESULT_OK) {
          channel.invokeMethod("onGPS", true)
        } else {
          channel.invokeMethod("onGPS", false)
        }
        true
      } else {
        false
      }
    }
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }
}
