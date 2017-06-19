package pl.kognitywistyka.app.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.User;

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
        middleLayer.addStyleName("login-panel");

        topMenu = new HorizontalLayout();
        topMenu.addStyleName("kogniusos-top");
        topMenu.setHeight("25px");
        topMenu.setWidth("100%");

        topSpacer = new VerticalLayout();
        bottomSpacer = new VerticalLayout();

        topSpacer.setSizeFull();
        bottomSpacer.setSizeFull();

        addComponent(topSpacer);
        addComponent(topMenu);
        addComponent(middleLayer);
        addComponent(bottomSpacer);

        setExpandRatio(topSpacer, 0.1f);
        setExpandRatio(middleLayer, 1.5f);
        setExpandRatio(bottomSpacer, 0.25f);

        setComponentAlignment(middleLayer, Alignment.MIDDLE_CENTER);

//        initTop();
    }

    protected void initTop() {
        topMenu.removeAllComponents();
        User loginInfo = AuthenticationService.getCurrentLoginInfo();

        if(loginInfo != null) {
            String text = "You're logged in as: ";// <b>" + loginInfo.getFirstName() + " " + loginInfo.getLastName() + "</b>";
            Label loginLabel = new Label(text, ContentMode.HTML);

            topMenu.addComponent(loginLabel);
            topMenu.setComponentAlignment(loginLabel, Alignment.TOP_LEFT);

            Button userNameButton = new Button(loginInfo.getFirstName() + loginInfo.getLastName());

            userNameButton.addClickListener(event -> {
               UI.getCurrent().setContent(new UserWindow(loginInfo));
            });

            topMenu.addComponent(userNameButton);
            topMenu.setComponentAlignment(userNameButton, Alignment.TOP_LEFT);

            Button logoutButton = new Button("Log out");

            logoutButton.addClickListener(event -> {
                AuthenticationService.logout();
                UI.getCurrent().setContent(new LoginWindow());
            });

            topMenu.addComponent(logoutButton);
            topMenu.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
        }
        else {
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

    public Component getMiddleLayer() {
        return middleLayer;
    }

    public void setMiddleLayer(Component middleLayer) {
        this.middleLayer = middleLayer;
    }

}
