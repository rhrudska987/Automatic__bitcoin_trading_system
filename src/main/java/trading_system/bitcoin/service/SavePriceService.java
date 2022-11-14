package trading_system.bitcoin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import trading_system.bitcoin.model.Asking_Prices;
import trading_system.bitcoin.model.Coins;
import trading_system.bitcoin.model.Prices;
import trading_system.bitcoin.repository.Asking_PricesRepository;
import trading_system.bitcoin.repository.CoinsRepository;
import trading_system.bitcoin.repository.PricesRepository;
import trading_system.bitcoin.service_external.Api_Client;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
/* 일정 주기로 코인 가격정보를 저장하기 위한 서비스 */
public class SavePriceService {

    @Autowired
    CoinsRepository coinsRepository;
    @Autowired
    PricesRepository pricesRepository;
    @Autowired
    Asking_PricesRepository asking_pricesRepository;

    // 빗썸에서 제공한 API_Client Class 정의 (API Key 와 Secret를 입력)
    static Api_Client apiClient = new Api_Client("ecef4acd63c19d37fe38bf984827e70f", "d1eb6d02b003cb5df2e0820e22301b7e");

    // 10분 동안 거래량 계산을 위한, 10분 전 가격 정보를 담을 Map
    static Map<String,Double> preVolumeMap = new HashMap<>();
    static Map<String, Double> preQuantityMap = new HashMap<>();

    // 프로그램 실행 시 이전에 저장했던 가격 정보는 삭제
    @PostConstruct
    private void initDelAllPrices() throws Exception {
        log.info("[initDelAllPrices] 프로그램 최초 실행 시, 기존 Price 데이터 삭제 (지운 데이터: " + pricesRepository.count() + ")");
        pricesRepository.deleteAll();
        asking_pricesRepository.deleteAll();
    }

    // 10분마다 코인의 가격과 거래량 정보를 저장
    @Scheduled(cron = "30 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,57 * * * *") // 매시 09:30, 19:30, 29:30, 39:30, 49:30, 59:30에 실행
    //@Scheduled(fixedDelay = 3000)
    public void savePriceEvery3min() throws Exception {
        Prices currentPrice = new Prices();
        Asking_Prices currentQuantity = new Asking_Prices();
        List<Coins> coins = (List<Coins>) coinsRepository.findAll();
        log.info("[savePriceEvery3min] 3분마다 가격 정보를 저장 (현재 시간: " + LocalDateTime.now() + ", 저장할 코인 수: "+ coins.size() + ")");

        double curPreGap = 0.0;

        for(Coins c : coins) {
            currentPrice = getCoinPrice(c.getCoincode());
            currentQuantity = getQuantity(c.getCoincode());
            if( preVolumeMap.get(c.getCoincode()) == null || pricesRepository.countByCoincode(c.getCoincode()) == 0 ) {// 최초 실행의 경우, 거래량은 0으로 현재가격을 저장
                preVolumeMap.put(c.getCoincode(), currentPrice.getVolume());
                preQuantityMap.put(c.getCoincode(), currentQuantity.getQuantity());
                currentPrice.setVolume(0.0);
                //currentQuantity.setQuantity(0.0);
            } else { // 직전 데이터가 있는 경우
                log.info(currentPrice.toString()+ " | " + preVolumeMap.toString());
                log.info(currentQuantity.toString() + " | " + preQuantityMap.toString());
                curPreGap = currentPrice.getVolume() - preVolumeMap.get(c.getCoincode()); // 현재 거래량에서 직전거래량을 빼면 10분 동안 거래량
                preVolumeMap.put(c.getCoincode(), currentPrice.getVolume());
                preQuantityMap.put(c.getCoincode(), currentQuantity.getQuantity());
                if( curPreGap >= 0 ) {
                    currentPrice.setVolume(curPreGap);
                } else { // 매일 00:09:30의 경우, 빗썸 API가 00시 기준으로 리셋이 되기 때문에 그 전날 23:59:30~24:00:00의 30초동안 거래량은 버리고 00:00:00~00:09:30의 거래량만 저장
                    currentPrice.setVolume(currentPrice.getVolume());
                }
                currentQuantity.setQuantity(currentQuantity.getQuantity());
            }
            pricesRepository.save(currentPrice); // DB에 저장
            asking_pricesRepository.save(currentQuantity);
        }
    }

    // 빗썸 API를 통해 코인의 현재 가격 정보를 가져 옴
    private Prices getCoinPrice(String coinCode) throws Exception{

        Prices price = new Prices();
        // 빗썸 API 호출을 위한 URL 생성
        String url = "/public/ticker/";
        url = url + coinCode + "_KRW";
        System.out.println("=====" + url + "====");
        // 빗썸 API 호출
        HashMap<String,String> params = new HashMap<>();
        String result = apiClient.callApi(url,params);
        // 응답받은 String 데이터를 Map 객체로 저장
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> m = (Map<String, String>) mapper.readValue(result, Map.class).get("data");
        // Price 객체에 각각 응답 값을 저장
        price.setCoincode(coinCode);
        price.setPrice(Double.parseDouble(m.get("closing_price")));
        price.setVolume(Double.parseDouble(m.get("units_traded")));
        price.setDate(LocalDateTime.now());

        return price;
    }

    private Asking_Prices getQuantity(String coinCode) throws Exception {
        Asking_Prices asking_prices = new Asking_Prices();

        String url = "/public/orderbook/";
        url = url + coinCode + "_KRW" + "?count=10";
        HashMap<String, String> params = new HashMap<>();
        //params.put("payment_currency", "KRW");
        String result = apiClient.callApi(url, params);

        //ObjectMapper mapper = new ObjectMapper();
        //Map<String, String> m = (Map<String, String>) mapper.readValue(result, Map.class).get("data");
        System.out.println("RESULT ================== " + result);
        JSONParser parser = new JSONParser();
        JSONObject bids = (JSONObject)parser.parse(result);
        JSONArray bids_arr = (JSONArray)bids.get("bids");

        for(int i=0; i<bids_arr.size(); i++){
            System.out.println("------------------");
            JSONObject temp = (JSONObject)bids_arr.get(i);
            asking_prices.setPrice(Double.parseDouble((String)temp.get("price")));
            asking_prices.setQuantity(Double.parseDouble((String)temp.get("quantity")));
        }

        asking_prices.setCoincode(coinCode);
        asking_prices.setDate(LocalDateTime.now());
        return asking_prices;
    }
}
