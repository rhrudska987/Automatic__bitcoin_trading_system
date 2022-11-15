package trading_system.bitcoin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ASKING_PRICES")
@ToString
public class Asking_Prices {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Anum;

    private String coincode;

    private Double quantity;

    private Double price;

    private LocalDateTime date;


}
