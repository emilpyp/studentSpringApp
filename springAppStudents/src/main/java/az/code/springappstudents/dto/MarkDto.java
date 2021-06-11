package az.code.springappstudents.dto;

import az.code.springappstudents.models.Mark;
import az.code.springappstudents.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDto {
    private String lessonName;
    private int grade;
    private LocalDate creationDate;
    private Student student;

    public MarkDto(Mark m){
        this.lessonName = m.getLessonName();
        this.grade = m.getGrade();
        this.creationDate = m.getCreationDate();
        this.student = Student.builder().build();
    }
}
