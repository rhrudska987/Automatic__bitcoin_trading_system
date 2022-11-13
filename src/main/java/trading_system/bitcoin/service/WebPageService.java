package trading_system.bitcoin.service;
import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import trading_system.bitcoin.model.Coins;
import trading_system.bitcoin.model.Prices;
import trading_system.bitcoin.repository.CoinsRepository;
import trading_system.bitcoin.repository.PricesRepository;
import trading_system.bitcoin.service_external.Api_Client;

@Service
@Slf4j
/* Web 페이지의 정보 노출를 위한 조회용 서비스 */
public class WebPageService {

    @Autowired
    CoinsRepository coinsRepository;
    @Autowired
    PricesRepository pricesRepository;

    // 빗썸에서 제공한 API_Client Class 정의 (API Key 와 Secret를 입력)
    static Api_Client apiClient = new Api_Client("ecef4acd63c19d37fe38bf984827e70f", "d1eb6d02b003cb5df2e0820e22301b7e");

    // 내가 저장한 (관리할) 모든 코인의 목록을 가져온다
    public List<Coins> findAllCoins() throws Exception{
        return (List<Coins>) coinsRepository.findAll();
    }
    // 매개 변수로 받은 코인의 최근 10개 가격 데이터 저장
    public List<Prices> findPriceList(String coinCode) throws Exception{
        return pricesRepository.findFirst10ByCoincodeOrderByDateDesc(coinCode);
    }
}