package com.mythichelm.localnotificationsexample;

import android.os.Bundle;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

import com.mythichelm.localnotifications.LocalNotificationsPlugin;
import android.content.Intent;

public class MainActivity extends FlutterActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    LocalNotificationsPlugin.handleIntent(intent); // Add me for callbacks
  }
}
