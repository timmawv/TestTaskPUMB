package avlyakulov.timur.TestTaskPUMB.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "animals")
@ToString
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Integer category;

    public AnimalEntity(String name, String type, String sex, Integer weight, Integer cost, Integer category) {
        this.name = name;
        this.type = type;
        this.sex = sex;
        this.weight = weight;
        this.cost = cost;
        this.category = category;
    }
}