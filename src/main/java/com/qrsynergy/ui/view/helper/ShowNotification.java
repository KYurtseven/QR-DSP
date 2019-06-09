package com.qrsynergy.ui.view.helper;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class ShowNotification {

    /**
     * Show notification, default message
     * @param message message
     */
    public static void showNotification(String message){
        Notification notification = new Notification(message);
        notification.setDelayMsec(2000);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

    /**
     * Shows yellow warning notification
     * @param message message
     */
    public static void showWarningNotification(String message){
        Notification notification = new Notification(message, Notification.Type.WARNING_MESSAGE);
        notification.setDelayMsec(2000);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

}
