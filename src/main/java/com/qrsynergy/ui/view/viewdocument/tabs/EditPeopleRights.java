package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.data.HasValue;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.HashMap;

public class EditPeopleRights {

    private VerticalLayout content;
    private QR qr;
    private HashMap<String, Boolean> editMap;
    private Grid<String> grid;
    private RightType rightType;
    private User user;
    private final int maxRowCount = 6;
    private final int initialGridWith;


    /**
     * Constructor
     * @param qr QR
     */
    public EditPeopleRights(QR qr, RightType rightType, User user){
        this.qr = qr;
        // VIEW right should not edit the document
        this.rightType = rightType;
        this.user = user;
        if(rightType.equals(RightType.VIEW)){
            // Don't have remove button, less size
            initialGridWith = 430;
        }
        else{
            initialGridWith = 530;
        }
        // maps user rights, edit true, view false
        editMap = buildEditMap(qr);
    }

    /**
     * builds hash map
     * edit is true, view is false
     * @param qr
     * @return
     */
    private HashMap<String, Boolean> buildEditMap(QR qr){
        HashMap<String, Boolean> map = new HashMap<>();
        // User has edit rights
        // He cannot remove/change his rights. Prevent changing by
        // not adding him to the list
        if(rightType.equals(RightType.EDIT)){
            for(String email: qr.getE_info()){
                // don't add the current user
                if(email.equals(this.user.getEmail())){
                    continue;
                }
                map.put(email, true);
            }
        }
        else{
            for(String email: qr.getE_info()){
                map.put(email, true);
            }
        }

        for(String email: qr.getV_info()){
            map.put(email, false);
        }
        return map;
    }


    // TODO
    public Component getContent() {
        content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("text-center");

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleNames("text-center", "slot-text-center");


        // Don't show add people component if the right type is view
        if(!rightType.equals(RightType.VIEW))
            wrapper.addComponent(buildAddPeopleLayout());

        wrapper.addComponent(buildGrid(rightType));

        content.addComponent(wrapper);
        return content;
    }


    /**
     * Builds add people button and the textfield
     * Adds new people to the qr
     * @return component containing button and a textfield
     */
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
                    boolean result = ((DashboardUI) UI.getCurrent()).qrService
                            .addUserToQR(qr, newPersonField.getValue());

                    if(result){
                        ShowNotification.showNotification(newPersonField.getValue() + "" +
                                " is added successfully");
                        newPersonField.clear();
                        refreshGridAndMap();
                    }
                    else{
                        ShowNotification.showWarningNotification("An error occured, please " +
                                "refresh the page");
                    }
                }
                else{
                    ShowNotification.showWarningNotification("Please enter a valid email");
                }
            }
        });

        layout.addComponents(newPersonField, addPeopleButton);
        return layout;
    }

    /**
     * Builds grid
     * If the right type is VIEW, don't allow user to edit
     * @return grid
     */
    private Component buildGrid(RightType rightType){
        grid = new Grid<>(String.class);

        grid.setItems(editMap.keySet());
        grid.removeAllColumns();

        grid.addColumn(String::toString)
                .setCaption("Email")
                .setId("email")
                .setWidth(330)
                .setResizable(false)
                .setSortable(true);

        if(rightType.equals(RightType.VIEW)){
            // build checkbox
            grid.addComponentColumn(email -> buildRightCheckBoxWithoutEdit(email))
                    .setCaption("Can edit")
                    .setId("canedit")
                    .setWidth(100)
                    .setSortable(false)
                    .setResizable(false);
        }
        else{
            // build checkbox
            grid.addComponentColumn(email -> buildRightCheckbox(email))
                    .setCaption("Can edit")
                    .setId("canedit")
                    .setWidth(100)
                    .setSortable(false)
                    .setResizable(false);

            grid.addComponentColumn(email -> buildRemoveEmailButton(email))
                    .setCaption("Remove")
                    .setId("remove")
                    .setWidth(100)
                    .setSortable(false)
                    .setResizable(false);
        }

        grid.setWidth(initialGridWith, Sizeable.Unit.PIXELS);
        grid.setHeightByRows(maxRowCount);
        grid.addStyleName("slot-text-center");
        return grid;
    }

    /**
     * This component is rendered when the user has view rights on the document
     * User cannot change the rights of other users, only see those rights
     * @param email email of the user
     * @return check box component for each user
     */
    private Component buildRightCheckBoxWithoutEdit(String email){
        CheckBox checkbox = new CheckBox();
        checkbox.setValue(editMap.get(email));
        checkbox.setReadOnly(true);
        return checkbox;
    }

    /**
     * Change's user rights from view to edit or edit to view.
     * Selected if the user has edit rights
     * @param email email of the user
     * @return checkbox component
     */
    private Component buildRightCheckbox(String email){
        CheckBox checkbox = new CheckBox();
        checkbox.setValue(editMap.get(email));

        checkbox.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {

                boolean result = ((DashboardUI) UI.getCurrent())
                        .qrService.changeRightOfUser(
                                qr,
                                email,
                                // old right is edit or view?
                                event.getOldValue() ? RightType.EDIT : RightType.VIEW);
                if(result){
                    ShowNotification.showNotification("Right of " + email  + " is changed " +
                            "from " +
                            (event.getOldValue() ? RightType.EDIT : RightType.VIEW) +
                            " to " +
                            (event.getOldValue() ? RightType.VIEW : RightType.EDIT));

                    // refresh edit map
                    // qr is updated
                    refreshGridAndMap();
                }
                else{
                    ShowNotification.showWarningNotification("Could not change the right of the user");
                }
            }
        });
        return checkbox;
    }

    /**
     * Helper method
     * Refreshes grid and hashMap
     */
    private void refreshGridAndMap(){
        editMap = buildEditMap(qr);
        grid.setItems(editMap.keySet());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Builds a button that removes email/user from the QR
     * @return button to remove user from the QR
     */
    private Component buildRemoveEmailButton(String email){
        Button removeButton = new Button("-");
        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                boolean result = ((DashboardUI) UI.getCurrent())
                        .qrService.removeUserFromQR(qr, email);

                if(result){
                    ShowNotification.showNotification(email  + " is removed from the QR");
                    // refresh edit map
                    // qr is updated
                    refreshGridAndMap();
                }
                else{
                    ShowNotification.showWarningNotification("Couldn't delete the email");
                }
            }
        });
        return removeButton;
    }
}
