package pl.kognitywistyka.app.course;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @Column(name = "ID", nullable = false)
    private String id; //should be course's USOS code

//    private int localId;

    @Column(name = "NAME", nullable = false)
    private String courseName;

    @OneToMany(targetEntity = CourseGroup.class, mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseGroup> groups;

    @Column(name = "COORDINATOR")
    private String coordinator;

    @Column(name = "FACULTY") // I don't think we need to store this as a relation to a faculty-object, we only need to filter by this value;
    private String faculty;

    @Column(name = "SYLLABUS")
    private String syllabus;

    public Course() {

    }

    public Course(String id, String courseName, String faculty) {
        setId(id);
        setCourseName(courseName);
        setFaculty(faculty);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Set<CourseGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<CourseGroup> groups) {
        this.groups = groups;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @Override
    public String toString() {
        //todo properer string, this one only for filtering testdata
        String course = getId() + "|" + getCourseName() + "|" + getFaculty();
        return course;
    }
}
