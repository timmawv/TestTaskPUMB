package avlyakulov.timur.TestTaskPUMB.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "animals")
public class Animal {

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
}