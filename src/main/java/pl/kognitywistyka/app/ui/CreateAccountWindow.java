package pl.kognitywistyka.app.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by wikto on 19.06.2017.
 */
public class CreateAccountWindow extends CenteredWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private FormLayout inputBoxes;
    private HorizontalLayout buttonsLayout;

    private Label welcomeLabel;

    //TextFields
    private TextField userField;
    private TextField emailField;
    private TextField firstNameField;
    private TextField lastNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField albumNumberField;

    //Buttons
    private Button backButton;
    private Button addAccountButton;

    //Constructor
    public CreateAccountWindow() {
        init();
    }

    private void init() {
        setSizeFull();

        //Initializing layout
        middleLayer = new VerticalLayout();
        middleLayer.setSizeFull();
//        middleLayer.addStyleName("login-panel");

        init(middleLayer);

        inputBoxes = new FormLayout();
        buttonsLayout = new HorizontalLayout();

//        inputBoxes.addStyleName("form-centered");

        welcomeLabel = new Label("<p align='center'><b>Create an account...</b></p>");
//        welcomeLabel.addStyleName("caption-label");
        welcomeLabel.setContentMode(ContentMode.HTML);

        middleLayer.addComponent(welcomeLabel);
        middleLayer.addComponent(inputBoxes);
        middleLayer.addComponent(buttonsLayout);

        middleLayer.setExpandRatio(welcomeLabel, 0.4f);
        middleLayer.setExpandRatio(buttonsLayout, 0.8f);

        middleLayer.setComponentAlignment(inputBoxes, Alignment.MIDDLE_CENTER);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);

        middleLayer.setWidth("450px");
        middleLayer.setHeight("600px");

        //Initializing fields
        userField = new TextField("PESEL: ");
        albumNumberField = new TextField("Album number: ");

        firstNameField = new TextField("First name: ");
        lastNameField = new TextField("Last name: ");

        passwordField = new PasswordField("Enter password: ");
        confirmPasswordField = new PasswordField("Confirm password: ");

        emailField = new TextField("Email: ");

        inputBoxes.addComponent(userField);
        inputBoxes.addComponent(albumNumberField);
        inputBoxes.addComponent(firstNameField);
        inputBoxes.addComponent(lastNameField);
        inputBoxes.addComponent(passwordField);
        inputBoxes.addComponent(confirmPasswordField);
        inputBoxes.addComponent(emailField);

        //Initializing buttons
        addAccountButton = new Button("Create account");
        backButton = new Button("Return to login screen");

        buttonsLayout.addComponent(addAccountButton);
        buttonsLayout.addComponent(backButton);
        buttonsLayout.setSpacing(true);

        addAccountButton.addClickListener(event -> {
            throw new NotImplementedException();
            //TODO when authentication logic will be ready
        });

        backButton.addClickListener(event -> {
            getUI().setContent(new LoginWindow());
        });

    }
}
