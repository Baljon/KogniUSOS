package pl.kognitywistyka.app.course;

import pl.kognitywistyka.app.user.Student;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "ID", nullable = false)
    private String id; //should be course's USOS code

    @Column(name = "NAME", nullable = false)
    private String courseName;

    @Column(name = "FACULTY")
    private String faculty;

    @Column(name = "SYLLABUS")
    private String syllabus;

    @Column(name = "ACCEPTED")
    private boolean accepted;

    @Column(name = "BLACKLISTED")
    private boolean blacklisted;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

    public Course() {}

    public Course(String id, String courseName, String faculty, String syllabus) {
        setId(id);
        setCourseName(courseName);
        setFaculty(faculty);
        setSyllabus(syllabus);
    }

    public Course(String id, String courseName, String faculty, String syllabus, boolean accepted, boolean blacklisted) {
        this(id, courseName, faculty, syllabus);
        this.accepted = accepted;
        this.blacklisted = blacklisted;
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

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> participants) {
        this.students = participants;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void acceptCourse() {
        this.accepted = true;
    }

    public void rejectCourse() {
        this.accepted = false;
        this.blacklist();
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void blacklist() {
        this.blacklisted = true;
    }

    public void unblacklist() {
        this.blacklisted = false;
    }

    @Override
    public String toString() {
        String courseAsString = getId() + " " + getCourseName();
        return courseAsString;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void addStudent(Student user) {
        this.students.add(user);
    }
}
