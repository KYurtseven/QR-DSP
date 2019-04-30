package com.qrsynergy.ui.view;

import com.qrsynergy.controller.helper.ResponseStatusType;
import com.qrsynergy.controller.helper.SignUpResponse;
import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.Company;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.signup.SignUpErrorType;
import com.qrsynergy.ui.view.helper.signup.SignUpValidation;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class SignUpView extends VerticalLayout {

    // TODO
    // Page url to sign up

    final List<Company> companyList;
    TextField email;
    PasswordField password1;
    PasswordField password2;
    TextField fullName;
    ComboBox<String> select;
    Button submit;

    /**
     * Constructor
     */
    public SignUpView() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        companyList = ((DashboardUI) UI.getCurrent()).companyService.findAll();

        Component signUpForm = buildSignUpForm();
        addComponent(signUpForm);
        setComponentAlignment(signUpForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildSignUpForm(){
        final VerticalLayout signUpPanel = new VerticalLayout();
        signUpPanel.setSizeUndefined();
        signUpPanel.setMargin(false);
        Responsive.makeResponsive(signUpPanel);
        signUpPanel.addStyleName("login-panel");

        signUpPanel.addComponent(buildFields());
        return signUpPanel;
    }

    /**
     * Builds name field
     * @return name field
     */
    private Component buildFullName(){
        fullName = new TextField("Name and surname");
        fullName.setIcon(FontAwesome.USER);
        fullName.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        return fullName;
    }


    /**
     * Builds email field
     * @return email field
     */
    private Component buildEmail(){
        email = new TextField("Company email");
        email.setIcon(FontAwesome.USER);
        email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        return email;
    }

    /**
     * Builds password1 field
     * @return password1 field
     */
    private Component buildPassword1(){
        password1 = new PasswordField("Password");
        password1.setIcon(FontAwesome.LOCK);
        password1.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password1.addValueChangeListener(event ->{
            if(password2.getValue().length() == 0){
                // do nothing
            }
            else if(password2.getValue().equals(event.getValue())){
                password2.setRequiredIndicatorVisible(false);
            }
            else{
                password2.setRequiredIndicatorVisible(true);
            }
        });
        return password1;
    }

    /**
     * Builds password2 field
     * @return password2 field
     */
    private Component buildPassword2(){
        password2 = new PasswordField("Verify your password");
        password2.setIcon(FontAwesome.LOCK);
        password2.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password2.setRequiredIndicatorVisible(true);
        password2.addValueChangeListener(event ->{
            if(event.getValue().equals(password1.getValue())){
                password2.setRequiredIndicatorVisible(false);
            }
            else{
                // Not match
                password2.setRequiredIndicatorVisible(true);
            }
        });
        return password2;
    }

    /**
     * Builds company select list
     * @return company select list
     */
    private Component buildCompanySelect(){
        select = new ComboBox<>("Company");
        List<String> companyNames = new ArrayList<>();
        for(Company company: companyList){
            companyNames.add(company.getName());
        }
        select.setItems(companyNames);

        return select;
    }


    /**
     * Builds submit button
     * @return submit button
     */
    private Component buildSubmitButton(){
        submit = new Button("Submit");
        submit.setDisableOnClick(true);
        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SignUpValidation validation = areFieldsValid();
                if(validation.isValid()){
                    // Prepare inputs
                    UserDTO userDTO = new UserDTO(
                            fullName.getValue(),
                            email.getValue(),
                            password1.getValue(),
                            select.getSelectedItem().get()
                    );
                    // save to the database
                    SignUpResponse signUpResponse = ((DashboardUI) UI.getCurrent())
                            .userService.saveUser(userDTO);

                    if(signUpResponse.getStatus().equals(ResponseStatusType.SUCCESS)){
                        // TODO
                        // Show notification about success
                        // After 2 seconds, reload login page
                    }
                    else{
                        // Show notification
                        showNotification(signUpResponse.getMessage().getMessage());
                        submit.setEnabled(true);
                    }
                }
                else{
                    // Show notification
                    showNotification(validation.getErrorMessage());
                    submit.setEnabled(true);
                }
            }
        });
        return submit;
    }

    /**
     * Show notification
     * @param message message
     */
    public void showNotification(String message){
        Notification notification = new Notification(message);
        notification.setDelayMsec(2000);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

    /**
     * Builds all fields
     * @return all fields
     */
    private Component buildFields(){
        VerticalLayout fields = new VerticalLayout();
        fields.addStyleName("fields");

        fields.addComponents(
                buildFullName(),
                buildEmail(),
                buildPassword1(),
                buildPassword2(),
                buildCompanySelect(),
                buildSubmitButton()
        );

        return fields;
    }

    /**
     * Checks if the email is valid or not.
     * Find selected company in the list. Match company's email extension with the
     * user's email extension. They should match for now.
     * Passwords should match.
     * Does NOT check if there is a user with given email.
     * @return true if the input is valid
     */
    private SignUpValidation areFieldsValid(){

        if(EmailValidator.getInstance().isValid(email.getValue())){
            String emailExtension = email.getValue().substring(email.getValue().lastIndexOf("@") + 1);

            String selectedCompanyEmail;
            // Find selected company's email extension
            try{
                selectedCompanyEmail= select.getSelectedItem().get();
            }
            catch(Exception e){
                return new SignUpValidation(false, SignUpErrorType.NOT_SELECTED_COMPANY);
            }
            for(Company company: companyList){
                if(company.getName().equals(selectedCompanyEmail)){
                    String companyEmailExtension = company.getEmailExtension();

                    if(emailExtension.equals(companyEmailExtension)){
                        // Check password equality
                        if(password1.getValue().equals(password2.getValue())){
                            if(password1.getValue().length() == 0){
                                return new SignUpValidation(false, SignUpErrorType.EMPTY_PASSWORD);
                            }
                            if(password1.getValue().length() < 5){
                                return new SignUpValidation(false, SignUpErrorType.SHORT_PASSWORD);
                            }
                            if(fullName.getValue().length() > 3){
                                return new SignUpValidation(true, SignUpErrorType.OK);
                            }
                        }
                        // Tell notification passwords are not matched
                        return new SignUpValidation(false, SignUpErrorType.PASSWORD_NOT_MATCH);
                    }
                    // Tell notification you cannot add company that is not yours
                    return new SignUpValidation(false, SignUpErrorType.EMAIL_COMPANY_EMAIL_NOT_MATCH);
                }
            }
        }
        // Tell notification email is not valid
        return new SignUpValidation(false, SignUpErrorType.INVALID_EMAIL);
    }
}
