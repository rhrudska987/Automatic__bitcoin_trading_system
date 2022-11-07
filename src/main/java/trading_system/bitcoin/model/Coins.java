package trading_system.bitcoin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COINS")
@Getter
@Setter
@ToString
public class Coins {

    @Id
    private String coincode;

    private String coinname;

}