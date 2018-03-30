package com.mythichelm.localnotifications.factories;

import com.mythichelm.localnotifications.entities.NotificationAction;
import com.mythichelm.localnotifications.entities.NotificationSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 30.03.2018.
 */

@SuppressWarnings("unchecked")
public class NotificationSettingsFactory implements INotificationSettingsFactory {
    @Override
    public NotificationSettings createFromArguments(List<Object> arguments) {
        NotificationSettings settings = new NotificationSettings();
        settings.Title = (String)arguments.get(0);
        settings.Body = (String)arguments.get(1);
        settings.ImageUrl = (String)arguments.get(2);
        settings.Ticker = (String)arguments.get(3);
        settings.Priority = (int)arguments.get(4);
        settings.IsOngoing = (boolean)arguments.get(5);
        settings.Id = (int)arguments.get(6);
        settings.OnNotificationClick = getNotifcationAction(arguments);
        settings.ExtraActions = getExtraActions(arguments);
        return settings;
    }

    private NotificationAction[] getExtraActions(List<Object> arguments) {
        List<String> actionsCallbacks = (List<String>)arguments.get(11);
        List<String> actionsTexts = (List<String>)arguments.get(12);
        List<String> actionsIntentPayloads = (List<String>)arguments.get(13);
        List<Boolean> actionsLaunchesApp = (List<Boolean>)arguments.get(14);

        List<NotificationAction> extraActions = new ArrayList<>();
        for (int i = 0; i < actionsCallbacks.size(); i++) {
            String callback = actionsCallbacks.get(i);
            String text = actionsTexts.get(i);
            String payload = actionsIntentPayloads.get(i);
            boolean launch = actionsLaunchesApp.get(i);
            extraActions.add(new NotificationAction(callback, text, payload, launch));
        }
        return extraActions.toArray(new NotificationAction[0]);
    }

    private NotificationAction getNotifcationAction(List<Object> arguments) {
        String callbackName = (String)arguments.get(7);
        String actionText = (String)arguments.get(8);
        String intentPayload = (String)arguments.get(9);
        boolean launchesApp = (boolean)arguments.get(10);
        return new NotificationAction(callbackName, actionText, intentPayload, launchesApp);
    }
}
