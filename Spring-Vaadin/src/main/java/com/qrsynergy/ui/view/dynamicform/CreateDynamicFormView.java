package com.qrsynergy.ui.view.dynamicform;

import com.qrsynergy.ui.event.DashboardEventBus;
import com.qrsynergy.ui.view.dynamicform.components.*;
import com.qrsynergy.ui.view.helper.ShowNotification;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateDynamicFormView extends Panel implements View {

    public static final String TITLE_ID = "createdynamicform-title";

    private final VerticalLayout root;
    private VerticalLayout formLayout;
    private Form form ;

    /**
     * Constructor
     */
    public CreateDynamicFormView(){
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        form = new Form();
        root = new VerticalLayout();
        root.setSizeUndefined();
        root.setId("dynamic_root");
        root.setSpacing(false);
        // TODO
        // generate style name generate qr view
        root.addStyleName("sales");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());


        Component content =  buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
    }


    /**
     * Adds title
     * TODO
     * Every Page will implement this
     * Maybe write it as a Interface???
     * @return
     */
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        Label titleLabel = new Label("Create New Dynamic Form");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildContent() {

        formLayout = new VerticalLayout();

        JSONParser parser = new JSONParser();

        String checkboxJson = "{\r\n    \"label\": \"Piston parts\",\r\n    \"buttons\": [\r\n      { \"checkName\": \"R545\", \"checked\": true },\r\n      { \"checkName\": \"Hybrid S99\", \"checked\": false },\r\n      { \"checkName\": \"F20 G-84\", \"checked\": true }\r\n    ]\r\n  }";
        String radiobuttonJson = "{\r\n    \"label\": \"Engine type\",\r\n    \"radioNames\": [\"Diesel\", \"Otto\", \"Atkinson\"],\r\n    \"selected\": \"Diesel\"\r\n  }";
        String listJson = "{\r\n    \"label\": \"TODO List\",\r\n    \"texts\": [\"Images\", \"Read only forms\", \"Generate QR code\"]\r\n  }";
        String imageJson = "{\r\n    \"label\": \"Carburetor Images\",\r\n    \"image_urls\": [\r\n      \"https://images-na.ssl-images-amazon.com/images/I/81MMZQdS9xL._SL1500_.jpg\",\r\n      \"https://images-na.ssl-images-amazon.com/images/I/71d9HebQx1L._SL1500_.jpg\",\r\n      \"https://images-na.ssl-images-amazon.com/images/I/911oxThpSNL._SL1500_.jpg\"\r\n    ],\r\n    \"labels\": [\"Carb 2100\", \"Edelbrock 1405\", \"Holley 0-80457sa\"]\r\n  }";

        form.editable = true;
        // CheckboxPart checkboxPart = new CheckboxPart("Piston parts");
        // checkboxPart.Add("R545", true);
        // checkboxPart.Add("Hybrid S99", false);
        // checkboxPart.Add("F20 G-84", true);
        // checkboxPart.AddToView(this);
        CheckboxPart checkboxPart = new CheckboxPart("Dummy", form);
        try {
            checkboxPart = new CheckboxPart((JSONObject) parser.parse(checkboxJson), form);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // CheckboxPart checkboxPart = new
        // CheckboxPart((JSONObject)JSONParser.parseJSON(checkboxJson));


        RadioButtonPart radioButtonPart = new RadioButtonPart("Dummy",form);
        try {
            radioButtonPart = new RadioButtonPart((JSONObject) parser.parse(radiobuttonJson),form);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ListPart listPart = new ListPart("Dummy",form);
        try {
            listPart = new ListPart((JSONObject) parser.parse(listJson),form);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextPart textPart = new TextPart("Dummy",form);
        try {
            textPart = new TextPart((JSONObject) parser.parse(listJson),form);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ImagePart imagePart = new ImagePart("Dummy",form);
        try {
            imagePart = new ImagePart((JSONObject) parser.parse(imageJson),form);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // TextPart jsonPart = new TextPart("Converted JSONs");
        // jsonPart.Add(imagePart.ToJson());
        // jsonPart.AddToView(form);

        form.AddFormPart(checkboxPart);
        form.AddFormPart(radioButtonPart);
        form.AddFormPart(listPart);
        form.AddFormPart(textPart);
        form.AddFormPart(imagePart);




        formLayout.addComponents(
                buildButtons("Add checkbox", DynamicComponentType.CHECKBOX),
                buildButtons("Add radio button", DynamicComponentType.RADIOBUTTON),
                buildButtons("Add text", DynamicComponentType.TEXT),
                buildButtons("Add image", DynamicComponentType.IMAGE),
                buildButtons("Add list", DynamicComponentType.LIST)
        );

        form.InitView(formLayout);

        return formLayout;
    }

    /**
     * Builds buttons for each dynamic component type
     * @param caption
     * @param componentType
     * @return
     */
    public Component buildButtons(String caption, DynamicComponentType componentType){
        Button button = new Button(caption);
        button.addClickListener(event -> {
            FormPart fp = CreateFormPart(componentType, form);
            form.AddFormPart(fp); // Create and add form part
            form.AddToView(fp); // Updates layout

            ShowNotification.showNotification("FormPart created");
        });
        return button;
    }


    public FormPart CreateFormPart(DynamicComponentType type, Form parentForm) {
        switch (type) {
            case CHECKBOX:
                CheckboxPart checkboxPart = new CheckboxPart("Dummy", parentForm);
                checkboxPart.Add("Selection 1", false);
                checkboxPart.Add("Selection 2", false);
                checkboxPart.Add("Selection 3", false);
                return checkboxPart;

            case RADIOBUTTON:
                RadioButtonPart radioButtonPart = new RadioButtonPart("Engine type", parentForm);
                radioButtonPart.Add("Diesel");
                radioButtonPart.Add("Otto");
                radioButtonPart.Add("Atkinson");

                return radioButtonPart;

            case TEXT:
                TextPart textPart = new TextPart("Description", parentForm);
                textPart.Add(
                "This app creates QR codes for documents. Lorem ipsum dolor sit amet, " +
                        "id partem detraxit vis, case vidit prima id eam. Dignissim adipiscing " +
                        "contentiones quo ad, ei latine labores menandri eum, sit ea nullam gloriatur " +
                        "intellegebat. Graece consequuntur in eos, mea ei error delicata, sale eirmod " +
                        "intellegat mea cu. Duo zril mandamus assentior ei, dolor veniam adversarium " +
                        "mel in.");
                return textPart;

            case IMAGE:
                ImagePart imagePart = new ImagePart("Carburetor Images", parentForm);
                imagePart.Add("Carb 2100",
                "https://images-na.ssl-images-amazon.com/images/I/81MMZQdS9xL._SL1500_.jpg");
                imagePart.Add("Edelbrock 1405",
                "https://images-na.ssl-images-amazon.com/images/I/71d9HebQx1L._SL1500_.jpg");
                imagePart.Add("Holley 0-80457sa",
                "https://images-na.ssl-images-amazon.com/images/I/911oxThpSNL._SL1500_.jpg");
                return imagePart;

            case LIST:
                ListPart listPart = new ListPart("TODO List", parentForm);
                listPart.Add("Images");
                listPart.Add("Read only forms");
                listPart.Add("Generate QR code");

                return listPart;
        }
        return null;
    }
}