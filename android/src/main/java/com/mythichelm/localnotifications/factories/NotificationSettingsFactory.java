package com.mythichelm.localnotifications.factories;

import com.mythichelm.localnotifications.LocalNotificationsPlugin;
import com.mythichelm.localnotifications.entities.NotificationAction;
import com.mythichelm.localnotifications.entities.NotificationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 30.03.2018.
 */

@SuppressWarnings("unchecked")
public class NotificationSettingsFactory implements INotificationSettingsFactory {
    @Override
    public NotificationSettings createFromArguments(List<Object> arguments) {
        LocalNotificationsPlugin.customLog("Creating NotificationSettings from arguments");
        HashMap<String, Object> map = (HashMap<String, Object>)arguments.get(0);

        NotificationSettings settings = new NotificationSettings();
        settings.Title = (String)map.get("title");
        settings.Body = (String)map.get("content");
        settings.ImageUrl = (String)map.get("imageUrl");
        settings.Ticker = (String)map.get("ticker");
        settings.Id = (int)map.get("id");

        // get android specific settings
        HashMap<String, Object> androidSettings = (HashMap<String, Object>)map.get("androidSettings");
        settings.Priority = (int)androidSettings.get("priority");
        settings.IsOngoing = (boolean)androidSettings.get("isOngoing");
        settings.Channel = (String)androidSettings.get("channel");
        settings.VibratePattern = LocalNotificationsPlugin.intArrayListToLongArray(
                (ArrayList<Integer>)androidSettings.get("vibratePattern"));
        settings.UseDefaultVibratePattern = (settings.VibratePattern.length == 0);

        HashMap<String, Object> onNotifClickMap = (HashMap<String, Object>)map.get("onNotificationClick");
        settings.OnNotificationClick = getNotificationAction(onNotifClickMap);

        List<Object> extraActionsList = (List<Object>)map.get("extraActions");
        settings.ExtraActions = getExtraActions(extraActionsList);

        LocalNotificationsPlugin.customLog("Finished creating NotificationSettings from arguments");
        return settings;
    }

    private NotificationAction[] getExtraActions(List<Object> extraActionsList) {
        List<NotificationAction> extraActions = new ArrayList<>();
        for (Object obj : extraActionsList) {
            HashMap<String, Object> actionAsMap = (HashMap<String, Object>)obj;
            NotificationAction extraAction = getNotificationAction(actionAsMap);
            extraActions.add(extraAction);
        }

        return extraActions.toArray(new NotificationAction[0]);
    }

    private NotificationAction getNotificationAction(HashMap<String, Object> map) {
        String callbackName = (String)map.get("callback");
        String actionText = (String)map.get("actionText");
        String payload = (String)map.get("payload");
        boolean launchesApp = (boolean)map.get("launchesApp");
        return new NotificationAction(callbackName, actionText, payload, launchesApp);
    }
}
