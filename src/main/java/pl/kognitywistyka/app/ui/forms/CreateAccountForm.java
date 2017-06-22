package pl.kognitywistyka.app.ui.forms;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.KogniUSOSUI;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.ui.LoginWindow;
import pl.kognitywistyka.app.user.Student;

/**
 * Created by wikto on 22.06.2017.
 */
public class CreateAccountForm extends FormLayout {

    //Fields
    private TextField id = new TextField("Album number: ");
    private TextField email = new TextField("e-mail: ");
    private TextField firstName = new TextField("First name: ");
    private TextField lastName = new TextField("Last name: ");
    private PasswordField password = new PasswordField("Password: ");
    private PasswordField confirmPassword = new PasswordField("Confirm password: ");

    //Binder
    private Binder<Student> studentBinder = new Binder<>(Student.class);

    //Variables
    private Student student;

    public CreateAccountForm(Student student) {
        setStudent(student);

        setSizeUndefined();

        addComponents(id, email, firstName, lastName, password, confirmPassword);

        studentBinder.bindInstanceFields(this);

        setVisible(true);
        id.selectAll();
    }

    public void setStudent(Student student) {
        this.student = student;
        studentBinder.setBean(student);
    }

    public Student getStudent() {
        return student;
    }
}
