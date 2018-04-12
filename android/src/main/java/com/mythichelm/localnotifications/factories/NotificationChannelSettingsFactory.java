package com.mythichelm.localnotifications.factories;

import com.google.gson.Gson;
import com.mythichelm.localnotifications.LocalNotificationsPlugin;
import com.mythichelm.localnotifications.entities.NotificationChannelSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationChannelSettingsFactory implements INotificationChannelSettingsFactory {
    @Override
    public NotificationChannelSettings createFromArguments(List<Object> arguments) {
        HashMap<String, Object> map = (HashMap<String, Object>)arguments.get(0);
        LocalNotificationsPlugin.customLog("Creating a notification channel with params: " + new Gson().toJson(map));

        NotificationChannelSettings settings = new NotificationChannelSettings();

        settings.Id = (String) map.get("id");
        settings.Name= (String) map.get("name");
        settings.Description = (String) map.get("description");
        settings.Importance = (int) map.get("importance");
        settings.VibratePattern = LocalNotificationsPlugin.intArrayListToLongArray(
                (ArrayList<Integer>)map.get("vibratePattern"));
        settings.UseDefaultVibratePattern = (settings.VibratePattern.length == 0);

        return settings;
    }
}
