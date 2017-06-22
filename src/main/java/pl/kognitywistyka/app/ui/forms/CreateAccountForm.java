package pl.kognitywistyka.app.ui.forms;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.KogniUSOSUI;
import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.ui.LoginWindow;
import pl.kognitywistyka.app.user.Student;

import java.util.Objects;

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

    //Button
    private Button addAccountButton;

    //Variables
    private Student student;

    public CreateAccountForm(Student student) {

        Validator<String> valid = new Validator<String>() {
            @Override
            public ValidationResult apply(String s, ValueContext valueContext) {
                if(s.equals(password.getValue())) return ValidationResult.ok();
                else return ValidationResult.error("Incorrect password.");
            }
        };

        setStudent(student);

        setSizeUndefined();

//        studentBinder.bindInstanceFields(this);

        studentBinder.forField(id).withValidator(id -> id.length() == 6,
                "Incorrect album number!").bind(Student::getId, Student::setId);
        id.setRequiredIndicatorVisible(true);
        id.setIcon(VaadinIcons.USER);
        studentBinder.forField(email).withValidator(new EmailValidator("Email address incorrect!"))
                .bind(Student::getEmail, Student::setEmail);
        email.setRequiredIndicatorVisible(true);
        email.setIcon(VaadinIcons.ENVELOPE_OPEN);
        studentBinder.forField(firstName).withValidator(name -> name != null && !name.equals(""), "Field required!")
                .bind(Student::getFirstName, Student::setFirstName);
        firstName.setRequiredIndicatorVisible(true);
        firstName.setIcon(VaadinIcons.USER_CARD);
        studentBinder.forField(lastName).withValidator(name -> name != null && !name.equals(""), "Field required!")
                .bind(Student::getLastName, Student::setLastName);
        lastName.setRequiredIndicatorVisible(true);
        lastName.setIcon(VaadinIcons.USER_CARD);
        studentBinder.forField(password).withValidator(pass -> pass.length()> 4,
                "Password should be at least 4 characters long!").bind(Student::getPassword, Student::setPassword);
        password.setRequiredIndicatorVisible(true);
        password.setIcon(VaadinIcons.PASSWORD);
        studentBinder.forField(confirmPassword).withValidator(valid);
        confirmPassword.setRequiredIndicatorVisible(true);
        confirmPassword.setIcon(VaadinIcons.PASSWORD);

        addComponents(id, email, firstName, lastName, password, confirmPassword);

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

    public Button getAddAccountButton() {
        return addAccountButton;
    }

    public void setAddAccountButton(Button addAccountButton) {
        this.addAccountButton = addAccountButton;
    }

    public Binder<Student> getStudentBinder() {
        return studentBinder;
    }
}
