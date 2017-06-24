package pl.kognitywistyka.app.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.persistence.HibernateUtils;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.user.Pokropski;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import javax.persistence.NoResultException;
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
        List<User> studentsList = Arrays.asList(
                new Student("1", "Radosław", "Jurczak"),
                new Student("2", "Jakub", "Milewski"),
                new Student("3", "Wiktor", "Rorot"),
                new Pokropski("admin", "admin", "admin", AuthenticationService.sha512("admin"))
        );
//        Integer id = 0;
        for (User user : studentsList) {
            String id = user.getId();
            users.put(id, user);
//            id = id + 1;
        }
    }

    //todo
//    public synchronized List<User> findAll() {
//        return new ArrayList<>();
//    }

    public synchronized List<User> findById(String id) throws NoResultException {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List<User> resultList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from User where id = :id").setParameter("id", id);
            User user = (User) query.getSingleResult();
            resultList = new ArrayList<>();
            resultList.add(user);
        } catch (NoResultException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    public synchronized List<User> findByName(String name) throws NoResultException {
        if(name.isEmpty()) return findAllFinal();
        else {
            Session session = HibernateUtils.getSessionFactory().openSession();
            Transaction tx = null;
            List resultList;
            List<User> finalList = new ArrayList<>();
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery(
                        "from Student where lower(firstName) like: name or lower(lastName) like: name order by id").setParameter(
                        "name", "%" + name.toLowerCase() + "%");
                resultList = query.getResultList();
                for (Object object : resultList) {
                    Student student = (Student) object;
                    finalList.add(student);
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

    public synchronized List<User> findAllFinal() throws NoResultException {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        List resultList;
        List<User> finalList = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Student order by lastName, firstName, id");
            resultList = query.getResultList();
            finalList = new ArrayList<>();
            for(Object object : resultList) {
                Student student = (Student) object;
                finalList.add(student);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return finalList;
    }

    //Adds user to db
    public void addUser(User user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            session.close();
            e.printStackTrace();
        }
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
