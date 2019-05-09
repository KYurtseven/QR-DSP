package com.qrsynergy.ui.view;

import com.qrsynergy.controller.helper.ResponseStatusType;
import com.qrsynergy.controller.helper.SignUpResponse;
import com.qrsynergy.controller.helper.UserDTO;
import com.qrsynergy.model.Company;
import com.qrsynergy.ui.DashboardUI;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.qrsynergy.ui.view.helper.signup.SignUpErrorType;
import com.qrsynergy.ui.view.helper.signup.SignUpValidation;
import com.vaadin.data.HasValue;
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
import java.util.Comparator;
import java.util.List;

public class SignUpView extends VerticalLayout {

    // TODO
    // Page url to sign up

    private final List<Company> companyList;
    private TextField email;
    private PasswordField password1;
    private PasswordField password2;
    private TextField fullName;
    private ComboBox<String> select;
    private Button submit;
    private RadioButtonGroup<String> radio;

    private final String INDIVIDUAL = "Individual";
    private final String COMPANY = "Company";


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
     * User can select whether he is an individual user
     * or a company employee
     * @return radio buttons for company or individual selection
     */
    private Component buildCompanyOrIndividual(){
        radio = new RadioButtonGroup<>("Individual or company account?");
        radio.setItems(INDIVIDUAL, COMPANY);

        radio.setValue(COMPANY);

        radio.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                if(event.getValue().equals(INDIVIDUAL)){
                    // Dont render combo box
                    select.setVisible(false);
                }
                else{
                    // render company selection, combo box
                    select.setVisible(true);
                }
            }
        });
        radio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        return radio;
    }

    /**
     * Builds company select list
     * @return company select list
     */
    private Component buildCompanySelect(){
        select = new ComboBox<>("Company");
        List<String> companyNames = new ArrayList<>();
        for(Company company: companyList){
            if(company.getName().toUpperCase().equals(INDIVIDUAL.toUpperCase())){
                // don't add this to the list
                continue;
            }
            companyNames.add(company.getName());
        }
        // Sort alphabetically
        companyNames.sort(Comparator.comparing(String::toString));
        select.setItems(companyNames);
        // in default, radio is Company
        select.setVisible(true);
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
                            // if the user did not select a company
                            // set it to "INDIVIDUAL"
                            (radio.getValue().equals(INDIVIDUAL)) ? INDIVIDUAL.toUpperCase() : select.getSelectedItem().get()
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
                        ShowNotification.showNotification(signUpResponse.getMessage().getMessage());
                        submit.setEnabled(true);
                    }
                }
                else{
                    // Show notification
                    ShowNotification.showNotification(validation.getErrorMessage());
                    submit.setEnabled(true);
                }
            }
        });
        return submit;
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
                buildCompanyOrIndividual(),
                buildCompanySelect(),
                buildSubmitButton()
        );

        return fields;
    }

    /**
     * Checks whether the email is valid or not. If it is valid:
     *
     * Case 1: User selected INDIVIDUAL
     *  No need to check for company. Check for password equality
     *
     * Case 2: User selected COMPANY
     *  Find the selected company in the drop down.
     *  Get the email extension of the company and the user.
     *  If they are matched check password equality.
     *  If they did not match, user cannot select this
     *
     *  Does NOT check if there is a user with given email.
     *
     * @return true if the input is valid
     */
    private SignUpValidation areFieldsValid(){

        if(EmailValidator.getInstance().isValid(email.getValue())){
            String emailExtension = email.getValue().substring(email.getValue().lastIndexOf("@") + 1);

            if(radio.getValue().equals(INDIVIDUAL)){
                return checkPasswordEquality();
            }

            String selectedCompanyName;
            // Find selected company's name
            try{
                selectedCompanyName= select.getSelectedItem().get();
            }
            catch(Exception e){
                return new SignUpValidation(false, SignUpErrorType.NOT_SELECTED_COMPANY);
            }
            for(Company company: companyList){
                if(company.getName().equals(selectedCompanyName)){
                    String companyEmailExtension = company.getEmailExtension();

                    if(emailExtension.equals(companyEmailExtension)){
                        return checkPasswordEquality();
                    }
                    // Tell notification you cannot add company that is not yours
                    return new SignUpValidation(false, SignUpErrorType.EMAIL_COMPANY_EMAIL_NOT_MATCH);
                }
            }
        }
        // Tell notification email is not valid
        return new SignUpValidation(false, SignUpErrorType.INVALID_EMAIL);
    }

    /**
     * Checks equality of the two password field
     * Their values should be same
     * They should be more than 5 char
     * If those conditions are satisfied, check the name field of the input
     * It should be more than 3 char
     * @return
     */
    private SignUpValidation checkPasswordEquality(){
        // Check password equality
        if(password1.getValue().equals(password2.getValue())){
            if(password1.getValue().length() == 0){
                return new SignUpValidation(false, SignUpErrorType.EMPTY_PASSWORD);
            }
            if(password1.getValue().length() < 5){
                return new SignUpValidation(false, SignUpErrorType.SHORT_PASSWORD);
            }
            if(fullName.getValue().length() < 3){
                return new SignUpValidation(false, SignUpErrorType.NOT_VALID_NAME);
            }
            else{
                return new SignUpValidation(true, SignUpErrorType.OK);
            }
        }
        // Tell notification passwords are not matched
        return new SignUpValidation(false, SignUpErrorType.PASSWORD_NOT_MATCH);
    }
}
