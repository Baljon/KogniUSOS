package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;
import pl.kognitywistyka.app.user.User;

import java.util.ArrayList;

/**
 * Created by wikto on 20.06.2017.
 */
public class CourseWindow extends ItemWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout courseNameLayout;
    private CssLayout facultyLayout;
    private CssLayout lecturerLayout;
    private CssLayout syllabusLayout;

    private HorizontalLayout buttonLayout;

    //Labels
    //todo lol, vaadin, seriously, only labels?
    private Label courseNameLabel;
    private Label facultyLabel;
    private Label lecturerLabel;
    private Label syllabusLabel;

    //Buttons
    private Button previousScreenButton;

    //Variables
    private Course course;
    private GridWindow previousWindow;
    private Button registerDeleteButton;

    public CourseWindow(Course course, Component previousWindow) {
        setCourse(course);
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
        courseNameLayout = new CssLayout();
        courseNameLayout.setSizeFull();

        courseNameLabel = new Label("Course Name: " + course.getCourseName());

        courseNameLayout.addComponent(courseNameLabel);
        middleLayer.addComponent(courseNameLayout);
        middleLayer.setComponentAlignment(courseNameLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(courseNameLayout, 0.1f);

        //Initializing faculty
        facultyLayout = new CssLayout();
        facultyLayout.setSizeFull();

        facultyLabel = new Label("Faculty: " + course.getFaculty());

        facultyLayout.addComponent(facultyLabel);
        middleLayer.addComponent(facultyLayout);
        middleLayer.setComponentAlignment(facultyLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(facultyLayout, 0.1f);

        //Initializing lecturer
        //todo

        //Initializing syllabus
        //todo

        //Initializing buttons
        buttonLayout = new HorizontalLayout();

        previousScreenButton = new Button("Return to course list", VaadinIcons.ANGLE_LEFT);
        previousScreenButton.addClickListener(event -> {
            getUI().getCurrent().setContent(previousWindow);
        });

        buttonLayout.addComponent(previousScreenButton);
        middleLayer.addComponent(buttonLayout);
        middleLayer.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);

        if(!AuthenticationService.isAdmin()) {
            registerDeleteButton = new Button("Register");
            registerDeleteButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

            registerDeleteButton.addClickListener(event -> {
                //Initializing warning window
                Window window = new Window();

                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_DANGER);

                cancelButton.addClickListener(clickEvent -> {
                    window.close();
                });

                sureButton.addClickListener(clickEvent -> {
                    boolean registered = CourseService.register(course);
                    window.close();
                    showNotification(registered);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(window, buttonsList));
            });
        } else {
            registerDeleteButton = new Button("Delete");
            registerDeleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

            registerDeleteButton.addClickListener(event -> {
                //Initializing warning window
                Window window = new Window();

                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_DANGER);

                cancelButton.addClickListener(clickEvent -> {
                    window.close();
                });

                sureButton.addClickListener(clickEvent -> {
                    CourseService courseService = CourseService.getInstance();
                    boolean deleted = courseService.delete(course);
                    if(deleted) {
                        previousWindow.updateGrid();
                        getUI().getCurrent().setContent(previousWindow);
                    }
                    window.close();
                    showNotification(deleted);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(window, buttonsList));
            });
        }

        buttonLayout.addComponent(registerDeleteButton);
        middleLayer.addComponent(buttonLayout);
        middleLayer.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);

        initTop();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setPreviousWindow(Component previousWindow) {
        this.previousWindow = (GridWindow) previousWindow;
    }
}
