package pl.kognitywistyka.app.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import javax.persistence.NoResultException;
import java.util.*;

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
                        .setParameter("id", "%"+id.toLowerCase()+"%");
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
            List resultList = null;
            List<Course> finalList = null;
            try {
                tx = session.beginTransaction();
                Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
                //todo
                Query query = session.createQuery(
                        "from Course as course " +
                                "join course.students as students " +
                                "where lower(course.id) like :id " +
                                "and :student not in " +
                                "elements(students.id) " +
                                "and course.accepted = true")
                        .setParameter("id", "%" + id.toLowerCase() + "%").setParameter("student", student.getId());
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
                session.save(course);
                session.save(user);
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
            course.addStudent(user);
//            session.save(user);
            session.save(course);
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

    public boolean addCourses(String value) {
        //todo Currently supports only one course and no REST
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //todo connecting qith USOS here
            session.save(new Course(value, value, value, true, false));
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

    public boolean proposeCourses(String value) {
        //todo Currently supports only one course and no REST
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //todo connecting with USOS here + plus querying db whether exists
            session.save(new Course(value, value, value, false, false));
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
            session.remove(course);
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
                session.remove(course);
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
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        HashMap<String, Set<Student>> resultMap = new HashMap<>();
        int count = 0;
        try {
            tx = session.beginTransaction();
            for (Course course : selectedCourses) {
                resultMap.put(course.getId(), course.getStudents());
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
        return true;
        //todo here call to some print to external format method
    }

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
}
