package pl.kognitywistyka.app.ui;

import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.ui.forms.CreateAccountForm;
import pl.kognitywistyka.app.user.Student;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by wikto on 19.06.2017.
 */
public class CreateAccountWindow extends CenteredWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private HorizontalLayout buttonsLayout;

    private Label welcomeLabel;

    //Form
    private CreateAccountForm createAccountForm;

    //Buttons
    private Button backButton;
    private Button createAccountButton;

    //Variables
    private StudentService studentService = StudentService.getInstance();


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

        //Initializing form
        createAccountForm = new CreateAccountForm(new Student());
//        createAccountForm.addStyleName("form-centered");

        welcomeLabel = new Label("<p align='center'><b>Create an account...</b></p>");
//        welcomeLabel.addStyleName("caption-label");
        welcomeLabel.setContentMode(ContentMode.HTML);

        middleLayer.addComponent(welcomeLabel);
        middleLayer.addComponent(createAccountForm);

        middleLayer.setExpandRatio(welcomeLabel, 0.1f);
        middleLayer.setExpandRatio(createAccountForm, 0.8f);

        middleLayer.setComponentAlignment(createAccountForm, Alignment.TOP_CENTER);

        middleLayer.setWidth("450px");
        middleLayer.setHeight("600px");
        middleLayer.setSpacing(true);


        //Initializing buttons
        buttonsLayout = new HorizontalLayout();

        backButton = new Button("Return to login screen");
        createAccountButton = new Button("Create account");

        createAccountButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        createAccountButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        buttonsLayout.addComponent(backButton);
        buttonsLayout.addComponent(createAccountButton);

        buttonsLayout.setSpacing(true);


        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        createAccountButton.addClickListener(event -> {
            try {
                Student student = new Student();
                createAccountForm.getStudentBinder().writeBean(student);
                boolean added = studentService.add(student);
                showNotification(added);
                getUI().getCurrent().setContent(new LoginWindow());
            } catch (ValidationException ve) {
                createAccountForm.getStudentBinder().validate();
                Notification.show("You must complete all fields with proper values!", Notification.Type.ERROR_MESSAGE);
            }
        });

        backButton.addClickListener(event -> {
            getUI().setContent(new LoginWindow());
        });

    }
}
