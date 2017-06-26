package pl.kognitywistyka.app;

import javax.servlet.annotation.WebServlet;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.ui.LoginWindow;
import pl.kognitywistyka.app.ui.MainWindow;
import pl.kognitywistyka.app.user.Student;

import java.util.List;

/**
 * Built upon an auto-generated template.
 *
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("kogniusos")
public class KogniUSOSUI extends UI {

    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = HibernateUtils.getSessionFactory();
        } catch (Throwable e) {
            System.err.println(e);
            throw new ExceptionInInitializerError(e);
        }
        KogniUSOSUI KU = new KogniUSOSUI();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        if (AuthenticationService.getInstance().isAuthenticated()) {
            setContent(new MainWindow());
        } else {
            setContent(new LoginWindow());
        }
    }

    @WebServlet(urlPatterns = "/*", name = "KogniUSOSServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = KogniUSOSUI.class, productionMode = false)
    public static class KogniUSOSServlet extends VaadinServlet {
    }
}
