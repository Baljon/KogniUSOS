package pl.kognitywistyka.app.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.Student;

import java.util.*;

/**
 * Created by wikto on 20.06.2017.
 */
public class CourseService {

    private static CourseService instance;
    private HashMap<String, Course> courses;

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
            instance.ensureTestData();
        }
        return instance;
    }

    public void ensureTestData() {
        courses = new HashMap<>();
        List<Course> coursesList = Arrays.asList(
                new Course("1", "kurs1", "Filozofia"),
                new Course("2", "kurs2", "Matematyka")
        );
//        Integer id = 0;
        for (Course course : coursesList) {
            String id = course.getId();
            courses.put(id, course);
//            id = id + 1;
        }
    }

    public synchronized List<Course> findById(String id) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Course where lower(id) like: id order by id, courseName")
                    .setParameter("id", id.toLowerCase());
            Course course = (Course) query.getSingleResult();
            resultList.add(course);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            session.close();
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return resultList;
    }

    public synchronized List<Course> findByName(String name) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Course where lower(courseName) like: name order by courseName, id")
                    .setParameter("name", "%" + name.toLowerCase() + "%");
            Course course = (Course) query.getSingleResult();
            resultList.add(course);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            session.close();
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return resultList;
    }

    /***
     * Returns all courses.
     * @return ArrayList<Course>
     */
    public synchronized List<Course> findAll() {
        ArrayList<Course> arrayList = new ArrayList<>();
        for (Course course : courses.values()) {
            arrayList.add(course);
        }
        return arrayList;
    }

    /***
     * Returns all courses whose String representation contains given String.
     * @param value filtering value
     * @return ArrayList<Course>
     */
    public synchronized List<Course> findAll(String value) {
        ArrayList<Course> arrayList = new ArrayList<>();
        for (Course course : courses.values()) {
            boolean passesFilter = (value == null || value.isEmpty())
                    || course.toString().toLowerCase().contains(value.toLowerCase());
            if (passesFilter) {
                arrayList.add(course);
            }
        }
//        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
        return arrayList;
    }

    /***
     * Returns all courses whose String representation contains given String and that current user is registered to.
     * @param value String, filtering value
     * @param value1 boolean, indicates whether the fact that user is registered to should be taken into account
     * @return ArrayList<Course>
     */
    public synchronized List<Course> findAll(String value, Boolean value1) {
        Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
        ArrayList<Course> arrayList = new ArrayList<>();
        for (Course course : courses.values()) {
            boolean passesFilter = (value == null || value.isEmpty())
                    || (course.toString().toLowerCase().contains(value.toLowerCase()) && (value1 || student.isRegisteredTo(course)));
            if (passesFilter) {
                arrayList.add(course);
            }
        }
//        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
        return arrayList;
    }

    public static boolean register(Set<Course> selectedCourses) {
        return false;
    }

    public static boolean register(Course course) {
        return false;
    }

    public static boolean addCourses(String value) {
        //smart - checks if String contains commas
        return false;
    }

    public static boolean proposeCourses(String value) {
        return false;
    }

    public boolean delete(Course course) {
        courses.remove(course.getId());
        return true;
    }

    public boolean delete(Set<Course> selectedCourses) {
        for (Course course : selectedCourses) {
            courses.remove(course.getId());
        }
        return true;
    }

    public boolean unregister(Course course) {
        return false;
    }

    public boolean unregister(Set<Course> selectedCourses) {
        return false;
    }

    public boolean exportStudents(Set<Course> selectedCourses) {
        return false;
    }
}
