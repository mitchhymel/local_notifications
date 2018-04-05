package com.mythichelm.localnotifications.factories;

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
        HashMap<String, Object> map = (HashMap<String, Object>)arguments.get(0);

        NotificationSettings settings = new NotificationSettings();
        settings.Title = (String)map.get("title");
        settings.Body = (String)map.get("content");
        settings.ImageUrl = (String)map.get("imageUrl");
        settings.Ticker = (String)map.get("ticker");
        settings.Priority = (int)map.get("importance");
        settings.IsOngoing = (boolean)map.get("isOngoing");
        settings.Id = (int)map.get("id");
        settings.Channel = (String)map.get("channel");

        HashMap<String, Object> onNotifClickMap = (HashMap<String, Object>)map.get("onNotificationClick");
        settings.OnNotificationClick = getNotificationAction(onNotifClickMap);

        List<Object> extraActionsList = (List<Object>)map.get("extraActions");
        settings.ExtraActions = getExtraActions(extraActionsList);

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
        String intentPayload = (String)map.get("intentPayload");
        boolean launchesApp = (boolean)map.get("launchesApp");
        return new NotificationAction(callbackName, actionText, intentPayload, launchesApp);
    }
}
