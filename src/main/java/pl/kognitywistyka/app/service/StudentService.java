package pl.kognitywistyka.app.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import java.sql.Connection;
import java.util.*;

/**
 * Created by wikto on 21.06.2017.
 */
public class StudentService {

    private static StudentService instance;
    private HashMap<String, User> users;

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
            instance.ensureTestData();
        }
        return instance;
    }

    public void ensureTestData() {
        users = new HashMap<>();
        List<Student> studentsList = Arrays.asList(
                new Student("1", "Radosław", "Jurczak"),
                new Student("2", "Jakub", "Milewski"),
                new Student("3", "Wiktor", "Rorot")
        );
//        Integer id = 0;
        for (Student student : studentsList) {
            String id = student.getId();
            users.put(id, student);
//            id = id + 1;
        }
    }

    //todo
    public synchronized List<User> findAll() {
        return new ArrayList<>();
    }

    public synchronized List<Student> findById(String id) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Student> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Student where id =: id").setParameter("id", id);
            Student student = (Student) query.getSingleResult();
            resultList.add(student);
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

    public synchronized List<Student> findByName(String name) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Student> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(
                    "from Student where lower(firstName) =: name or lower(lastName) =: name order by id").setParameter(
                            "name", name.toLowerCase());
            resultList = (List<Student>) query.getResultList();
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

    public synchronized List<Student> findAllFinal() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<Student> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Student order by lastName, firstName, id");
            resultList = (List<Student>) query.getResultList();
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

    public synchronized ArrayList<User> findAll(String value) {
        ArrayList<User> arrayList = new ArrayList<>();
        for (User user : users.values()) {
            boolean passesFilter = (value == null || value.isEmpty())
                    || user.toString().toLowerCase().contains(value.toLowerCase());
            if (passesFilter) {
                //todo we should implement ordering here
                arrayList.add(user);
            }
        }
//        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
        return arrayList;
    }

    public void register(Set<Course> selectedCourses) {
    }

    public boolean delete(User student) {
        users.remove(student.getId());
        return true;
    }

    public boolean delete(Set<User> selectedStudents) {
        for (User user : selectedStudents) {
            users.remove(user.getId());
        }
        return true;
    }

    public boolean add(Student student) {
        try {
            student.setPassword(AuthenticationService.sha512(student.getPassword()));
            if (!users.containsKey(student.getId())) {
                users.put(student.getId(), student);
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
