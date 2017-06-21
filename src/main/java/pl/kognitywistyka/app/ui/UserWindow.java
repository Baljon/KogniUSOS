package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.User;

/**
 * Created by wikto on 19.06.2017.
 */
public class UserWindow extends CenteredWindow{

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

    //Variables
    private User user;
    private StudentsWindow previousWindow;

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
        deleteButton.addClickListener(event -> {
            StudentService studentService = StudentService.getInstance();
            boolean deleted = studentService.delete(user);
            showNotification(deleted);
            if(deleted) {
                previousWindow.updateGrid();
                getUI().getCurrent().setContent(previousWindow);
            }
        });

        buttonLayout.addComponent(deleteButton);


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
        this.previousWindow = (StudentsWindow) previousWindow;
    }
}
