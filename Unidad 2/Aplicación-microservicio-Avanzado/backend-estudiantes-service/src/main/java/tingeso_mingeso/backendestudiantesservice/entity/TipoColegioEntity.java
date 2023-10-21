package tingeso_mingeso.backendestudiantesservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
