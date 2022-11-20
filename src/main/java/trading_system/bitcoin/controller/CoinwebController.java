package trading_system.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trading_system.bitcoin.model.Coins;
import trading_system.bitcoin.model.Prices;
import trading_system.bitcoin.model.Wallet;
import trading_system.bitcoin.service.SavePriceService;
import trading_system.bitcoin.service.WebPageService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CoinwebController {

    @Autowired
    WebPageService webPageService;
    @Autowired
    SavePriceService savePriceService;

    @GetMapping("/") // http://localhost:8080/ 진입 시 main.html 페이지 진입을 위한 ModelandView
    public String mainPage(Model model) throws Exception {
        List<Coins> coinList = webPageService.findAllCoins();
        model.addAttribute("coinList",coinList); // 전체 코인 리스트 전달

        List<Prices> priceList = new ArrayList<>();
        priceList = webPageService.findPriceList(coinList.get(0).getCoincode());
        model.addAttribute("priceList",priceList); // 코인 리스트의 첫번째 코인의 가격 정보 전달

        Optional<Wallet> wallet = webPageService.findRecentWallet();
        if(wallet.isPresent()){
            Wallet myWallet = wallet.get();
            model.addAttribute("myWallet", myWallet);
        }
        return "main";
    }

    @RequestMapping(value = "/")
    public String BuyCoin(HttpServletRequest request) throws Exception {
        Optional<String> units_buy_opt = Optional.ofNullable(request.getParameter("buy_units"));
        if(units_buy_opt.isPresent()){
            String unit_buy_str = units_buy_opt.get();
            BigDecimal units = new BigDecimal(unit_buy_str).abs();
            savePriceService.buyBTC(units, "BTC", "KRW");
        }
        else {
            Optional<String> units_sell_opt = Optional.ofNullable(request.getParameter("sell_units"));
            if(units_sell_opt.isPresent()) {
                String unit_sell_str = units_sell_opt.get();
                BigDecimal units = new BigDecimal(unit_sell_str).abs();
                savePriceService.sellBTC(units, "BTC", "KRW");
            }
        }
        return "redirect:";
    }

    @GetMapping("/coin/prices") // AJAX 구현을 위한 Price 데이터 전달 메소드
    public String getCoinPrices(Model model, @RequestParam String coinCode) throws Exception {

        List<Prices> priceList = new ArrayList<>();
        priceList = webPageService.findPriceList(coinCode); // 코인코드를 파라미터로 받아, DB 조회 후 가격 정보를 전달
        model.addAttribute("priceList",priceList);
        return "main :: priceTable"; // thymeleaf AJAX 구현을 위해, 데이터가 변경 될 ":: ID" 추가
    }
}
