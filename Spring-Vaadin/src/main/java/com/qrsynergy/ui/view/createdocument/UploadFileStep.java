package com.qrsynergy.ui.view.createdocument;

import com.qrsynergy.model.QR;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.vaadin.server.StreamVariable;

import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadFileStep implements WizardStep {

    public static final String uploadLocation = "D:\\QRDSP\\github\\Spring-Vaadin\\FILES\\";
    private UploadFinishedHandler uploadFinishedHandler;
    private UploadStateWindow uploadStateWindow = new UploadStateWindow();
    private static final int FILE_COUNT = 1;
    private double uploadSpeed = 100;

    private FirstStepInfo firstStepInfo;
    private VerticalLayout content;

    public UploadFileStep(FirstStepInfo firstStepInfo){
        this.firstStepInfo = firstStepInfo;
    }


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

            try{
                String type = fileName.substring(fileName.lastIndexOf(".") +1);
                if(type.equals("xlsx")){

                    if(firstStepInfo.getUrl() != null){
                        File toBeDeletedFile = new File(uploadLocation + firstStepInfo.getDiskName());
                        toBeDeletedFile.delete();
                    }

                    firstStepInfo.setUrl(UUID.randomUUID().toString());
                    firstStepInfo.setType("xlsx");
                    firstStepInfo.setOriginalName(fileName);
                    Date curDate = new Date();
                    firstStepInfo.setCreatedAt(curDate);
                    firstStepInfo.setLastModified(curDate);
                    firstStepInfo.setDiskName(firstStepInfo.getUrl() + ".xlsx");

                    File targetFile = new File(uploadLocation + firstStepInfo.getDiskName());

                    java.nio.file.Files.copy(
                            stream,
                            targetFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);

                    Notification.show(fileName + " uploaded", Notification.Type.ASSISTIVE_NOTIFICATION);
                }
                else{
                    Notification.show("Only 'xlsx' format is supported", Notification.Type.WARNING_MESSAGE);
                }
            }
            catch(IOException e){
                Notification.show(fileName + " is not uploaded", Notification.Type.WARNING_MESSAGE);
            }
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