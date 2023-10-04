package tingeso_mingeso.topEducation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "tipo_colegio")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoColegioEntity {

    @Id
    @NotNull
    private int id;
    private String tipo;
}
