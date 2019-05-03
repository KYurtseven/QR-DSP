package com.qrsynergy.ui.view.sharedocument.steps;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.Wizard;
import com.vaadin.shared.Registration;
import org.apache.commons.validator.routines.EmailValidator;

@Deprecated
public class AddPeopleStep implements WizardStep {

    private String type;
    private VerticalLayout content = new VerticalLayout();
    private Button addToListButton = new Button("Add to List");
    private Button deleteFromListButton = new Button("");
    private TextField emailField = new TextField("Enter email address");
    private NativeSelect<String> select;
    private ListDataProvider<String> dataProvider;
    private Label titleLabel;

    // Listeners
    private Registration addToListRegistration;
    private Registration deleteFromListRegistration;
    private Registration valueChangeRegistration;
    private Registration nextPageRegistration;
    private Registration prevPageRegistration;

    private Wizard owner;

    /**
     * @param type is showing the user page's title
     * @param select added emails is listed here
     * @param dataProvider dataProvider for select param
     * @param owner parent wizard
     */
    public AddPeopleStep(String type,
                         NativeSelect<String> select,
                         ListDataProvider<String> dataProvider,
                         Wizard owner){
        this.type = type;
        this.select = select;
        this.dataProvider = dataProvider;
        this.owner = owner;

        if(type.equals("view")){
            titleLabel = new Label("Individual view rights");
        }
        else{
            titleLabel = new Label("Individual edit rights");
        }
    }

    /**
     * Shows user current step's name
     * @return current step's name
     */
    public String getCaption() {
        if(type.equals("view")){
            return "View";
        }
        return "Edit";
    }

    /**
     * Wizard step's constructor
     * Every time the user pressed next button to render this page
     * this function will be called
     * @return Wizard component
     */
    public Component getContent() {
        Responsive.makeResponsive(content);
        content.addStyleName("login-panel");

        // Width is too narrow. Make it responsive.
        select.setVisibleItemCount(5);
        select.setWidth("250px");
        select.setDataProvider(dataProvider);
        select.setEmptySelectionAllowed(false);
        deleteFromListButton.setEnabled(false);
        content.setMargin(true);

        initiateListeners();

        emailField.setIcon(FontAwesome.USER);
        emailField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        deleteFromListButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        deleteFromListButton.setIcon(FontAwesome.REMOVE);


        HorizontalLayout row1 = new HorizontalLayout();
        row1.addComponents(titleLabel, emailField, addToListButton);

        HorizontalLayout row2 = new HorizontalLayout();
        row2.addComponents(select, deleteFromListButton);
        content.addComponents(row1, row2);

        return content;
    }

    /**
     * Initialize listeners
     *
     * addToListRegistration listens Add To List Button
     * deleteFromListRegistration listens Remove Button
     * valueChangeRegistration listens adding an element to the
     * list or removing element from the list
     *
     * nextPageRegistration listens for the wizard. If the nextpage button is pressed
     * remove all listeners in this page. Otherwise, if the user presses back-next
     * again and again, everywhere will be filled with button listeners.
     *
     * prevPageRegistration, same as above.
     */
    public void initiateListeners(){
        addToListRegistration = addToListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addEmailToList(emailField.getValue());
            }
        });
        deleteFromListRegistration = deleteFromListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                select.getSelectedItem().
                        ifPresent(selectedItem -> removeSelectedEmailFromList());
            }
        });
        valueChangeRegistration = select.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                deleteFromListButton.setEnabled(select.getSelectedItem().isPresent());
            }
        });

        nextPageRegistration = owner.getNextButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeListeners();
            }
        });
        prevPageRegistration = owner.getBackButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeListeners();
            }
        });
    }


    /**
     * Removes this page's listeners.
     *
     */
    public void removeListeners(){
        addToListRegistration.remove();
        deleteFromListRegistration.remove();
        valueChangeRegistration.remove();
        nextPageRegistration.remove();
        prevPageRegistration.remove();
    }

    /**
     * adds email to the Native select,
     * clears the text field
     *
     * @param email
     *
     * checks email
     * if email is not appropriate, don't accept,
     * show user a notification/warning
     */
    private void addEmailToList(String email){
        boolean valid = EmailValidator.getInstance().isValid(email);
        if(valid){
            dataProvider.getItems().add(email);
            select.getDataProvider().refreshAll();
            emailField.clear();
        }
        else{
            // Not valid email
            // Show notification to the user
            Notification notValidEmailNotification = new Notification("Email address is not valid");
            notValidEmailNotification.setDelayMsec(2000);
            notValidEmailNotification.setPosition(Position.MIDDLE_CENTER);
            notValidEmailNotification.show(Page.getCurrent());
        }
    }

    /**
     * removes selected email from the list
     * when remove button is pressed
     */
    private void removeSelectedEmailFromList(){
        select.getSelectedItem().ifPresent(selectedItem->{
            dataProvider.getItems().remove(selectedItem);
        });
        select.setSelectedItem(dataProvider.getItems().isEmpty()? null: dataProvider.getItems().iterator().next());
        select.getDataProvider().refreshAll();
    }

    public boolean onAdvance() {
        return true;
    }

    public boolean onBack() {

        return true;
    }
}
