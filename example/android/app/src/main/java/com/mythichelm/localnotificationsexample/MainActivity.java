package com.mythichelm.localnotificationsexample;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "com.mythichelm.localnotificationsexample";
  private MethodChannel channel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    channel = new MethodChannel(getFlutterView(), CHANNEL);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    String callbackName = intent.getStringExtra("callback_key");
    String payload = intent.getStringExtra("payload_key");
    if (callbackName != null && callbackName != "") {
      channel.invokeMethod(callbackName, payload);
    }
    else {
      Log.d("TAG", "callback name was null or empty");
    }
  }
}
