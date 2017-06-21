package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;

import javax.jnlp.PersistenceService;
import javax.xml.soap.Text;
import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
public class MainWindow extends CenteredWindow {


    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<Course> courseGrid;

    //Filter
    private TextField filterField;
    private Button clearFilterFieldButton;

    //Button
    private Button registerButton;
    private Button studentsButton;

    //variables
    //todo should it be here?
    private Set<Course> selectedCourses;
    private CourseService courseService = CourseService.getInstance();



    //TODO
    public MainWindow() {
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
        courseGrid = new Grid<>(Course.class);

        courseGrid.addColumn(Course::getId).setCaption("USOS code");
        courseGrid.addColumn(Course::getCourseName).setCaption("Course Name");
        courseGrid.addColumn(Course::getFaculty).setCaption("Faculty");
        courseGrid.setColumns("id", "courseName", "faculty");

        courseGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        courseGrid.addColumn(event -> "View course",
                new ButtonRenderer<>(clickEvent ->
                        getUI().getCurrent().setContent(new CourseWindow(clickEvent.getItem(), getUI().getCurrent().getContent()))));

        courseGrid.addSelectionListener(event -> {
            selectedCourses = event.getAllSelectedItems();
            registerButton.setEnabled(selectedCourses.size()>0);
        });

        middleLayer.addComponent(courseGrid);
        middleLayer.setComponentAlignment(courseGrid, Alignment.MIDDLE_LEFT);
        middleLayer.setExpandRatio(courseGrid, 0.8f);

        updateGrid();

        //Inittializing buttons
        buttonsLayout = new HorizontalLayout();

        registerButton = new Button("Register to Selected Courses");

        registerButton.addClickListener(event -> {
            boolean registered = courseService.register(selectedCourses);
            showNotification(registered);
        });

        registerButton.setEnabled(false);

        buttonsLayout.addComponent(registerButton);

        //todo am I putting stuff into logic that should be handled by db?
        if(AuthenticationService.isAdmin()) {
            studentsButton = new Button("View students");

            studentsButton.addClickListener(event -> {
                getUI().getCurrent().setContent(new StudentsWindow());
            });

            buttonsLayout.addComponent(studentsButton);
        }

        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_LEFT);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        //Initializing top menu
        initTop();
    }

    public void updateGrid() {
        List<Course> courses = courseService.findAll(filterField.getValue());
        courseGrid.setItems(courses);
    }

}
