package com.qrsynergy.ui.view.viewdocument.tabs;

import com.qrsynergy.model.QR;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.data.HasValue;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.HashMap;

public class EditPeopleRights {

    VerticalLayout content;
    QR qr;
    HashMap<String, Boolean> editMap;
    Grid<String> grid;

    private final int maxRowCount = 6;
    private final int initialGridWith = 530;


    /**
     * Constructor
     * @param qr QR
     */
    public EditPeopleRights(QR qr){
        this.qr = qr;
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
        for(String email: qr.getE_info()){
            map.put(email, true);
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

        wrapper.addComponent(buildAddPeopleLayout());
        wrapper.addComponent(buildGrid());

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
     * @return grid
     */
    private Component buildGrid(){
        grid = new Grid<>(String.class);

        grid.setItems(editMap.keySet());
        grid.removeAllColumns();

        grid.addColumn(String::toString)
                .setCaption("Email")
                .setId("email")
                .setWidth(330)
                .setResizable(false)
                .setSortable(true);

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

        grid.setWidth(initialGridWith, Sizeable.Unit.PIXELS);
        grid.setHeightByRows(maxRowCount);
        grid.addStyleName("slot-text-center");
        return grid;
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
