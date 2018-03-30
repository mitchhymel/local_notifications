package com.mythichelm.localnotifications.factories;

import com.mythichelm.localnotifications.entities.NotificationSettings;

import java.util.List;

/**
 * Created by simon on 30.03.2018.
 */

public interface INotificationSettingsFactory {
    NotificationSettings createFromArguments(List<Object> arguments);
}
