package pl.kognitywistyka.app.ui;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.User;

import java.util.List;

/**
 * Created by wikto on 19.06.2017.
 */
public abstract class CenteredWindow extends VerticalLayout {

    //private static final long serialVersionUID = 1L;

    //Layouts
    private VerticalLayout topSpacer;
    private VerticalLayout bottomSpacer;
    private HorizontalLayout topMenu;

    private Component middleLayer;

    protected void init(Component middleLayer) {
        setSizeFull();

        //Initializing layouts
        this.setMiddleLayer(middleLayer);
        middleLayer.setSizeFull();
//        middleLayer.addStyleName("login-panel");

        topMenu = new HorizontalLayout();
//        topMenu.addStyleName("kogniusos-top");
        topMenu.setHeight("15px");
        topMenu.setWidth("60%");

        topSpacer = new VerticalLayout();
        bottomSpacer = new VerticalLayout();

        topSpacer.setSizeFull();
        bottomSpacer.setSizeFull();

        addComponent(topSpacer);
        addComponent(topMenu);
        setComponentAlignment(topMenu, Alignment.MIDDLE_CENTER);
        addComponent(middleLayer);
        addComponent(bottomSpacer);

        setExpandRatio(topSpacer, 0.015f);
        setExpandRatio(middleLayer, 0.94f);
        setExpandRatio(bottomSpacer, 0.05f);

        setComponentAlignment(middleLayer, Alignment.MIDDLE_CENTER);

//        initTop();
    }

    protected void initTop() {
        topMenu.removeAllComponents();
        User loginInfo = AuthenticationService.getCurrentLoginInfo();

        if (loginInfo != null) {
            String text = "You're logged in as: ";// <b>" + loginInfo.getFirstName() + " " + loginInfo.getLastName() + "</b>";
            Label loginLabel = new Label(text, ContentMode.HTML);

            topMenu.addComponent(loginLabel);
            topMenu.setComponentAlignment(loginLabel, Alignment.MIDDLE_LEFT);
            topMenu.setExpandRatio(loginLabel, 0.18f);

            Button userNameButton = new Button(loginInfo.getFirstName() + " " + loginInfo.getLastName());

            userNameButton.addClickListener(event -> {
                UI.getCurrent().setContent(new StudentWindow(loginInfo, getUI().getCurrent().getContent()));
            });

            topMenu.addComponent(userNameButton);
            topMenu.setComponentAlignment(userNameButton, Alignment.TOP_LEFT);
            topMenu.setExpandRatio(userNameButton, 0.8f);

            Button logoutButton = new Button("Log out");

            logoutButton.addClickListener(event -> {
                boolean signedOut = AuthenticationService.logout();
                UI.getCurrent().setContent(new LoginWindow());
                showNotification(signedOut);
            });

            topMenu.addComponent(logoutButton);
            topMenu.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
        } else {
            String text = "You're not logged in!";
            Label loginLabel = new Label(text, ContentMode.HTML);

            topMenu.addComponent(loginLabel);
            topMenu.setComponentAlignment(loginLabel, Alignment.TOP_LEFT);

            Button loginButton = new Button("Sign in");

            loginButton.addClickListener(event -> {
                UI.getCurrent().setContent(new LoginWindow());
            });

            topMenu.addComponent(loginButton);
            topMenu.setComponentAlignment(loginButton, Alignment.TOP_RIGHT);
        }
    }

    public void showNotification(boolean value) {
        if (value) {
            Notification notification = new Notification("Ok, it's done!");
            notification.show(Page.getCurrent());
        } else {
            Notification notification = new Notification("Something went wrong!",
                    "If problem persists, contact administrator.", Notification.Type.ERROR_MESSAGE);
            notification.show(Page.getCurrent());
        }
    }

    public Window showWarning(List<Button> buttonList) {
        //Initializing warning window
        Window window = new Window();
        window.setCaption("Warning!");
        window.setWidth("300px");
        window.setModal(true);
        window.setClosable(false);
        window.setDraggable(false);
        window.setResizable(false);
        window.setStyleName(ValoTheme.NOTIFICATION_WARNING);

        //Initializing layout
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        HorizontalLayout buttonsLayout = new HorizontalLayout();

        //Initializing label
        Label label = new Label("Are you sure? <br> This change cannot be undone!", ContentMode.HTML);
        label.setStyleName(ValoTheme.LABEL_FAILURE);
        content.addComponent(label);

        for(Button button : buttonList) {
            button.addClickListener(event -> window.close());
            buttonsLayout.addComponent(button);
        }

        content.addComponent(buttonsLayout);
        content.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        window.setContent(content);
        return window;
    }

    public Component getMiddleLayer() {
        return middleLayer;
    }

    public void setMiddleLayer(Component middleLayer) {
        this.middleLayer = middleLayer;
    }

}
