package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.course.Course;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@DiscriminatorValue("student")
public class Student extends User {
    //Students are identified (studentID) by their album number. Fake IDs can be injected by admins if necessary.

    @Column(name = "EMAIL")
    private String email;

    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL)
    @JoinTable(name = "STUDENT_COURSE",
    joinColumns = {@JoinColumn(name = "STUDENT_ID")},
    inverseJoinColumns = {@JoinColumn(name = "COURSE_ID")})
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
        return getFirstName() + " " + getLastName() + ", nr albumu " + getId();
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

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
