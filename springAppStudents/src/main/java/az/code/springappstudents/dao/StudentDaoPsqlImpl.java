package az.code.springappstudents.dao;

import az.code.springappstudents.models.IStudent;
import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Repository("dbDao")
@Transactional
public class StudentDaoPsqlImpl implements StudentDao{
    @PersistenceContext
    EntityManager entityManager;

    public StudentDaoPsqlImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<Student> getAll() {
        Query query = entityManager.createQuery("Select s from Student s");

        return query.getResultList();
    }

    @Override
    public Student getById(long id) {
        return entityManager.find(Student.class, id);
    }

    @Transactional
    @Override
    public Student save(Student s) {
        return entityManager.merge(s);
    }

    @Override
    public List<Student> find(String name, String surname) {
        Query query = entityManager.createQuery("Select s from Student s where " +
                "s.firstname = :fname and s.lastname = :lname");
        return query.setParameter("fname", name).setParameter("lname", surname).getResultList();
    }

    @Override
    public Student delete(long id) {
        Student student = entityManager.find(Student.class, id);
        entityManager.remove(student);
        return student;
    }

    @Override
    public List<Mark> getMarks(long id) {
        System.out.println(id);
        Query query = entityManager.createQuery("Select m from Mark m Where m.student.id=:sid");
        return query.setParameter("sid", id).getResultList();
    }

    @Override
    public List<Mark> getMarks(String firstname) {
        Query query = entityManager.createQuery("Select m from Mark m where " +
                "m.student.firstname LIKE :name");
        return query.setParameter("name", firstname+"%").getResultList();
    }

    @Override
    public Mark getMark(long studentId, long markId) {
        Query query = entityManager.createQuery("Select m from Mark m " +
                "Where m.student.id = :sid and m.id = :mid");
        return (Mark) query.setParameter("sid", studentId).setParameter("mid", markId).getResultList().get(0);
    }

    @Override
    public Mark deleteMark(long studentId, long markId) {
        Student s = getById(studentId);
        Mark mark = getMark(studentId, markId);
        s.getMarks().remove(mark);
        entityManager.remove(mark);
        return mark;
    }

    @Override
    public Mark saveMark(long studentId, Mark newMark) {
        if(newMark.getId() == null) {
            Student student = entityManager.find(Student.class, studentId);
            student.getMarks().add(newMark);
            return newMark;
        }
        Mark mark = getMark(studentId, newMark.getId());
        mark.setCreationDate(newMark.getCreationDate() != null ? newMark.getCreationDate(): mark.getCreationDate());
        mark.setGrade(newMark.getGrade() >= 0 && newMark.getGrade() <= 100 ? newMark.getGrade(): mark.getGrade());
        mark.setLessonName(newMark.getLessonName() != null && !newMark.getLessonName().equals("") ? newMark.getLessonName(): mark.getLessonName());
        return mark;
    }

    @Override
    public void fillTables(){
        Random rng = new Random(47);
        LongStream.rangeClosed(1,10)
                .mapToObj(n -> {
                    Student s = Student.builder().firstname("fname" + n).lastname("lname"+ n).build();
                    s.setMarks(LongStream.rangeClosed(1,5)
                            .mapToObj(i -> Mark.builder()
                                    .lessonName("Subject" + i)
                                    .grade(rng.nextInt(99) + 1)
                                    .creationDate(LocalDate.now())
                                    .student(s).build())
                            .collect(Collectors.toList()));
                    return s;
                })
                .forEach(this::save);
    }
}
