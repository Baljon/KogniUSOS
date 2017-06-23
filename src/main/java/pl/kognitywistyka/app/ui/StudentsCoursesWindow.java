package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.Student;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by wikto on 22.06.2017.
 */
public class StudentsCoursesWindow extends GridWindow<Course> {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<Course> grid;

    //Filter
    private TextField filterField;
    private Button clearFilterFieldButton;

    //Button
    private Button unregisterButton;
    private Button coursesButton;

    //variables
    //todo should it be here?
    private Set<Course> selectedCourses;
    private StudentService studentService = StudentService.getInstance();
    private CourseService courseService = CourseService.getInstance();

    public StudentsCoursesWindow() {
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

        //Initializing filtering
        filterLayout = new CssLayout();
        filterLayout.setSizeFull();

        filterField = new TextField();

        filterField.setPlaceholder("filter by name...");
        filterField.addValueChangeListener(e -> updateGrid());
        filterField.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterFieldButton = new Button(VaadinIcons.CLOSE_SMALL);

        clearFilterFieldButton.setDescription("Clear the current filter");
        clearFilterFieldButton.addClickListener(e -> filterField.clear());

        filterLayout.addComponents(filterField, clearFilterFieldButton);
        filterLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        middleLayer.addComponent(filterLayout);
        middleLayer.setComponentAlignment(filterLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(filterLayout, 0.1f);

        //Initializing grid
        grid = new Grid<>(Course.class);

        grid.addColumn(Course::getId).setCaption("USOS code");
        grid.addColumn(Course::getCourseName).setCaption("Course Name");
        grid.addColumn(Course::getFaculty).setCaption("Faculty");
        grid.setColumns("id", "courseName", "faculty");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addColumn(event -> "View",
                new ButtonRenderer<>(clickEvent ->
                        getUI().getCurrent().setContent(new CourseWindow(clickEvent.getItem(), getUI().getCurrent().getContent()))));

        grid.addSelectionListener(event -> {
            selectedCourses = event.getAllSelectedItems();
            unregisterButton.setEnabled(selectedCourses.size() > 0);
        });

        middleLayer.addComponent(grid);
        middleLayer.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        middleLayer.setExpandRatio(grid, 0.8f);

        updateGrid();

        //Inittializing buttons
        buttonsLayout = new HorizontalLayout();

        if (!AuthenticationService.getInstance().isAdmin()) {
            unregisterButton = new Button("Unregister from Selected Courses");

            unregisterButton.addClickListener(event -> {

                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

                sureButton.addClickListener(clickEvent -> {
                    boolean registered = courseService.unregister(selectedCourses);
                    showNotification(registered);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(buttonsList));
            });
        }

        unregisterButton.setEnabled(false);
        buttonsLayout.addComponent(unregisterButton);

        //todo am I putting stuff into logic that should be handled by db?
        coursesButton = new Button("View all courses");

        coursesButton.addClickListener(event -> {
            getUI().getCurrent().setContent(new MainWindow());
        });

        buttonsLayout.addComponent(coursesButton);

        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_LEFT);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        //Initializing top menu
        initTop();

    }


    @Override
    public void updateGrid() {
        Student currentStudent = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
        Set<Course> courses = currentStudent.getGroups();
        grid.setItems(courses);
    }
}
