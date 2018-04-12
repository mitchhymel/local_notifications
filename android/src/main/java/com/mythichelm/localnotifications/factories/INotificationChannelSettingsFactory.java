package com.mythichelm.localnotifications.factories;

import com.mythichelm.localnotifications.entities.NotificationChannelSettings;

import java.util.List;

public interface INotificationChannelSettingsFactory {
    NotificationChannelSettings createFromArguments(List<Object> arguments);
}
