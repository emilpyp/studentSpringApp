package az.code.springappstudents.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Student")
@Table(name = "students")
@NamedQueries({
        @NamedQuery(name = "findAll", query = "SELECT s FROM Student s"),
        @NamedQuery(name = "findById", query = "SELECT s FROM Student s WHERE s.id = :id")
})
public class Student implements IStudent{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_id_gen"
    )
    @SequenceGenerator(
            name = "student_id_gen",
            allocationSize = 1
    )
    private Long id;
    private String firstname;
    private String lastname;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Mark.class)
    private List<Mark> marks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}