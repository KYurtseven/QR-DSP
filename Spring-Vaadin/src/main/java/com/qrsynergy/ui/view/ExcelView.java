package com.qrsynergy.ui.view;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.CommentEntry;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.event.DashboardEvent;
import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.sharedocument.steps.UploadAndAddPeople;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import java.io.File;
import java.io.IOException;
import java.util.Date;

// TODO
// Is this necessary?
@Theme("mytheme")
public class ExcelView extends HorizontalLayout {

    private String url;
    private User user;
    private QR qr;
    private Spreadsheet spreadsheet;
    private Comment comment;
    private SliderPanel rightSlider;
    private TextArea textArea;
    private HorizontalLayout root ;

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
        addComponent(buildLayout());

    }

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
     * Builds return and save buttons
     * TODO
     * Don't render save button if the user has view rights
     * @return layout containing buttons
     */
    private Component buildReturnAndSave(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.addComponents(returnToMainView(),saveExcelButton());
        return layout;
    }


    private void initSlider()
    {
        VerticalLayout sliderVerticalLayout = new VerticalLayout();

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

                 }
            }
        });
        sliderVerticalLayout.addComponents(buildPreviousComments(), textArea, submit);

    }

    private Component buildCommentRow(){
        return null;
    }

    /**
     * Builds previous comments
     * @return
     */
    private Component buildPreviousComments(){

        VerticalLayout layout = new VerticalLayout();
        for(CommentEntry commentEntry: comment.getCommentEntries()){
            VerticalLayout row = new VerticalLayout();
            Label senderName = new Label(commentEntry.getSender());

            Label sendDate = new Label(commentEntry.getDateInDDMMYYYY());
            Label message = new Label(commentEntry.getMessage());
            row.addComponents(senderName, sendDate, message);

            layout.addComponent(row);
        }
        return layout;
    }

    private void initSpreadsheet() {
        File sampleFile = new File(UploadAndAddPeople.uploadLocation + qr.getUrl() + ".xlsx");
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
        Button saveButton = new Button();
        saveButton.setIcon(VaadinIcons.CLOUD_UPLOAD);
        // TODO
        return saveButton;
    }

    /**
     * Builds a button which returns user to the Main View
     * @return button for returning to the main view
     */
    private Component returnToMainView(){
        Button button = new Button();
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
