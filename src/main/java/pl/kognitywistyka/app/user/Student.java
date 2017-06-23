package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.course.Course;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    //Students are identified (studentID) by their album number. Fake IDs can be injected by admins if necessary.

//    @Id
//    @Column(name="STUDENT_ID", nullable = false)
//    private String studentID;

//    @Column(name = "STUDENT_FIRSTNAME", nullable = false)
//    private String firstName;
//
//    @Column(name = "STUDENT_LASTNAME", nullable = false)
//    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String albumNo, String firstName, String lastName) {
        setId(albumNo);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public Student(String albumNo, String firstName, String lastName, String email, String password) {
        super();
    }

    public Set<Course> getGroups() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return getId() + "|" + getFirstName() + "|" + getLastName();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRegisteredTo(Course course) {
//        return false;
        return courses.contains(course);
//        return true;
    }
}
