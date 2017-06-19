package pl.kognitywistyka.app.course;

import pl.kognitywistyka.app.user.Student;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@Table(name = "COURSES_GROUPS") // shouldn't we keep it as a part of COURSES table?
public class CourseGroup {

    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    @ManyToOne(targetEntity = Course.class, cascade = {CascadeType.ALL})
    private Course course;

    @Column(name = "LECTURER")
    private String lecturer;

    @Column(name = "DATE")
    private String date;

    @ManyToMany(targetEntity = Student.class, cascade = CascadeType.ALL)
    @JoinTable(name = "GROUP_PARTICIPANTS", joinColumns = @JoinColumn(name="GROUP"), inverseJoinColumns = @JoinColumn(name = "PARTICIPANTS"))
    private Set<Student> students;

    public CourseGroup() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
