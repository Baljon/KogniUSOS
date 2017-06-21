package pl.kognitywistyka.app.service;

import pl.kognitywistyka.app.course.Course;
import java.util.*;
import java.util.Comparator;

/**
 * Created by wikto on 20.06.2017.
 */
public class CourseService {

    private static CourseService instance;
    private HashMap<String, Course> courses;

    public static CourseService getInstance() {
        if (instance == null){
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
        for(Course course : coursesList) {
            String id = course.getId();
            courses.put(id, course);
//            id = id + 1;
        }
    }

    //todo
    public synchronized List<Course> findAll() {
        return new ArrayList<>();
    }

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

    public static boolean register(Set<Course> selectedCourses) {
        return false;
    }

    public static boolean register(Course course) {
        return false;
    }

    public static boolean addCourses(String value) {
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
        for(Course course : selectedCourses) {
            courses.remove(course.getId());
        }
        return true;
    }
}
