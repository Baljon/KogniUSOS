package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 21.06.2017.
 */
public class StudentsWindow extends GridWindow<User> {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<Student> grid;

    //Filter
    private TextField filterField;
    private Button clearFilterFieldButton;

    //Button
    private Button deleteButton;
    private Button coursesButton;

    //variables
    //todo should it be here?
    private Set<Student> selectedStudents;

    public StudentsWindow() {
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

        filterField.setPlaceholder("filter by...");
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
        grid = new Grid<>(Student.class);
        grid.setSizeFull();

        grid.addColumn(Student::getId).setCaption("PESEL");
        grid.addColumn(Student::getFirstName).setCaption("First Name");
        grid.addColumn(Student::getLastName).setCaption("Last Name");
        grid.setColumns("id", "firstName", "lastName");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addColumn(event -> "View",
                new ButtonRenderer<>(clickEvent -> {
                    getUI().getCurrent().setContent(new StudentWindow(clickEvent.getItem(), getUI().getCurrent().getContent()));
                }));

        grid.addColumn(event -> "Delete",
                new ButtonRenderer<>(clickEvent -> {
                    StudentService studentService = StudentService.getInstance();
                    boolean deleted = studentService.delete(clickEvent.getItem());
                    showNotification(deleted);
                    updateGrid();
                }));

        grid.addSelectionListener(event -> {
            selectedStudents = event.getAllSelectedItems();
            deleteButton.setEnabled(selectedStudents.size() > 0);
        });

        middleLayer.addComponent(grid);
        middleLayer.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        middleLayer.setExpandRatio(grid, 0.8f);

        updateGrid();

        //Initializing buttons
        buttonsLayout = new HorizontalLayout();

        deleteButton = new Button("Delete selected accounts");

        deleteButton.addClickListener(event -> {
            StudentService studentService = StudentService.getInstance();
            boolean deleted = studentService.delete(selectedStudents);
            showNotification(deleted);
            updateGrid();
        });

        deleteButton.setEnabled(false);

        buttonsLayout.addComponent(deleteButton);

        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_LEFT);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        coursesButton = new Button("View courses");

        coursesButton.addClickListener(event -> {
            getUI().getCurrent().setContent(new MainWindow());
        });

        buttonsLayout.addComponent(coursesButton);

        //Initializing top menu
        initTop();
    }

    public void updateGrid() {
        StudentService studentService = StudentService.getInstance();
        List<Student> studentsList= studentService.findAll(filterField.getValue());
        grid.setItems(studentsList);
    }

}
