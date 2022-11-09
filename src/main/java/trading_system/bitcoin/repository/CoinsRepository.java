package trading_system.bitcoin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trading_system.bitcoin.model.Coins;

@Repository
public interface CoinsRepository extends CrudRepository<Coins, String> {

}