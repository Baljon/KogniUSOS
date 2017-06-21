package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.User;

import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 21.06.2017.
 */
public class StudentsWindow extends CenteredWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<User> usersGrid;

    //Filter
    private TextField filterField;
    private Button clearFilterFieldButton;

    //Button
    private Button deleteButton;
    private Button coursesButton;

    //variables
    //todo should it be here?
    private Set<User> selectedStudents;

    public StudentsWindow() {
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
        usersGrid = new Grid<>(User.class);
        usersGrid.setSizeFull();

        usersGrid.addColumn(User::getId).setCaption("PESEL");
        usersGrid.addColumn(User::getFirstName).setCaption("First Name");
        usersGrid.addColumn(User::getLastName).setCaption("Last Name");
        usersGrid.setColumns("id", "firstName", "lastName");

        usersGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        usersGrid.addColumn(event -> "View account details",
                new ButtonRenderer<>(clickEvent -> {
                    getUI().getCurrent().setContent(new UserWindow(clickEvent.getItem(), getUI().getCurrent().getContent()));
                }));

        usersGrid.addColumn(event -> "Delete account",
                new ButtonRenderer<>(clickEvent -> {
                    StudentService studentService = StudentService.getInstance();
                    boolean deleted = studentService.delete(clickEvent.getItem());
                    showNotification(deleted);
                    updateGrid();
                }));

        usersGrid.addSelectionListener(event -> {
            selectedStudents = event.getAllSelectedItems();
            deleteButton.setEnabled(selectedStudents.size() > 0);
        });

        middleLayer.addComponent(usersGrid);
        middleLayer.setComponentAlignment(usersGrid, Alignment.MIDDLE_LEFT);
        middleLayer.setExpandRatio(usersGrid, 0.8f);

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
        List<User> usersList = studentService.findAll(filterField.getValue());
        usersGrid.setItems(usersList);
    }

}
