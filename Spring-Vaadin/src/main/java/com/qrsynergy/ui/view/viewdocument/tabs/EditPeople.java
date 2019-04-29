package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.service.QRService;
import com.qrsynergy.ui.DashboardUI;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

public class EditPeople {

    private final String userRemovalConfirmation1 = "Are you sure to remove ";
    private final String userRemovalConfirmation3 = "from";
    private final String userRemovalConfirmation4_edit = " editing this QR";
    private final String userRemovalConfirmation4_view = " viewing this QR";

    VerticalLayout content;
    QR qr;

    public EditPeople(QR qr){
        content = new VerticalLayout();
        this.qr = qr;
    }

    // TODO
    public Component getContent() {
        Responsive.makeResponsive(content);
        content.addComponent(editEditPeople(qr));
        return content;
    }

    private Component editEditPeople(QR qr){
        VerticalLayout verticalLayout = new VerticalLayout();

        Grid<String> grid = new Grid<>(String.class);
        grid.setItems(qr.getE_info());
        grid.removeAllColumns();

        grid.addColumn(String::toString).setCaption("Email");
        grid.addColumn(email ->{
            Button removeButton = new Button("-");
            removeButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {

                    if(((DashboardUI) UI.getCurrent()).qrService
                            .removeUserFromQR(qr, email, RightType.EDIT)){
                        grid.getDataProvider().refreshAll();
                        // TODO
                        // Notification
                    }
                    else{
                        // TODO
                        // Error notification
                    }
                }
            });
            return removeButton;
        }, new ComponentRenderer()).setCaption("Remove");

        TextField newEditPerson = new TextField("New person's email:");

        // TODO
        // rename
        Button addButton = new Button("Add edit");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean valid = EmailValidator.getInstance().isValid(newEditPerson.getValue());
                if(valid){
                    if(((DashboardUI) UI.getCurrent()).qrService
                            .addUserToQR(qr, newEditPerson.getValue(), RightType.EDIT)){
                        grid.getDataProvider().refreshAll();
                        newEditPerson.clear();
                        // TODO
                        // Notification
                    }
                    else{
                        // TODO
                        // Error notification
                    }
                }
                else{
                    // TODO
                    // Notification, enter valid email
                }
            }
        });
        verticalLayout.addComponents(grid, newEditPerson, addButton);
        return verticalLayout;
    }

    /**
     * In this tab, user can add or remore users.
     * Either for editing users or viewing users
     * TODO
     * Do it with grid.
     * @param qr QR
     * @param user_infos QR's user_info
     * @param type view or edit
     * @return
     */
    private Component editPeopleTab(QR qr, List<String> user_infos, String type){

        VerticalLayout layout = new VerticalLayout();
        for(String user_info: user_infos){
            HorizontalLayout hLayout = new HorizontalLayout();
            Label user_infoLabel = new Label(user_info);
            Button removeUserButton = new Button("-");
            removeUserButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    ConfirmDialog.show(UI.getCurrent(), buildConfirmationUserRemoval(user_info, type),
                        new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog confirmDialog) {
                                if(confirmDialog.isConfirmed()){
                                    // TODO
                                }
                                else{
                                    // TODO
                                }
                            }
                        });
                }
            });
        }
        return null;
    }

    /**
     * TODO
     * Better string building
     * @param user_info to be removed user's email
     * @param type view or edit
     * @return string to be rendered
     */
    private String buildConfirmationUserRemoval(String user_info, String type){
        if(type.equals("view")){
            return userRemovalConfirmation1 + user_info + userRemovalConfirmation3 + userRemovalConfirmation4_view;
        }
        else{
            return userRemovalConfirmation1 + user_info + userRemovalConfirmation3 +   userRemovalConfirmation4_edit;
        }
    }
}