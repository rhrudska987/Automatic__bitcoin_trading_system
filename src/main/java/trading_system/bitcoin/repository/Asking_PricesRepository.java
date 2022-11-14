package trading_system.bitcoin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trading_system.bitcoin.model.Asking_Prices;
import trading_system.bitcoin.model.Prices;

import java.util.List;

@Repository
public interface Asking_PricesRepository extends CrudRepository<Asking_Prices, Integer> {
    List<Asking_Prices> findFirst10Asking_PriceByCoincodeOrderByDateDesc(String Coincode); // 최신 10개 거래량 정보 조회
}
