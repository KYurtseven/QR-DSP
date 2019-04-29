package com.qrsynergy.ui.view.sharedocument.steps;

import com.qrsynergy.model.helper.DocumentType;
import com.qrsynergy.ui.view.sharedocument.infos.FirstStepInfo;
import com.qrsynergy.ui.view.sharedocument.ShareDocumentView;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.apache.commons.io.IOUtils;
import org.vaadin.teemu.wizards.WizardStep;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.*;

import java.io.InputStream;

import com.vaadin.server.StreamVariable;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wizard step for uploading the file
 * Once the upload is finished, the data is saved in FirstStepInfo
 * which will be used later when the submit button is pressed
 */
public class UploadFileStep implements WizardStep {

    // TODO
    public static final String uploadLocation = "D:\\QRDSP\\github\\Spring-Vaadin\\FILES\\";
    private UploadFinishedHandler uploadFinishedHandler;
    private UploadStateWindow uploadStateWindow = new UploadStateWindow();
    private static final int FILE_COUNT = 1;
    private double uploadSpeed = 100;

    private FirstStepInfo firstStepInfo;
    private VerticalLayout content;
    /**
     * Constructor
     * @param firstStepInfo
     */
    public UploadFileStep(FirstStepInfo firstStepInfo){
        this.firstStepInfo = firstStepInfo;
    }


    /**
     * Title of the wizard step
     * @return
     */
    public String getCaption() {
        return "Upload";
    }

    public Component getContent() {
        content = new VerticalLayout();
        content.setMargin(true);

        createUploadFinishedHandler();
        addUpload();

        return content;
    }

    /**
     * Creates uploadFile area
     */
    private void addUpload(){
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();

        final FileUpload fileUpload = new FileUpload(uploadFinishedHandler, uploadStateWindow);
        int maxFileSize = 5242880; //5 MB
        fileUpload.setMaxFileSize(maxFileSize);
        String errorMsgPattern = "File is too big (max = {0}): {2} ({1})";
        fileUpload.setSizeErrorMsgPattern(errorMsgPattern);
        fileUpload.setMaxFileCount(FILE_COUNT);

        fileUpload.getSmartUpload().setUploadButtonCaptions("Upload File", "Upload Files");
        fileUpload.getSmartUpload().setUploadButtonIcon(FontAwesome.UPLOAD);

        vl.addComponent(fileUpload);
        vl.setComponentAlignment(fileUpload, Alignment.MIDDLE_CENTER);
        vl.setResponsive(true);
        content.addComponent(vl);
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
                    firstStepInfo.setUrl(UUID.randomUUID().toString());
                    // TODO
                    // Right now, it only accepts xlsx.
                    firstStepInfo.setDocumentType(DocumentType.EXCEL);
                    firstStepInfo.setOriginalName(fileName);
                    Date curDate = new Date();
                    firstStepInfo.setCreationDate(curDate);
                    firstStepInfo.setLastModified(curDate);
                    firstStepInfo.setDiskName(firstStepInfo.getUrl() + ".xlsx");

                    // First, convert Input Stream to Byte[]
                    // When writing, convert byte[] to file
                    firstStepInfo.setFileInBytes(IOUtils.toByteArray(stream));
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
