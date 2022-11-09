package trading_system.bitcoin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trading_system.bitcoin.model.Prices;

@Repository
public interface PricesRepository extends CrudRepository<Prices, Integer>{

    List<Prices> findFirst10ByCoincodeOrderByDateDesc(String Coincode); // 최신 10개 가격 정보 조회

    Long countByCoincode(String Coincode); // 코인별 데이터 수 조회

}