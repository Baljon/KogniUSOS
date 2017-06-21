package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
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

    private HorizontalLayout buttonsLayout;

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
//        middleLayer.addStyleName("login-panel");

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
        buttonsLayout = new HorizontalLayout();

        previousWindowButton = new Button("Return", VaadinIcons.ANGLE_LEFT);
        previousWindowButton.addClickListener(event -> {
            getUI().getCurrent().setContent(previousWindow);
        });

        buttonsLayout.addComponent(previousWindowButton);
        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_LEFT);

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

        //todo when Authentication will be ready change the if statement to commented (admin cannot delete his account!)
//        if (!AuthenticationService.isAdmin() && AuthenticationService.getCurrentLoginInfo().equals(user)) {
        if(true) {
            buttonsLayout.addComponent(deleteButton);
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
                VerticalLayout popupContent = new VerticalLayout();
                popupContent.addComponent(new Label("Please, separate each code with a comma."));
                popupContent.addComponent(new Label("Courses will be downloaded from USOSweb UW and added to the database."));
                popupContent.addComponent(new Label("You may browse them in the course grid."));
                PopupView popup = new PopupView("Info", popupContent);
                content.addComponent(popup);
                popup.setHideOnMouseOut(false);

                //Initializing codes field
                TextField codesField = new TextField("Enter codes: ");
                content.addComponent(codesField);

                //Initializing submit button
                Button submitButton = new Button("Submit");
                submitButton.addClickListener(submitEvent -> {
                    boolean submitted = CourseService.addCourses(codesField.getValue());
                    window.close();
                    showNotification(submitted);
                });
                content.addComponent(submitButton);
                content.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
                window.setContent(content);
                getUI().getUI().addWindow(window);
            });

            buttonsLayout.addComponent(addCoursesButton);
        }
        else if (!AuthenticationService.isAdmin() && AuthenticationService.getCurrentLoginInfo().equals(user)) {
            addCoursesButton = new Button("Propose courses");
            addCoursesButton.addClickListener(event -> {
                //Initializing submit window
                Window window = new Window("Propose courses");
                window.setWidth("300px");
                window.setModal(true);

                //Initializing layout
                FormLayout content = new FormLayout();
                content.setMargin(true);

                //Initializing info button
                VerticalLayout popupContent = new VerticalLayout();
                popupContent.addComponent(new Label("Please, separate each code with a comma."));
                popupContent.addComponent(new Label("Courses must be accepted by an admin."));
                popupContent.addComponent(new Label("Please, check in the course list, you won't be notified."));
                PopupView popup = new PopupView("Info", popupContent);
                content.addComponent(popup);
                popup.setHideOnMouseOut(false);

                //Initializing codes field
                TextField codesField = new TextField("Enter codes: ");
                content.addComponent(codesField);

                //Initializing submit button
                Button submitButton = new Button("Submit");
                submitButton.addClickListener(submitEvent -> {
                    boolean submitted = CourseService.proposeCourses(codesField.getValue());
                    window.close();
                    showNotification(submitted);
                });
                content.addComponent(submitButton);
                content.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
                window.setContent(content);
                getUI().getUI().addWindow(window);
            });

            buttonsLayout.addComponent(addCoursesButton);
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
