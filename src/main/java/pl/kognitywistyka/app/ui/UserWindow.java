package pl.kognitywistyka.app.ui;

import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.User;

/**
 * Created by wikto on 19.06.2017.
 */
public class UserWindow extends ItemWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout nameLayout;
    private CssLayout albumNumberLayout;
    private CssLayout emailLayout;

    private HorizontalLayout buttonLayout;

    //Labels
    //todo lol, vaadin, seriously, only labels?
    private Label nameLabel;
    private Label albuNumberLabel;
    private Label emailLabel;

    //Buttons
    private Button previousWindowButton;
    private Button deleteButton;
    private Button addCoursesButton;

    //Variables
    private User user;
    private GridWindow previousWindow;

    public UserWindow(User user, Component previousWindow) {
        setUser(user);
        setPreviousWindow(previousWindow);
        init();
    }

    private void init() {

        setSizeFull();

        //Initializing layout
        middleLayer = new VerticalLayout();
        middleLayer.setSizeFull();
        middleLayer.addStyleName("login-panel");

        init(middleLayer);

        middleLayer.setWidth("600px");
        middleLayer.setHeight("600px");

        //Initializing name
        nameLayout = new CssLayout();
        nameLayout.setSizeFull();

        nameLabel = new Label("Name: " + user.getFirstName() + " " + user.getLastName());

        nameLayout.addComponent(nameLabel);
        middleLayer.addComponent(nameLayout);
        middleLayer.setComponentAlignment(nameLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(nameLayout, 0.1f);

        //Initializing album number
        albumNumberLayout = new CssLayout();
        albumNumberLayout.setSizeFull();

        albuNumberLabel = new Label("Album number: " + user.getId());

        albumNumberLayout.addComponent(albuNumberLabel);
        middleLayer.addComponent(albumNumberLayout);
        middleLayer.setComponentAlignment(albumNumberLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(albumNumberLayout, 0.1f);

        //Initializing email
        //todo


        //Initializing button
        buttonLayout = new HorizontalLayout();

        previousWindowButton = new Button("Return to course list", VaadinIcons.ANGLE_LEFT);
        previousWindowButton.addClickListener(event -> {
            getUI().getCurrent().setContent(previousWindow);
        });

        buttonLayout.addComponent(previousWindowButton);
        middleLayer.addComponent(buttonLayout);
        middleLayer.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);

        deleteButton = new Button("Delete account");
        //it's absolutely not safe todo should add some prompt asking if I'm sure
        deleteButton.addClickListener(event -> {
            StudentService studentService = StudentService.getInstance();
            boolean deleted = studentService.delete(user);
            showNotification(deleted);
            if (deleted) {
                previousWindow.updateGrid();
                getUI().getCurrent().setContent(previousWindow);
            }
        });

        if (!AuthenticationService.isAdmin()) {
            buttonLayout.addComponent(deleteButton);
        }

        if (AuthenticationService.isAdmin() && AuthenticationService.getCurrentLoginInfo().equals(user)) {
            addCoursesButton = new Button("Add courses");
            addCoursesButton.addClickListener(event -> {
                //Initializing submit window
                Window window = new Window("Add courses");
                window.setWidth("300px");
                window.setModal(true);

                //Initializing layout
                FormLayout content = new FormLayout();
                content.setMargin(true);

                //Initializing info button
                Button infoButton = new Button(VaadinIcons.INFO_CIRCLE_O);
                infoButton.addClickListener(infoEvent -> {
                    Notification notification = new Notification("Please, separate each code with a comma.",
                            "Courses will be downloaded from USOSweb UW and added to the database. \n" +
                                    "You may browse them in the course grid.", Notification.Type.ASSISTIVE_NOTIFICATION);
                    notification.show(getUI().getUI().getPage());
                });
                content.addComponent(infoButton);
                content.setComponentAlignment(infoButton, Alignment.TOP_LEFT);

                //Initializing codes field
                TextField codesField = new TextField("Enter codes: ");
                content.addComponent(codesField);

                //Initializing submit button
                Button submitButton = new Button("Submit");
                submitButton.addClickListener(submitEvent -> {
                    boolean submitted = CourseService.addCourses(codesField.getValue());
                    showNotification(submitted);
                    window.close();
                });
                content.addComponent(submitButton);
                content.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
                window.setContent(content);
                getUI().getUI().addWindow(window);
            });

            buttonLayout.addComponent(addCoursesButton);
        }



        initTop();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Component getPreviousWindow() {
        return previousWindow;
    }

    public void setPreviousWindow(Component previousWindow) {
        this.previousWindow = (GridWindow) previousWindow;
    }
}
