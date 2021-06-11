package az.code.springappstudents.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Mark")
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mark_id_gen"
    )
    @SequenceGenerator(
            name = "mark_id_gen",
            allocationSize = 1
    )
    private Long id;
    private String lessonName;
    private int grade;
    private LocalDate creationDate;
    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonBackReference
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return id.equals(mark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}