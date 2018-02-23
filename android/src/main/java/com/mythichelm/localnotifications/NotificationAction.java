package com.mythichelm.localnotifications;

public class NotificationAction {
  public String callbackFunctionName;
  public String actionText;
  public String intentPayload;
  public boolean launchesApp;

  public NotificationAction(String callbackFunctionName, String actionText, String intentPayload, boolean launchesApp) {
    this.callbackFunctionName = callbackFunctionName;
    this.actionText = actionText;
    this.intentPayload = intentPayload;
    this.launchesApp = launchesApp;
  }

  boolean isEmptyAction() {
    return this.callbackFunctionName == ""
            && this.actionText == ""
            && this.intentPayload == "";
  }
}