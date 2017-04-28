package com.dim.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class NotificationHelper {
	public static void info(String message) {
		sendNotification(message, NotificationType.INFORMATION);
	}

	public static void error(String message) {
		sendNotification(message, NotificationType.ERROR);
	}

	public static void sendNotification(String message, NotificationType notificationType) {
		Notification notification = new Notification("com.dim.plugin.adbduang",
		                                             "ADB Duang ",
		                                             espaceString(message),
		                                             notificationType);
		Notifications.Bus.notify(notification);
	}
	public static void sendNotification(String message, NotificationType notificationType, NotificationListener notificationListener) {
		Notification notification = new Notification("com.dim.plugin.adbduang",
				"ADB Duang ",
				espaceString(message),
				notificationType,notificationListener);
		Notifications.Bus.notify(notification);
	}



	private static String espaceString(String string) {
		// replace with both so that it returns are preserved in the notification ballon and in the event log
		return string.replaceAll("\n", "\n<br />");
	}
}
