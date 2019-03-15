package com.qrsynergy.ui.view.createdocument;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.*;
import java.io.InputStream;

import com.vaadin.server.StreamVariable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadFileStep implements WizardStep {

    private UploadFinishedHandler uploadFinishedHandler;
    private UploadStateWindow uploadStateWindow = new UploadStateWindow();
    private static final int FILE_COUNT = 1;
    private double uploadSpeed = 100;
    private Label uploadedFileLabel;

    private VerticalLayout content;

    public String getCaption() {
        return "Upload file";
    }

    public Component getContent() {
        content = new VerticalLayout();
        content.setMargin(true);

        createUploadFinishedHandler();
        addUpload();

        return content;
    }

    private void addUpload(){
        final FileUpload fileUpload = new FileUpload(uploadFinishedHandler, uploadStateWindow);
        int maxFileSize = 5242880; //5 MB
        fileUpload.setMaxFileSize(maxFileSize);
        String errorMsgPattern = "File is too big (max = {0}): {2} ({1})";
        fileUpload.setSizeErrorMsgPattern(errorMsgPattern);
        fileUpload.setCaption(this.getCaption());
        fileUpload.setPanelCaption(this.getCaption());
        fileUpload.setMaxFileCount(FILE_COUNT);

        fileUpload.getSmartUpload().setUploadButtonCaptions("Upload File", "Upload Files");
        fileUpload.getSmartUpload().setUploadButtonIcon(FontAwesome.UPLOAD);

        content.addComponent(fileUpload,0);

    }

    private void createUploadFinishedHandler() {
        uploadFinishedHandler = (InputStream stream, String fileName, String mimeType, long length, int filesLeftInQueue) -> {
            Notification.show(fileName + " uploaded (" + length + " bytes).", Notification.Type.WARNING_MESSAGE);


            uploadedFileLabel = new Label(fileName);

            content.addComponent(uploadedFileLabel);
        };
    }

    public boolean onAdvance() {
        return true;
    }

    public boolean onBack() {
        return true;
    }


    private class FileUpload extends MultiFileUpload {

        public FileUpload(UploadFinishedHandler handler, UploadStateWindow uploadStateWindow) {
            super(handler, uploadStateWindow, false);
        }

        @Override
        protected UploadStatePanel createStatePanel(UploadStateWindow uploadStateWindow) {
            return new FileUploadStatePanel(uploadStateWindow);
        }
    }

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
                Logger.getLogger(CreateDocumentView.class.getName()).log(Level.SEVERE, null, ex);
            }
            super.onProgress(event);
        }
    }

}
