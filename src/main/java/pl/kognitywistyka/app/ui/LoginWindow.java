package pl.kognitywistyka.app.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wikto on 19.06.2017.
 */
public class LoginWindow extends CenteredWindow {

//    private static final long serialVersionUID = 1L;

    //Layouts
    private VerticalLayout middleLayer;
    private FormLayout inputBoxes;
    private HorizontalLayout buttonsLayout;

    private Label welcomeLabel;

    //Fields
    private TextField userField;
    private PasswordField passwordField;

    //Buttons
    private Button loginButton;
    private Button reminderButton;
    private Button createAccount;

    public LoginWindow() {
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

        welcomeLabel = new Label("<p align='center'><b>WELCOME TO KogniUSOS!</b></p>");
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
        middleLayer.setHeight("300px");

        //Initializing fields
        userField = new TextField("PESEL: ");
        passwordField = new PasswordField("Password: ");

        inputBoxes.addComponent(userField);
        inputBoxes.addComponent(passwordField);

        //Initializing buttons
        loginButton = new Button("Sign in");
        loginButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//        reminderButton = new Button("Retrieve password");
        createAccount = new Button("Sign up");

        buttonsLayout.addComponent(loginButton);
//        buttonsLayout.addComponent(reminderButton);
        buttonsLayout.addComponent(createAccount);
        buttonsLayout.setSpacing(true);

//        reminderButton.addClickListener(event -> {
//           throw new NotImplementedException();
////            getUI().setContent(new SendReminderWindow());
//        });

        loginButton.addClickListener(event -> {
            getUI().setContent(new MainWindow());
            //TODO login logic
//            if (AuthenticationService.validateLogin(userField.getValue(), passwordField.getValue())) {
//                getUI().setContent(new MainWindow());
//            } else {
//                Notification.show("Niepoprawne dane logowania.", Notification.Type.ERROR_MESSAGE);
//            }
        });

        createAccount.addClickListener(event -> {
            //throw new NotImplementedException();
            getUI().setContent(new CreateAccountWindow());
        });
    }

}
