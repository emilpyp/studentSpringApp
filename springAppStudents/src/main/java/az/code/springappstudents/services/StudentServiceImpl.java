package az.code.springappstudents.services;

import az.code.springappstudents.dao.StudentDao;
import az.code.springappstudents.exceptions.StudentNotFound;
import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import az.code.springappstudents.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    @Value("${dao.name}")
    private String daoQualifier;
    StudentDao studentDAO;

    @Autowired
    private void getStudentDao(ApplicationContext context){
        this.studentDAO = (StudentDao) context.getBean(daoQualifier);
        studentDAO.fillTables();
    }


    @Override
    public List<Student> getAllStudents(){
        return studentDAO.getAll();
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.getById(id);
    }

    public List<Student> findStudents(String name, String lname){
        return studentDAO.find(name, lname);
    }

    @Override
    public Student saveStudent(Student s) {
        return studentDAO.save(s);
    }

    @Override
    public Student deleteStudent(int id) {
        return studentDAO.delete(id);
    }

    @Override
    public List<Mark> getMarks(int id) {
        return studentDAO.getMarks(id);
    }

    @Override
    public Mark getMarkForStudent(int studentId, int markId) {
        Mark mark = studentDAO.getMark(studentId, markId);
        if(mark != null) {
            return mark;
        }
        throw new StudentNotFound();
    }

    @Override
    public Mark deleteMarkForStudent(int studentId, int markId){
        Mark mark = studentDAO.deleteMark(studentId, markId);
        if(mark != null){
            return mark;
        }
        return null;
    }

    @Override
    public Mark saveMark(int studentId, Mark newMark) {
        Mark mark = studentDAO.saveMark(studentId, newMark);
        if(mark != null){
            return mark;
        }
        return null;
    }

    @Override
    public List<Student> studentsWithHighestGpa(int count) {
        List<Student> students = getAllStudents();
        students.stream()
                .filter(s-> !s.getMarks().isEmpty())
                .collect(Collectors.toList())
                .sort((s1, s2)->
                s2.getMarks().stream().mapToInt(Mark::getGrade).reduce(0, Integer::sum) / s2.getMarks().size() -
                s1.getMarks().stream().mapToInt(Mark::getGrade).reduce(0, Integer::sum) / s1.getMarks().size());
        if(count > students.size()) count = students.size();
        return students.subList(0, count);
    }

    @Override
    public List<Student> studentsAboveCutOff(int mark) {
        List<Student> students = getAllStudents();
        return students
                .stream()
                .filter(s -> s.getMarks().stream().anyMatch(m -> m.getGrade() > mark))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> studentsAboveCutOff(int mark, String nameLike) {
        List<Student> students = getAllStudents();
        return students
                .stream()
                .filter(s -> s.getMarks().stream().anyMatch(m -> m.getGrade() > mark) &&
                        s.getFirstname().startsWith(nameLike))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<?> studentsAboveCutOff(int mark, int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(studentsAboveCutOff(mark), pageNum, pageSize, baseUrl);
    }

    @Override
    public Pagination<?> studentsAboveCutOff(int mark, String nameLike, int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(studentsAboveCutOff(mark, nameLike), pageNum, pageSize, baseUrl);
    }


    @Override
    public List<Student> studentsAboveAvg() {
        List<Student> students = getAllStudents();
        List<Student> aboveAvg = new ArrayList<>();
        for(Student s: students){
            double above50 = (double) s.getMarks().stream().filter(m -> m.getGrade() > 50).count();
            if(above50 / s.getMarks().size() >= 0.7){
                aboveAvg.add(s);
            }
        }
        return aboveAvg;
    }

    @Override
    public List<Mark> filterMarks(String studentName, int minMark) {
        List<Mark> marks = studentDAO.getMarks(studentName+"%");
        return marks.stream().filter(m -> m.getGrade() > minMark).collect(Collectors.toList());
    }

    @Override
    public Pagination<?> getStudentPage(int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(getAllStudents(), pageNum, pageSize, baseUrl);
    }

    @Override
    public Pagination<?> getMarksPage(int id, int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(getMarks(id), pageNum, pageSize, baseUrl);
    }

    @Override
    public Pagination<?> studentsWithHighestGpa(int count, int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(studentsWithHighestGpa(count), pageNum, pageSize, baseUrl);
    }


    @Override
    public Pagination<?> studentsAboveAvg(int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(studentsAboveAvg(), pageNum, pageSize, baseUrl);
    }

    @Override
    public Pagination<?> findStudents(String firstname, String lastname, int pageNum, int pageSize, String baseUrl) {
        return new Pagination<>(findStudents(firstname, lastname), pageNum, pageSize, baseUrl);
    }
}