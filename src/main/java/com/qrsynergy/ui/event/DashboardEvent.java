package com.qrsynergy.ui.event;

import com.qrsynergy.ui.view.DashboardViewType;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
        private final String email, password;

        public UserLoginRequestedEvent(final String email,final String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static final class UserSignUpFinishedEvent{

    }

    public static final class UserDownloadMobileAppPageRequestedEvent{

    }

    public static final class UserDownloadMobileAppFinishedEvent{

    }

    public static final class ExcelPageRequestedEvent{
        private final String url;

        public ExcelPageRequestedEvent(final String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
    }

    public static final class ExcelPreviousPageEvent{

    }
    /**
     * For creating company in the UI
     * Not used in the production
     */
    public static final class CompanyCreateRequestedEvent{
        private final String name, emailExtension;

        public CompanyCreateRequestedEvent(final String name, final String emailExtension){
            this.name = name;
            this.emailExtension = emailExtension;
        }

        public String getName() {
            return name;
        }

        public String getEmailExtension() {
            return emailExtension;
        }
    }

    public static final class UserSignUpRequestedEvent{

    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }

        public DashboardViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }


}
