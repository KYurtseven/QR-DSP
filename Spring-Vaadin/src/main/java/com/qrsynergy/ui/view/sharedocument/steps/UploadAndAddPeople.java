package com.qrsynergy.ui.view.sharedocument.steps;

import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.view.sharedocument.infos.FileInfo;
import com.qrsynergy.ui.view.sharedocument.ShareDocumentView;
import com.qrsynergy.ui.view.sharedocument.infos.PeopleInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.vaadin.teemu.wizards.WizardStep;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.*;

import java.io.InputStream;

import com.vaadin.server.StreamVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wizard step for uploading the file
 * Once the upload is finished, the data is saved in FirstStepInfo
 * which will be used later when the submit button is pressed
 */
public class UploadAndAddPeople implements WizardStep {

    public static final String uploadLocation = System.getProperty("user.dir") + "\\FILES\\";
    private UploadFinishedHandler uploadFinishedHandler;
    private UploadStateWindow uploadStateWindow = new UploadStateWindow();
    private static final int FILE_COUNT = 1;
    private double uploadSpeed = 100;
    private final int maxRowCount = 8;
    private final int initialGridWith = 600;

    private FileInfo fileInfo;
    private VerticalLayout content;
    private Label uploadedFileNameLabel;
    private List<PeopleInfo> peopleInfoList;

    private Grid<PeopleInfo> grid;

    /**
     * Constructor
     * @param fileInfo stores file information
     * @param peopleInfoList stores added people and their rights
     */
    public UploadAndAddPeople(FileInfo fileInfo, List<PeopleInfo> peopleInfoList){
        this.fileInfo = fileInfo;
        this.peopleInfoList = peopleInfoList;
    }


    /**
     * Title of the wizard step
     * @return
     */
    public String getCaption() {
        return "Upload and Add People";
    }

    /**
     * Builds content
     * @return
     */
    public Component getContent() {
        content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("text-center");

        createUploadFinishedHandler();

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleNames( "text-center", "slot-text-center");


        wrapper.addComponent(buildUpload());
        wrapper.addComponent(buildUploadedFileNameLabel());
        wrapper.addComponent(buildAddPeopleLayout());
        wrapper.addComponent(buildGrid());

        content.addComponent(wrapper);
        return content;
    }

    private Component buildAddPeopleLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleNames("text-center", "slot-text-center");
        TextField newPersonField = new TextField("Person's email:");

        Button addPeopleButton = new Button("Add");
        addPeopleButton.addStyleName("margin-top-25");

        addPeopleButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean valid = EmailValidator.getInstance().isValid(newPersonField.getValue());
                if(valid) {
                    PeopleInfo peopleInfo = new PeopleInfo(newPersonField.getValue());
                    // add to grid's list
                    peopleInfoList.add(peopleInfo);
                    if(peopleInfoList.size() == maxRowCount + 1){
                        grid.setWidth(grid.getWidth() + 10, Unit.PIXELS);
                    }
                    // clear TextField
                    newPersonField.clear();
                    // refresh grid
                    grid.getDataProvider().refreshAll();
                }
                else{
                    Notification.show("Please enter a valid email", Notification.Type.WARNING_MESSAGE);
                }
            }
        });

        layout.addComponents(newPersonField, addPeopleButton);
        return layout;
    }

    private Component buildUploadedFileNameLabel(){
        uploadedFileNameLabel = new Label("Uploaded file name:");
        uploadedFileNameLabel.addStyleName("slot-text-center");

        return uploadedFileNameLabel;
    }

    /**
     * Creates uploadFile area
     */
    private Component buildUpload(){

        final FileUpload fileUpload = new FileUpload(uploadFinishedHandler, uploadStateWindow);
        fileUpload.addStyleName("text-center");

        int maxFileSize = 5242880; //5 MB
        fileUpload.setMaxFileSize(maxFileSize);
        String errorMsgPattern = "File is too big (max = {0}): {2} ({1})";
        fileUpload.setSizeErrorMsgPattern(errorMsgPattern);
        fileUpload.setMaxFileCount(FILE_COUNT);

        fileUpload.getSmartUpload().setUploadButtonCaptions("Upload File", "Upload Files");
        fileUpload.getSmartUpload().setUploadButtonIcon(FontAwesome.UPLOAD);

        return fileUpload;
    }

    /**
     * Builds grid for adding people
     * @return
     */
    private Component buildGrid(){

        grid = new Grid<>();
        grid.setItems(peopleInfoList);

        grid.removeAllColumns();

        grid.addColumn(PeopleInfo::getEmail).
                setCaption("Email")
                .setId("email")
                .setWidth(330)
                .setResizable(false)
                .setSortable(true);

        // TODO
        // Make it sortable
        grid.addComponentColumn(peopleInfo -> buildRightsRadio(peopleInfo))
            .setId("radio")
            .setWidth(170)
            .setCaption("Rights")
            .setResizable(false)
                // Setting sortable true is not enough
            .setSortable(false);

        grid.addComponentColumn(peopleInfo -> buildRemoveEmailButton(peopleInfo))
                .setCaption("Remove")
                .setId("remove")
                .setWidth(100)
                .setResizable(false)
                .setSortable(false);

        grid.setWidth(initialGridWith, Unit.PIXELS);
        // Set fixed max height for rows
        grid.setHeightByRows(maxRowCount);

        grid.addStyleName("slot-text-center");

        return grid;
    }

    /**
     * Builds radio button for changing rights of the user
     * @param peopleInfo user to be changed
     * @return radio button component for each people
     */
    private Component buildRightsRadio(PeopleInfo peopleInfo){
        RadioButtonGroup<RightType> radio = new RadioButtonGroup<>();

        radio.setItems(RightType.EDIT, RightType.VIEW);
        radio.setValue(peopleInfo.getRightType());

        radio.addValueChangeListener(event->{
            if(event.getValue().equals(RightType.EDIT)){
                peopleInfo.setRightType(RightType.EDIT);
            }
            else{
                peopleInfo.setRightType(RightType.VIEW);
            }
        });

        radio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        return radio;
    }

    /**
     * Builds remove button for each email
     * @param peopleInfo people to be removed from the grid
     * @return remove button
     */
    private Component buildRemoveEmailButton(PeopleInfo peopleInfo){
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcons.TRASH);

        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                peopleInfoList.remove(peopleInfo);
                if(peopleInfoList.size() == maxRowCount - 1){
                    grid.setWidth(600, Unit.PIXELS);
                }
                grid.getDataProvider().refreshAll();
            }
        });

        return removeButton;
    }

    /**
     * Saves the file info to the FirstStepInfo class
     * TODO
     * Right now, it only accepts xlsx.
     */
    private void createUploadFinishedHandler() {
        uploadFinishedHandler = (InputStream stream, String fileName, String mimeType, long length, int filesLeftInQueue) -> {

            try{
                String type = fileName.substring(fileName.lastIndexOf(".") +1);
                if(type.equals("xlsx")){
                    fileInfo.setUrl(UUID.randomUUID().toString());
                    // TODO
                    // Right now, it only accepts xlsx.
                    fileInfo.setDocumentType(DocumentType.EXCEL);
                    fileInfo.setOriginalName(fileName);
                    Date curDate = new Date();
                    fileInfo.setCreationDate(curDate);
                    fileInfo.setLastModified(curDate);
                    fileInfo.setDiskName(fileInfo.getUrl() + ".xlsx");

                    // First, convert Input Stream to Byte[]
                    // When writing, convert byte[] to file
                    fileInfo.setFileInBytes(IOUtils.toByteArray(stream));

                    uploadedFileNameLabel.setValue("Uploaded file name: " + fileName);
                }
                else{
                    Notification.show("Only 'xlsx' format is supported", Notification.Type.WARNING_MESSAGE);
                }
            }
            catch(Exception e){
                Notification unhandledUploadFileError = new Notification("Unhandled file upload error is occured");
                unhandledUploadFileError.setDelayMsec(2000);
                unhandledUploadFileError.setPosition(Position.MIDDLE_CENTER);
                unhandledUploadFileError.show(Page.getCurrent());
                System.out.println("Unhandled file upload error: " + e);
            }

        };
    }

    /**
     * It goes forward
     * @return
     */
    public boolean onAdvance() {
        return true;
    }

    /**
     * It does not go backward
     * @return
     */
    public boolean onBack() {
        return false;
    }


    /**
     * Helper class for file upload
     */
    private class FileUpload extends MultiFileUpload {

        public FileUpload(UploadFinishedHandler handler, UploadStateWindow uploadStateWindow) {
            super(handler, uploadStateWindow, false);
        }

        @Override
        protected UploadStatePanel createStatePanel(UploadStateWindow uploadStateWindow) {
            return new FileUploadStatePanel(uploadStateWindow);
        }
    }

    /**
     * Helper class for showing the user upload duration
     */
    private class FileUploadStatePanel extends UploadStatePanel {

        public FileUploadStatePanel(UploadStateWindow window) {
            super(window);
        }

        @Override
        public void onProgress(StreamVariable.StreamingProgressEvent event) {
            try {
                // upload speed
                Thread.sleep((int) uploadSpeed);
            } catch (InterruptedException ex) {
                Logger.getLogger(ShareDocumentView.class.getName()).log(Level.SEVERE, null, ex);
            }
            super.onProgress(event);
        }
    }

}
