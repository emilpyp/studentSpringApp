package az.code.springappstudents.controllers;

import az.code.springappstudents.exceptions.StudentNotFound;
import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import az.code.springappstudents.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final int DEFAULT_PAGE_NUM = 1;
    private final int DEFAULT_PAGE_SIZE = 3;
    StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("")
    public ResponseEntity<?> getStudents(HttpServletRequest request,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size){
        // Return all.
        if(page.isEmpty() && size.isEmpty()){
            List<Student> result = studentService.getAllStudents();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        // Pagination.
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        String requestUrl = String.format("%s?", request.getRequestURL());
        return new ResponseEntity<>(studentService.getStudentPage(pageNum, pageSize, requestUrl), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id){
        Student s = studentService.getStudentById(id);
        if(s == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(HttpServletRequest request,
                                  @RequestParam String firstname,
                                  @RequestParam String lastname,
                                  @RequestParam Optional<Integer> page,
                                  @RequestParam Optional<Integer> size){
        if(page.isEmpty() && size.isEmpty()){
            return new ResponseEntity<>(studentService.findStudents(firstname, lastname), HttpStatus.OK);
        }
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        String requestUrl = String.format("%s?firstname=%s&lastname=%s&", request.getRequestURL(), firstname, lastname);
        return new ResponseEntity<>(
                studentService.findStudents(firstname, lastname, pageNum, pageSize, requestUrl),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody Student student){
        student.setId(null);
        Student s = studentService.saveStudent(student);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student){
        student.setId((long) id);
        Student s = studentService.saveStudent(student);
        if(s == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable int id){
        Student s = studentService.deleteStudent(id);
        if(s == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping("/{id}/marks")
    public ResponseEntity<?> getMarksForStudent(HttpServletRequest request,
                                                @PathVariable int id,
                                                @RequestParam Optional<Integer> page,
                                                @RequestParam Optional<Integer> size){
        if(page.isEmpty() && size.isEmpty()){
            List<Mark> marks = studentService.getMarks(id);
            return new ResponseEntity<>(marks, HttpStatus.OK);
        }
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        String requestUrl = String.format("%s?", request.getRequestURL());
        return new ResponseEntity(studentService.getMarksPage(id, pageNum, pageSize, requestUrl), HttpStatus.OK);

    }

    @GetMapping("/{studentId}/marks/{markId}")
    public ResponseEntity<Mark> getMarkForStudent(@PathVariable int studentId,
                                                  @PathVariable int markId){
        Mark m = studentService.getMarkForStudent(studentId, markId);
        if(m == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}/marks/{markId}")
    public ResponseEntity<Mark> deleteMarkForStudent(@PathVariable int studentId,
                                                     @PathVariable int markId){
        Mark m = studentService.deleteMarkForStudent(studentId, markId);
        if(m == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @PostMapping("/{studentId}/marks")
    public ResponseEntity<Mark> saveMarkForStudent(@PathVariable int studentId,
                                                   @RequestBody Mark newMark){
        newMark.setId(null);
        Mark mark = studentService.saveMark(studentId, newMark);
        if(mark == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

    @PutMapping("/{studentId}/marks/{markId}")
    public ResponseEntity<Mark> updateMarkForStudent(@PathVariable int studentId,
                                                     @PathVariable int markId,
                                                     @RequestBody Mark newMark){
        newMark.setId((long) markId);
        Mark mark = studentService.saveMark(studentId, newMark);
        if(mark == null){
            throw new StudentNotFound();
        }
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

    @GetMapping("/avg/{count}")
    public ResponseEntity<?> studentsWithHighestGpa(HttpServletRequest request,
                                                    @PathVariable int count,
                                                    @RequestParam Optional<Integer> page,
                                                    @RequestParam Optional<Integer> size){
        if(page.isEmpty() && size.isEmpty()){
            return new ResponseEntity<>(studentService.studentsWithHighestGpa(count), HttpStatus.OK);
        }
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        String requestUrl = String.format("%s?", request.getRequestURL());
        return new ResponseEntity<>(studentService.studentsWithHighestGpa(count, pageNum, pageSize, requestUrl), HttpStatus.OK);

    }


    @GetMapping("/cutoff")
    public ResponseEntity<?> studentsAboveCutOff(HttpServletRequest request,
                                                 @RequestParam int mark,
                                                 @RequestParam Optional<String> nameLike,
                                                 @RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> size){
        if(page.isEmpty() && size.isEmpty()){
            if(nameLike.isEmpty()){
                return new ResponseEntity<>(studentService.studentsAboveCutOff(mark), HttpStatus.OK);
            }
            return new ResponseEntity<>(studentService.studentsAboveCutOff(mark, nameLike.get()), HttpStatus.OK);
        }
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        if(nameLike.isEmpty()){
            String requestUrl = String.format("%s?mark=%d&", request.getRequestURL(), mark);
            return new ResponseEntity<>(studentService.studentsAboveCutOff(mark, pageNum, pageSize, requestUrl), HttpStatus.OK);
        }
        String requestUrl = String.format("%s?mark=%d&nameLike=%s&", request.getRequestURL(), mark, nameLike);
        return new ResponseEntity<>(studentService.studentsAboveCutOff(mark, nameLike.get(), pageNum, pageSize, requestUrl), HttpStatus.OK);
    }

    @GetMapping("/aboveAvg")
    public ResponseEntity<?> studentsAboveAvg(HttpServletRequest request,
                                              @RequestParam Optional<Integer> page,
                                              @RequestParam Optional<Integer> size){
        if(page.isEmpty() && size.isEmpty()){
            return new ResponseEntity<>(studentService.studentsAboveAvg(), HttpStatus.OK);
        }
        int pageNum = page.orElse(DEFAULT_PAGE_NUM);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        String requestUrl = String.format("%s?", request.getRequestURL());
        return new ResponseEntity<>(studentService.studentsAboveAvg(pageNum, pageSize, requestUrl), HttpStatus.OK);

    }

    @GetMapping("/marks/filter")
    public ResponseEntity<List<Mark>> filterMarks(@RequestParam String studentName,
                                                  @RequestParam int minMark){
        List<Mark> filteredMarks = studentService.filterMarks(studentName, minMark);
        return new ResponseEntity<>(filteredMarks, HttpStatus.OK);
    }

}