package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.User;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 21.06.2017.
 */
public class StudentsWindow extends GridWindow<User> {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout filterNameLayout;
    private CssLayout filterIdLayout;
    private HorizontalLayout superFilterLayout;
    private HorizontalLayout buttonsLayout;

    //Grid
    private Grid<User> grid;

    //Filter
    private TextField filterNameField;
    private Button clearFilterNameFieldButton;
    private TextField filterIdField;
    private Button clearFilterIdFieldButton;

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
//        middleLayer.addStyleName("login-panel");

        init(middleLayer);

        middleLayer.setWidth("750px");
        middleLayer.setHeight("600px");

        //Initializing filtering
        superFilterLayout = new HorizontalLayout();

        filterNameLayout = new CssLayout();
        filterNameLayout.setSizeFull();
        filterIdLayout = new CssLayout();
        filterIdLayout.setSizeFull();

        filterNameField = new TextField();
        filterIdField = new TextField();

        filterNameField.setPlaceholder("filter by name...");
        filterNameField.addValueChangeListener(e -> updateGrid());
        filterNameField.addValueChangeListener(e -> filterIdField.clear());
        filterNameField.setValueChangeMode(ValueChangeMode.LAZY);
        filterIdField.setPlaceholder("filter by id...");
        filterIdField.addValueChangeListener(e -> updateGridById());
        filterIdField.addValueChangeListener(e -> filterNameField.clear());
        filterIdField.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterNameFieldButton = new Button(VaadinIcons.CLOSE_SMALL);
        clearFilterIdFieldButton = new Button(VaadinIcons.CLOSE_SMALL);

        clearFilterNameFieldButton.setDescription("Clear the current filter");
        clearFilterNameFieldButton.addClickListener(e -> filterNameField.clear());
        clearFilterIdFieldButton.setDescription("Clear the current filter");
        clearFilterIdFieldButton.addClickListener(e -> filterNameField.clear());

        filterNameLayout.addComponents(filterNameField, clearFilterNameFieldButton);
        filterNameLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filterIdLayout.addComponents(filterIdField, clearFilterIdFieldButton);
        filterIdLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        superFilterLayout.addComponents(filterNameLayout, filterIdLayout);

        middleLayer.addComponent(superFilterLayout);
        middleLayer.setComponentAlignment(superFilterLayout, Alignment.TOP_CENTER);
        middleLayer.setExpandRatio(superFilterLayout, 0.1f);

        //Initializing grid
        grid = new Grid<>(User.class);
        grid.setSizeFull();

        grid.addColumn(User::getId).setCaption("Album number");
        grid.addColumn(User::getFirstName).setCaption("First Name");
        grid.addColumn(User::getLastName).setCaption("Last Name");
        grid.setColumns("id", "firstName", "lastName");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addColumn(event -> "View",
                new ButtonRenderer<>(clickEvent -> {
                    getUI().getCurrent().setContent(new StudentWindow(clickEvent.getItem(), getUI().getCurrent().getContent()));
                }));

        grid.addColumn(event -> "Delete",
                new ButtonRenderer<>(clickEvent -> {

                    //Initializing buttons
                    Button cancelButton = new Button("Cancel");
                    cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                    Button sureButton = new Button("I'm sure");
                    sureButton.setStyleName(ValoTheme.BUTTON_DANGER);
                    buttonsLayout.addComponents(cancelButton, sureButton);

                    sureButton.addClickListener(sureEvent -> {
                        StudentService studentService = StudentService.getInstance();
                        boolean deleted = studentService.delete(clickEvent.getItem());
                        showNotification(deleted);
                        updateGrid();
                    });

                    ArrayList<Button> buttonsList = new ArrayList<>();
                    buttonsList.add(cancelButton);
                    buttonsList.add(sureButton);

                    getUI().getUI().addWindow(showWarning(buttonsList));
                }));

        grid.addSelectionListener(event -> {
            selectedStudents = event.getAllSelectedItems();
            deleteButton.setEnabled(selectedStudents.size() > 0);
        });

        middleLayer.addComponent(grid);
        middleLayer.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        middleLayer.setExpandRatio(grid, 0.8f);

        updateGrid();

        //Initializing buttons
        buttonsLayout = new HorizontalLayout();

        deleteButton = new Button("Delete selected accounts");
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

        deleteButton.addClickListener(event -> {

            //Initializing buttons
            Button cancelButton = new Button("Cancel");
            cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
            Button sureButton = new Button("I'm sure");
            sureButton.setStyleName(ValoTheme.BUTTON_DANGER);
            buttonsLayout.addComponents(cancelButton, sureButton);

            sureButton.addClickListener(sureEvent -> {
                StudentService studentService = StudentService.getInstance();
                boolean deleted = studentService.delete(selectedStudents);
                showNotification(deleted);
                updateGrid();
            });

            ArrayList<Button> buttonsList = new ArrayList<>();
            buttonsList.add(cancelButton);
            buttonsList.add(sureButton);

            getUI().getUI().addWindow(showWarning(buttonsList));
        });

        deleteButton.setEnabled(false);

        buttonsLayout.addComponent(deleteButton);

        middleLayer.addComponent(buttonsLayout);
        middleLayer.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        middleLayer.setExpandRatio(buttonsLayout, 0.1f);

        coursesButton = new Button("View courses");

        coursesButton.addClickListener(event -> {
            getUI().getCurrent().setContent(new MainWindow());
        });

        buttonsLayout.addComponent(coursesButton);

        //Initializing top menu
        initTop();
    }

    private void updateGridById() {
        StudentService studentService = StudentService.getInstance();
        List<User> studentsList = studentService.findById(filterIdField.getValue());
        if(studentsList == null) studentsList = new ArrayList<>();
        grid.setItems(studentsList);
    }

    public void updateGrid() {
        StudentService studentService = StudentService.getInstance();
        List<User> studentsList = studentService.findByName(filterNameField.getValue());
        if(studentsList == null) studentsList = new ArrayList<>();
        grid.setItems(studentsList);
    }

}
