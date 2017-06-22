package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
public class MainWindow extends GridWindow<Course> {


    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterLayout;
    private HorizontalLayout superFilterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<Course> grid;

    //Filter
    private TextField filterField;
    private Button clearFilterFieldButton;
    private CheckBox showRegisteredCheckBox;

    //Button
    private Button registerDeleteButton;
    private Button studentsOrMyCoursesButton;

    //variables
    //todo should it be here?
    private Set<Course> selectedCourses;
    private CourseService courseService = CourseService.getInstance();

    public MainWindow() {
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
        superFilterLayout = new HorizontalLayout();

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

        superFilterLayout.addComponent(filterLayout);
        superFilterLayout.setComponentAlignment(filterLayout, Alignment.TOP_LEFT);

        showRegisteredCheckBox = new CheckBox("Show courses I'm registered to");
        showRegisteredCheckBox.setValue(true);
        showRegisteredCheckBox.addValueChangeListener(event -> updateGrid());

        superFilterLayout.addComponent(showRegisteredCheckBox);
        superFilterLayout.setComponentAlignment(showRegisteredCheckBox, Alignment.MIDDLE_LEFT);

        middleLayer.addComponent(superFilterLayout);
        middleLayer.setComponentAlignment(superFilterLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(superFilterLayout, 0.1f);

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
            registerDeleteButton.setEnabled(selectedCourses.size() > 0);
        });

        middleLayer.addComponent(grid);
        middleLayer.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        middleLayer.setExpandRatio(grid, 0.8f);

        updateGrid();

        //Inittializing buttons
        buttonsLayout = new HorizontalLayout();

        if (!AuthenticationService.isAdmin()) {
            registerDeleteButton = new Button("Register to Selected Courses");

            registerDeleteButton.addClickListener(event -> {

                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

                sureButton.addClickListener(clickEvent -> {
                    boolean registered = courseService.register(selectedCourses);
                    showNotification(registered);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(buttonsList));
            });
        } else {
            registerDeleteButton = new Button("Delete Selected Courses");
            registerDeleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

            registerDeleteButton.addClickListener(event -> {

                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_DANGER);

                sureButton.addClickListener(clickEvent -> {
                    boolean deleted = courseService.delete(selectedCourses);
                    updateGrid();
                    showNotification(deleted);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(buttonsList));
            });
        }

        registerDeleteButton.setEnabled(false);
        buttonsLayout.addComponent(registerDeleteButton);

        //todo am I putting stuff into logic that should be handled by db?
        if (AuthenticationService.isAdmin()) {
            studentsOrMyCoursesButton = new Button("View students");

            studentsOrMyCoursesButton.addClickListener(event -> {
                getUI().getCurrent().setContent(new StudentsWindow());
            });
        } else {
            studentsOrMyCoursesButton = new Button("View my courses");

            studentsOrMyCoursesButton.addClickListener(event -> {
                getUI().getCurrent().setContent(new StudentsCoursesWindow());
            });
        }

        buttonsLayout.addComponent(studentsOrMyCoursesButton);

        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_LEFT);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        //todo should I leave it here?
        Button addCoursesButton;


        if (AuthenticationService.isAdmin()) {
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
                    String object = codesField.getValue();
                    window.close();
                    showNotification(submitted);
                    getUI().getCurrent().setContent(new TextWindow(object));
                });
                content.addComponent(submitButton);
                content.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
                window.setContent(content);
                getUI().getUI().addWindow(window);
            });

            buttonsLayout.addComponent(addCoursesButton);
        }

        //Initializing top menu
        initTop();
    }

    public void updateGrid() {
        List<Course> courses = courseService.findAll(filterField.getValue(), showRegisteredCheckBox.getValue());
        grid.setItems(courses);
    }

}
