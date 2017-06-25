package pl.kognitywistyka.app.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.reporting.ReportingUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.Student;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wikto on 20.06.2017.
 */
public class CourseService {


    private static CourseService instance;
//    private HashMap<String, Course> courses;

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
//            instance.ensureTestData();
        }
        return instance;
    }

//    public void ensureTestData() {
//        courses = new HashMap<>();
//        List<Course> coursesList = Arrays.asList(
//                new Course("1", "kurs1", "Filozofia"),
//                new Course("2", "kurs2", "Matematyka")
//        );
////        Integer id = 0;
//        for (Course course : coursesList) {
//            String id = course.getId();
//            courses.put(id, course);
////            id = id + 1;
//        }
//    }

    public synchronized List<Course> findById(String id) throws NoResultException {
        if (id.isEmpty()) return findAllAcceptedBlacklisted();
        else {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Query query = session
                        .createQuery("from Course " +
                                "where lower(id) like :id " +
                                "and accepted = true " +
                                "order by id, courseName")
                        .setParameter("id", "%" + id.toLowerCase() + "%");
                resultList = query.getResultList();
                finalList = new ArrayList<>();
                for (Object object : resultList) {
                    Course course = (Course) object;
                    finalList.add(course);
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
            return finalList;
        }
    }

    public synchronized List<Course> findById(String id, boolean showRegistered) throws NoResultException {
        if (!showRegistered) {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List<Object[]> resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
                Query query = session.createQuery(
                        "from Course course " +
                                "join course.students students " +
                                "where :student not in students " +
                                "and course.id like :id")
                        .setParameter("id", "%" + id.toLowerCase() + "%")
                        .setParameter("student", student);
                resultList = query.getResultList();
                finalList = new ArrayList<>();
                for (Object[] object : resultList) {
                    Course course = (Course) object[0];
                    finalList.add(course);
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
            return finalList;
        } else return findById(id);
    }

    public synchronized List<Course> findByName(String name) throws NoResultException {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List resultList = null;
        List<Course> finalList = null;
        try {
            tx = session.beginTransaction();
            Query query = session
                    .createQuery("from Course " +
                            "where lower(courseName) like :name " +
                            "and accepted = true " +
                            "order by courseName, id")
                    .setParameter("name", "%" + name.toLowerCase() + "%");
            resultList = query.getResultList();
            finalList = new ArrayList<>();
            for (Object object : resultList) {
                Course course = (Course) object;
                finalList.add(course);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return finalList;
    }

    public List<Course> findByName(String name, boolean showRegistered) throws NoResultException {
        if (!showRegistered) {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
                //todo
                Query query = session.createQuery(
                        "from Course as course " +
                                "join course.students as students " +
                                "where lower(course.courseName) like :name " +
                                "and :student not in " +
                                "elements(students.id) " +
                                "and course.accepted = true")
                        .setParameter("name", "%" + name.toLowerCase() + "%").setParameter("student", student.getId());
                resultList = query.getResultList();
                finalList = new ArrayList<>();
                for (Object object : resultList) {
                    Course course = (Course) object;
                    finalList.add(course);
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
            return finalList;
        } else return findByName(name);
    }

    public synchronized List<Course> findAllAcceptedBlacklisted(boolean accepted, boolean blacklisted) throws NoResultException {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List resultList = null;
        List<Course> finalList = null;
        try {
            tx = session.beginTransaction();
            Query query = session
                    .createQuery("from Course " +
                            "where accepted = :accepted " +
                            "and blacklisted = :blacklisted " +
                            "order by courseName, id")
                    .setParameter("accepted", accepted).setParameter("blacklisted", blacklisted);
            resultList = query.getResultList();
            finalList = new ArrayList<>();
            for (Object object : resultList) {
                finalList.add((Course) object);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return finalList;
    }

    public synchronized List<Course> findAllAcceptedBlacklisted() throws NoResultException {
        return findAllAcceptedBlacklisted(true, false);
    }


    public List<Course> findByNameAcceptedBlacklisted(String name, boolean accepted, boolean blacklisted) throws NoResultException {
        if (name.isEmpty()) return findAllAcceptedBlacklisted(accepted, blacklisted);
        else {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery(
                        "from Course as course " +
                                "where lower(course.courseName) like :name " +
                                "and accepted = :accepted " +
                                "and blacklisted = :blacklisted")
                        .setParameter("name", "%" + name.toLowerCase() + "%").setParameter("accepted", accepted)
                        .setParameter("blacklisted", blacklisted);
                resultList = query.getResultList();
                finalList = new ArrayList<>();
                for (Object object : resultList) {
                    Course course = (Course) object;
                    finalList.add(course);
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
            return finalList;
        }
    }

    public List<Course> findByIdAcceptedBlacklisted(String id, boolean accepted, boolean blacklisted) throws NoResultException {
        if (id.isEmpty()) return findAllAcceptedBlacklisted(accepted, blacklisted);
        else {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery(
                        "from Course as course " +
                                "where lower(course.id) like :id " +
                                "and accepted = :accepted " +
                                "and blacklisted = :blacklisted")
                        .setParameter("id", "%" + id.toLowerCase() + "%").setParameter("accepted", accepted)
                        .setParameter("blacklisted", blacklisted);
                resultList = query.getResultList();
                finalList = new ArrayList<>();
                for (Object object : resultList) {
                    Course course = (Course) object;
                    finalList.add(course);
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
            return finalList;
        }
    }

    public synchronized List<Course> findAllFinal() throws NoResultException {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Course");
            Course course = (Course) query.getSingleResult();
            resultList.add(course);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    public boolean register(Set<Course> selectedCourses) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Student user = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
            int count = 0;
            for (Course course : selectedCourses) {
                course.getStudents().add(user);
                user.getGroups().add(course);
                session.update(course);
                session.update(user);
                count++;
                if (count > 20) {
                    session.flush();
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean register(Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Student user = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
            user.addCourse(course);
//            course.addStudent(user);
            session.update(user);
//            session.update(course);
//                    .update(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    @Inject
    @ConfigProperty(name = "usosuw.service.course")
    private String serviceCourse;

    @Inject
    @ConfigProperty(name = "usosuw.service.fac")
    private String serviceFaculty;

    @Inject
    @ConfigProperty(name = "usosuw.service.course.fields")
    private String serviceCourseFields;

    @Inject
    @ConfigProperty(name = "usosuw.service.fac.fields")
    private String serviceFacultyFields;



    public Course courseFromJSON(String courseID, boolean acceptFlag) throws IOException {

        String id;
        String plName;
        String plDescription;
        String facultyName;
        String facultyID;
        URL aaa = new URL(serviceCourse + courseID + serviceCourseFields);

        HttpURLConnection request = (HttpURLConnection) aaa.openConnection();
        request.connect();
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        id = rootobj.get("id").getAsString();
        JsonElement name = rootobj.get("name");
        JsonObject nameObject = name.getAsJsonObject();
        plName = nameObject.get("pl").getAsString();
        facultyID =rootobj.get("fac_id").getAsString();
        JsonElement description = rootobj.get("description");
        JsonObject descriptionObject = description.getAsJsonObject();
        plDescription = descriptionObject.get("pl").getAsString();

        URL bbb = new URL(serviceFaculty + facultyID + serviceFacultyFields);

        HttpURLConnection request3 = (HttpURLConnection) bbb.openConnection();
        request.connect();
        JsonParser jp3 = new JsonParser();
        JsonElement root3 = jp3.parse(new InputStreamReader((InputStream) request3.getContent()));
        JsonObject rootobj3 = root3.getAsJsonObject();
        JsonElement nameF = rootobj3.get("name");
        JsonObject nameFac = nameF.getAsJsonObject();
        facultyName = nameFac.get("pl").getAsString();

        Course addedCourse = new Course(id, plName, facultyName, plDescription, acceptFlag, false);
        return addedCourse;
    };

    public boolean coolTransaction(Course addedCourse){
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(addedCourse);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    };

    public boolean addCourses(String value) throws IOException {

        Course addedCourse = courseFromJSON(value, true);
        return coolTransaction(addedCourse);

    }

    public boolean proposeCourses(String value) throws IOException {

        Course addedCourse = courseFromJSON(value, false);
        return coolTransaction(addedCourse);

    }

    public boolean acceptCourse(Course course) {
        return acceptRejectCourse(true, course);
    }

    public boolean rejectCourse(Course course) {
        return acceptRejectCourse(false, course);
    }

    public boolean acceptRejectCourse(boolean accept, Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (accept) {
                course.acceptCourse();
            } else {
                course.rejectCourse();
            }
            session.update(course);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean delete(Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String cID = course.getId();
            session.load(Course.class, cID);
            session.delete(course);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean delete(Set<Course> selectedCourses) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Course course : selectedCourses) {
                String cID = course.getId();
                Course persistentInstance = session.load(Course.class, cID);
                session.delete(persistentInstance);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean unregister(Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Student user = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
            course.getStudents().remove(user);
            session.update(course);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean unregister(Set<Course> selectedCourses) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Student user = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
            int count = 0;
            for (Course course : selectedCourses) {
                course.getStudents().remove(user);
                session.update(course);
                count++;
                if (count > 20) {
                    session.flush();
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean exportStudents(Set<Course> selectedCourses) {
        List<Course> courses = new ArrayList<>();
        courses.addAll(selectedCourses);
        ReportingUtils.generateReport(courses);
        return true;
    }
}

        /*
        try {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            int count = 0;
            tx = session.beginTransaction();
            for (Course course : selectedCourses) {
                result.add(course.getId(), course.getStudents());
                course.rejectCourse();
                session.update(course);
                count++;
                if (count > 20) {
                    session.flush();
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true; */
//todo here call to some print to external format method

//
//    /***
//     * Returns all courses.
//     * @return ArrayList<Course>
//     */
//    public synchronized List<Course> findAll() {
//        ArrayList<Course> arrayList = new ArrayList<>();
//        for (Course course : courses.values()) {
//            arrayList.add(course);
//        }
//        return arrayList;
//    }
//
//    /***
//     * Returns all courses whose String representation contains given String.
//     * @param value filtering value
//     * @return ArrayList<Course>
//     */
//    public synchronized List<Course> findAll(String value) {
//        ArrayList<Course> arrayList = new ArrayList<>();
//        for (Course course : courses.values()) {
//            boolean passesFilter = (value == null || value.isEmpty())
//                    || course.toString().toLowerCase().contains(value.toLowerCase());
//            if (passesFilter) {
//                arrayList.add(course);
//            }
//        }
////        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
//        return arrayList;
//    }
//
//    /***
//     * Returns all courses whose String representation contains given String and that current user is registered to.
//     * @param value String, filtering value
//     * @param value1 boolean, indicates whether the fact that user is registered to should be taken into account
//     * @return ArrayList<Course>
//     */
//    public synchronized List<Course> findAll(String value, Boolean value1) {
//        Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
//        ArrayList<Course> arrayList = new ArrayList<>();
//        for (Course course : courses.values()) {
//            boolean passesFilter = (value == null || value.isEmpty())
//                    || (course.toString().toLowerCase().contains(value.toLowerCase()) && (value1 || student.isRegisteredTo(course)));
//            if (passesFilter) {
//                arrayList.add(course);
//            }
//        }
////        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
//        return arrayList;
//    }
//}


