package com.qrsynergy.ui.view;

import com.qrsynergy.ui.view.dashboard.DashboardView;
import com.qrsynergy.ui.view.createdocument.ShareDocumentView;
import com.qrsynergy.ui.view.viewdocument.ViewDocumentView;
import com.qrsynergy.ui.view.dynamicform.CreateDynamicFormView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import java.awt.*;

public enum DashboardViewType {
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, false),
    SHAREDOCUMENT("Share Document", ShareDocumentView.class, FontAwesome.FILE_EXCEL_O, false),
    VIEWDOCUMENT("View Document", ViewDocumentView.class, FontAwesome.EYE, false),
    CREATEDYNAMIC("Create Dynamic Form", CreateDynamicFormView.class, FontAwesome.TWITCH /*TODO*/, false )
    ;

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
                              final Class<? extends View> viewClass, final Resource icon,
                              final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
