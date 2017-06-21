package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.course.CourseGroup;
import pl.kognitywistyka.app.user.User;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @Column(name="ALBUM_NUMBER", nullable = false)
    private String albumNumber;

    @ManyToMany(targetEntity = CourseGroup.class, cascade = CascadeType.ALL)
    @JoinTable(name = "GROUP_PARTICIPANTS", joinColumns = @JoinColumn(name = "PARTICIPANTS"), inverseJoinColumns = @JoinColumn(name="GROUP"))
    private Set<CourseGroup> groups;

    public Student() {

    }

    public Student(String pesel,String firstName, String lastName) {
        setId(pesel);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getAlbumNumber() {
        return albumNumber;
    }

    public void setAlbumNumber(String albumNumber) {
        this.albumNumber = albumNumber;
    }

    public Set<CourseGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<CourseGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return getId() + "|" + getFirstName() + "|" + getLastName();
    }

    public boolean equals(User user) {
//        return getId().equals(user.getId());
        return true;
    }
}
