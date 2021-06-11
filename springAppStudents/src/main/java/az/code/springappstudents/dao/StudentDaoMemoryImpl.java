package az.code.springappstudents.dao;

import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Repository("fileDao")
public class StudentDaoMemoryImpl implements StudentDao{
    private long studentId = 11;
    private long markId = 6;
    private final Map<Long, Student> studentMap = new HashMap<>();

    @Override
    public List<Student> getAll() {
        return new ArrayList<>(studentMap.values());
    }

    @Override
    public Student getById(long id) {
        return studentMap.get(id);
    }

    @Override
    public Student save(Student s) {
        Student inMap;
        if((inMap = studentMap.get(s.getId())) != null){
            inMap.setFirstname(s.getFirstname());
            inMap.setLastname(s.getLastname());
            return inMap;
        }
        else if(s.getId() == null){
            s.setId(studentId++);
            studentMap.put(s.getId(), s);
            return s;
        }
        return null;
    }

    @Override
    public List<Student> find(String name, String lastname) {
        return studentMap
                .values()
                .stream()
                .filter(s -> s.getFirstname().equals(name) || s.getLastname().equals(lastname))
                .collect(Collectors.toList());
    }

    @Override
    public Student delete(long id){
        return studentMap.remove(id);
    }

    @Override
    public List<Mark> getMarks(long id) {
        Student s = getById(id);
        if(s != null){
            return s.getMarks();
        }
        return null;
    }

    @Override
    public List<Mark> getMarks(String name) {
        return studentMap
                .values()
                .stream()
                .filter(s-> s.getFirstname().startsWith(name))
                .flatMap(s -> s.getMarks().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Mark getMark(long studentId, long markId) {
        Student s = getById(studentId);
        if(s != null){
            return s.getMarks().stream().filter(m -> m.getId() == markId).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public Mark saveMark(long studentId, Mark newMark) {
        if(newMark.getId() == null){
            Student student = getById(studentId);
            if(newMark.getGrade() < 0 || newMark.getGrade() > 100){
                return null;
            }
            newMark.setId(markId++);
            student.getMarks().add(newMark);
            return newMark;
        }
        Mark mark = getMark(studentId, newMark.getId());
        if(mark != null){
            mark.setCreationDate(newMark.getCreationDate() != null ? newMark.getCreationDate(): mark.getCreationDate());
            mark.setGrade(newMark.getGrade() >= 0 && newMark.getGrade() <= 100 ? newMark.getGrade(): mark.getGrade());
            mark.setLessonName(newMark.getLessonName() != null && !newMark.getLessonName().equals("") ? newMark.getLessonName(): mark.getLessonName());
        }
        return newMark;
    }

    @Override
    public void fillTables() {
        Random rng = new Random(47);
        LongStream.rangeClosed(1,10)
                .mapToObj(n -> {
                    Student s = Student.builder().id(n).firstname("fname" + n).lastname("lname"+ n).build();
                    s.setMarks(LongStream.rangeClosed(1,5)
                            .mapToObj(i -> new Mark(i, "Subject" + i, rng.nextInt(99) + 1, LocalDate.now(), s))
                            .collect(Collectors.toList()));
                    return s;
                })
                .forEach(s -> studentMap.put(s.getId(), s));
    }

    @Override
    public Mark deleteMark(long studentId, long markId) {
        Student s = getById(studentId);
        if(s == null) {
            return null;
        }
        int idx;
        if((idx = idxOfMark(s, markId)) == -1){
            return null;
        }
        Mark mark = s.getMarks().get(idx);
        s.getMarks().remove(mark);
        return mark;

    }


    private int idxOfMark(Student s, long markId){
        Mark mark = s.getMarks().stream().filter(m -> m.getId() == markId).findFirst().orElse(null);
        if(mark == null) {
            return -1;
        }
        return s.getMarks().indexOf(mark);
    }
}