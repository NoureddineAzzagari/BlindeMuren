package nl.avans.ivh11.BlindWalls.domain.mural;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mural {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    @Column(name="description")
    @NotEmpty(message = "{validation.description.NotEmpty.message}")
    @Size(min = 10, max = 10000, message = "{validation.description.Size.message}")
    private String description;
    @Column(name="artistname")
    private String artistName;
    @Column(name="name")
    @NotEmpty(message = "{validation.name.NotEmpty.message}")
    @Size(min = 1, max = 100, message = "{validation.name.Size.message}")
    private String name;

    private Mural(MuralBuilder muralBuilder) {
        this.artistName = muralBuilder.artistName;
        this.description = muralBuilder.description;
        this.name = muralBuilder.name;
    }

    public static class MuralBuilder {
        private String artistName;
        private final String description;
        private final String name;

        public MuralBuilder(String description, String name) {
            this.description = description;
            this.name = name;
        }

        public MuralBuilder artistName(String artistName) {
            this.artistName = artistName;
            return this;
        }

        public Mural build() {
            return new Mural(this);
        }
    }

//    @Id
//    @GeneratedValue
//    private Long id;
//
//    @NotEmpty(message = "Message may not be empty.")
//    private String name;
//
//    @NotEmpty(message = "Message is required.")
//    private String description;
//
//    public Mural() {}
//
//    public Mural(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setId(Long id){ this.id = id; }

}
