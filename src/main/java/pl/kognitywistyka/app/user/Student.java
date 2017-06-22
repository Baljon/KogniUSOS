package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.course.CourseGroup;

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

    public Student() {}

    public Student(String albumNo,String firstName, String lastName) {
        setId(albumNo);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public Student(String albumNo, String firstName, String lastName, String email, String password) {
        super();
    }

//    public String getStudentID() {
//        return studentID;
//    }
//
//    public void setStudentID(String albumNumber) {
//        this.studentID = albumNumber;
//    }

    public Set<Course> getGroups() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }

    @Override
    public String toString() {
        return getId() + "|" + getFirstName() + "|" + getLastName();
    }

    @Override
    public boolean equals(User user) {
        return getId().equals(user.getId());
    }

    public boolean equals(Student randomStudent) {
        return this.getId().equals(randomStudent.getId());
    }
}
