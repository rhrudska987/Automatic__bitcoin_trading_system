package trading_system.bitcoin.repository;

import org.springframework.data.repository.CrudRepository;
import trading_system.bitcoin.model.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
}
