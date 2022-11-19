package trading_system.bitcoin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "WALLET")
@Getter
@Setter
@ToString
public class Wallet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private double total_krw;

    private double in_use_krw;

    private double available_krw;

    private double total_btc;

    private double in_use_btc;

    private double available_btc;

    private int xcoin_last_btc;
}
