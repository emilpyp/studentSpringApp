package az.code.springappstudents.dao;

import az.code.springappstudents.models.IStudent;
import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;

import java.util.List;

public interface StudentDao {
    List<Student> getAll();
    Student getById(long id);
    Student save(Student s);
    List<Student> find(String name, String surname);
    Student delete(long id);

    List<Mark> getMarks(long id);
    List<Mark> getMarks(String name);
    Mark getMark(long studentId, long markId);
    Mark deleteMark(long studentId, long markId);
    Mark saveMark(long studentId, Mark newMark);

    public void fillTables();
}