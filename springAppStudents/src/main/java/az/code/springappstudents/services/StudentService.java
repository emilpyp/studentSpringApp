package az.code.springappstudents.services;

import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import az.code.springappstudents.pagination.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(int id);
    List<Student> findStudents(String n, String ln);
    Student saveStudent(Student s);
    Student deleteStudent(int id);
    List<Mark> getMarks(int id);
    Mark getMarkForStudent(int studentId,int markId);
    Mark deleteMarkForStudent(int studentId, int markId);
    Mark saveMark(int studentId, Mark mark);
    List<Student> studentsWithHighestGpa(int count);
    List<Student> studentsAboveCutOff(int mark);
    List<Student> studentsAboveCutOff(int mark, String nameLike);
    List<Student> studentsAboveAvg();
    List<Mark> filterMarks(String studentName, int minMark);

    Pagination<?> getStudentPage(int pageNum, int pageSize, String baseUrl);
    Pagination<?> getMarksPage(int id, int pageNum, int pageSize, String baseUrl);
    Pagination<?> studentsWithHighestGpa(int count, int pageNum, int pageSize, String baseUrl);
    Pagination<?> studentsAboveCutOff(int mark, int pageNum, int pageSize, String baseUrl);
    Pagination<?> studentsAboveCutOff(int mark, String nameLike, int pageNum, int pageSize, String baseUrl);
    Pagination<?> studentsAboveAvg(int pageNum, int pageSize, String baseUrl);
    Pagination<?> findStudents(String firstname, String lastname, int pageNum, int pageSize, String baseUrl);
}