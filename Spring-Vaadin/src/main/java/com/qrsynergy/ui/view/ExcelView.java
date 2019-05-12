package com.qrsynergy.ui.view;

import com.qrsynergy.GlobalSettings;
import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.CommentEntry;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.qrsynergy.ui.view.sharedocument.steps.UploadAndAddPeople;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ExcelView extends HorizontalLayout {

    private String url;
    private User user;
    private QR qr;
    private Spreadsheet spreadsheet;
    private Comment comment;
    private SliderPanel rightSlider;
    private TextArea textArea;
    private HorizontalLayout root ;
    private VerticalLayout commentBlockLayout;
    private RightType rightType;

    public ExcelView(String url, User user){
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        DashboardEventBus.register(this);
        this.url = url;
        this.user = user;
        // fetch qr from the database
        this.qr = ((DashboardUI) UI.getCurrent()).qrService.findQRByUrl(url);
        this.comment = ((DashboardUI) UI.getCurrent()).commentService.findComment(qr.getUrl());

        rightType = qr.findUsersRightInQR(user.getEmail());

        if(rightType.equals(RightType.NONE)){
            // show notification
            // return to the previous page
            ShowNotification.showWarningNotification("You cannot see this document");
            DashboardEventBus.post(new DashboardEvent.ExcelPreviousPageEvent());
            return;
        }

        addComponent(buildLayout());

    }

    /**
     * Builds layout containing buttons, excel and slider
     * @return
     */
    private Component buildLayout(){
        root = new HorizontalLayout();
        root.setSizeFull();

        initSpreadsheet();
        initSlider();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        root.addComponents(mainLayout, rightSlider);
        root.setExpandRatio(mainLayout, (float) 1);
        root.setExpandRatio(rightSlider, (float) 0);

        HorizontalLayout buttonsLayout = (HorizontalLayout) buildReturnAndSave();

        mainLayout.addComponents(buttonsLayout, spreadsheet);
        mainLayout.setExpandRatio(buttonsLayout, (float)0.05);
        mainLayout.setExpandRatio(spreadsheet, (float)0.95);

        return root;
    }


    /**
     * Builds return to the previous page and save document buttons
     * @return layout containing buttons
     */
    private Component buildReturnAndSave(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        // check whether the user has view rights?
        if(rightType.equals(RightType.OWNER) || rightType.equals(RightType.EDIT)){
            layout.addComponents(returnToMainView(),saveExcelButton());
        }
        else if(rightType.equals(RightType.VIEW) || rightType.equals(RightType.PUBLIC)){
            layout.addComponents(returnToMainView());
        }
        return layout;
    }


    /**
     * Inits slider and builds a text area for adding
     * a new comment.
     *
     */
    private void initSlider(){
        VerticalLayout sliderVerticalLayout = new VerticalLayout();
        sliderVerticalLayout.setId("slider-layout");
        rightSlider = new SliderPanelBuilder(sliderVerticalLayout)
                .expanded(false)
                .mode(SliderMode.RIGHT)
                .caption("Comments")
                .tabPosition(SliderTabPosition.MIDDLE)
                .autoCollapseSlider(true)
                .flowInContent(true)
                .style(SliderPanelStyles.ICON_BLACK)
                //.zIndex(9980)
                .build();

        textArea = new TextArea("");
        textArea.setDescription("Reply...");

        Button submit = new Button("Submit");
        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                 if(textArea.getValue().length() != 0){
                    CommentEntry newEntry = new CommentEntry(user.getFullName(),
                            new Date(),
                            textArea.getValue());

                    ((DashboardUI) UI.getCurrent()).commentService.addCommentEntry(comment, newEntry);

                    textArea.clear();
                    commentBlockLayout.addComponent(buildCommentRow(newEntry));
                 }
            }
        });
        sliderVerticalLayout.addStyleName("padding-left-and-right-10");
        sliderVerticalLayout.addComponents(buildPreviousComments(), textArea, submit);
    }

    /**
     * For each comment entry, build a row
     * @param commentEntry a single comment to be rendered
     * @return component containing single comment and details
     */
    private Component buildCommentRow(CommentEntry commentEntry){
        CssLayout row = new CssLayout();
        row.addStyleName("new_comment");

            CssLayout userComment = new CssLayout();
            userComment.addStyleName("user-comment");

                Image image = new Image("",
                        new ThemeResource("img/profile-pic-300px.jpg"));
                image.addStyleName("user_avatar");

            userComment.addComponent(image);

                CssLayout commentBody = new CssLayout();
                commentBody.addStyleName("comment_body");
                    Label commentLabel = new Label(commentEntry.getMessage());
                    commentLabel.addStyleName("label-word-break");
                commentBody.addComponent(commentLabel);

                CssLayout commentToolbar = new CssLayout();
                commentToolbar.addStyleName("comment_toolbar");

                    CssLayout commentDetails = new CssLayout();
                    commentDetails.addStyleName("comment_details");

                        Label timeLabel1 = new Label(VaadinIcons.CLOCK.getHtml()
                                + commentEntry.getTime(), ContentMode.HTML);
                        Label timeLabel2 = new Label(VaadinIcons.CALENDAR.getHtml()
                                + commentEntry.getDateInDDMMYYYY(), ContentMode.HTML);

                        Label senderLabel = new Label(VaadinIcons.USER.getHtml()
                                + commentEntry.getSenderCamelCase(), ContentMode.HTML);

                        timeLabel1.addStyleName("clock-and-time");
                        timeLabel2.addStyleName("clock-and-time");
                        senderLabel.addStyleName("clock-and-time");
                        commentDetails.addComponents(
                                timeLabel1,
                                timeLabel2,
                                senderLabel
                        );

                commentToolbar.addComponent(commentDetails);

            userComment.addComponents(commentBody, commentDetails);

        row.addComponent(userComment);
        return row;
    }

    /**
     * Builds previous comments
     * @return
     */
    private Component buildPreviousComments(){

        commentBlockLayout = new VerticalLayout();
        commentBlockLayout.addStyleName("comment_block");
        for(CommentEntry commentEntry: comment.getCommentEntries()){
            commentBlockLayout.addComponent(buildCommentRow(commentEntry));
        }
        return commentBlockLayout;
    }

    /**
     * Loads excel file to the spreadsheet component
     */
    private void initSpreadsheet() {
        File sampleFile = new File(GlobalSettings.getUploadLocation() + qr.getUrl() + ".xlsx");
        try {
            spreadsheet = new Spreadsheet(sampleFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return button that saves excel to the disk
     */
    private Component saveExcelButton(){
        Button saveButton = new Button("Save");
        // TODO
        return saveButton;
    }

    /**
     * Builds a button which returns user to the Main View
     * @return button for returning to the main view
     */
    private Component returnToMainView(){
        Button button = new Button("Previous Page");
        button.setIcon(VaadinIcons.BACKSPACE);

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.ExcelPreviousPageEvent());
            }
        });

        return button;
    }
}
