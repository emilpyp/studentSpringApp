package az.code.springappstudents.dto;

import az.code.springappstudents.models.IStudent;
import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto implements IStudent {
    private String firstname;
    private String lastname;
    private List<SimpleMark> marks;

    public StudentDto(Student s){
        this.firstname = s.getFirstname();
        this.lastname = s.getLastname();
        s.getMarks().stream().map(SimpleMark::new).forEach(marks::add);
    }

    private static class SimpleMark{
        private String lessonName;
        private int grade;
        private LocalDate creationDate;

        public SimpleMark(Mark m){
            this.lessonName = m.getLessonName();
            this.grade = m.getGrade();
            this.creationDate = m.getCreationDate();
        }
    }
}
