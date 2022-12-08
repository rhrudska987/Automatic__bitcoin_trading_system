# 빗썸 API를 이용한 나만의 트레이딩 웹 구현

자바(IntelliJ), 스프링(IntelliJ), H2 Database를 이용하여 비트코인 트레이딩 웹을 구현하였으며 실시간 가격 및 거래량, 매수, 매도, 내 자산 확인 이렇게 크게 4가지 파트로 나눴습니다.  
구현한 웹 페이지를 통해서 3분마다 현재 가격과 3분간의 거래량을 확인할 수 있으며 추가적으로 매수 및 매도 또한 할 수 있으며 매수 및 매도 시, 자산 변화를 직접 웹으로 확인할 수 있습니다.  
제작 과정에서 웹 MVC와 데이터베이스와의 연동 과정을 깊게 공부할 수 있었습니다.  
빗썸에 정상적으로 request 값이 가서 프로그램은 정상적으로 작동하나, 빗썸으로부터 받은 response 값이 {"status":"5100","message":"Bad Request.(Auth Data)"}인 이유는 
아직 발견하지 못하였습니다. 아직 발전해야 할 부분이 많은 것 같습니다.  
후에 조금 더 발전시켜 비트코인 자동매매 프로그램을 만드는 것이 목표입니다.  
**참고자료 : [비트코인 거래소 API를 활용한 비트코인 나만의 웹서비스 개발](https://kim-oriental.tistory.com/10)

<img width="960" alt="전체" src="https://user-images.githubusercontent.com/59828706/202992762-51fb6661-e422-439b-a3a2-583b14a83d09.png">  
초기 화면

## 목차
### [1. 사용 방법](#사용방법)
### [2. 거래량 및 가격](#3분마다-비트코인-거래량-및-가격을-받아서-차트로-구현)
### [3. 매도 및 매수](#매수-및-매도-창-구현)
### [4. 내 자산 확인](#내-지갑정보를-보여주는-테이블-구현)
### [5. 참고 자료](#참고-자료)


## 사용방법
1. 빗썸 홈페이지를 통해서 API 사용을 위한 Connect key 및 Secret key를 발급받습니다. ([key 발급 방법](https://www.hides.kr/795))
2. zip파일을 인텔리제이로 실행한 뒤, 발급받은 키를 Service 패키지에 SavePriceService.java와 WebPageService.java에서 static Api_Client apiClient = new Api_Client("Connect key", "Secret key");를 찾아서 각각 집어넣어 줍니다.
<img width="551" alt="api_key" src="https://user-images.githubusercontent.com/59828706/202992581-bfc09ab7-4ed7-401a-9bac-4cc086a8f82b.png">

3. H2 database설치 후, COINS, PRICES, WALLET 테이블을 만든다. (COINS와 PRICES는 참고자료에 설명되어있다.)  
CREATE TABLE WALLET  
(ID INT PRIMARY KEY AUTO_INCREMENT,  
TOTAL_KRW DOUBLE,  
IN_USE_KRW DOUBLE,  
AVAILABLE_KRW DOUBLE,  
TOTAL_BTC DOUBLE,  
IN_USE_BTC DOUBLE,  
AVAILABLE_BTC DOUBLE,  
XCOIN_LAST_BTC INT);
4. 인텔리제이를 실행을 한 뒤, localhost:8080으로 접속하여 구현된 웹페이지를 확인합니다.
5. 빗썸앱을 열고 확인한다.
## 3분마다 비트코인 거래량 및 가격을 받아서 차트로 구현

<img width="287" alt="h2_가격_거래량" src="https://user-images.githubusercontent.com/59828706/202993195-e049380d-9b2f-4f81-9659-8c1bc678a1ed.png">

H2 Database를 사용하여 PRICES테이블(PNUM, COINCODE, PRICE, VOLUME, DATE)을 구현

3분마다 빗썸으로부터 인텔리제이를 통해 정보를 받아와 H2 Database에 저장 

<img width="908" alt="가격_거래량_표" src="https://user-images.githubusercontent.com/59828706/202993186-f0fc6394-6225-4015-bca0-62902c353fae.png">

H2 Database에 들어있는 데이터를 가져와 웹에 테이블 구현

<img width="944" alt="가격_거래량_차트" src="https://user-images.githubusercontent.com/59828706/202993165-c59ee442-718e-428e-9878-04d1e3b51489.png">

H2 Database에 들어있는 데이터를 가져와 웹에 차트를 구현

여기까지는 참고 자료를 통해서 구현

## 매수 및 매도 창 구현
<img width="203" alt="초기" src="https://user-images.githubusercontent.com/59828706/203011643-be340e71-304d-4609-b3fa-eb36a67b6f38.png">

빗썸 앱 초기화면

<img width="203" alt="매수" src="https://user-images.githubusercontent.com/59828706/202993592-72478af2-3039-4980-bd67-0c7e046ed593.png">

0.0002BTC 매수

<img width="203" alt="매수_후" src="https://user-images.githubusercontent.com/59828706/203011666-e3da61c7-e739-4f85-8060-4806ced8bfc6.png">

매수 후

<img width="203" alt="매도" src="https://user-images.githubusercontent.com/59828706/202993604-fa9ba9a7-41f1-4dfc-b309-c1cce88783c5.png">

0.0001BTC 매도

<img width="203" alt="매도_후" src="https://user-images.githubusercontent.com/59828706/203011687-6cec4daa-43f5-43e3-99e1-481c9d8136ea.png">

매도 후

## 내 지갑정보를 보여주는 테이블 구현

<img width="605" alt="지갑_초기" src="https://user-images.githubusercontent.com/59828706/202995100-5b82b63d-d0f3-4368-bb30-740b99e8f310.png">

내 자산 초기 상태

<img width="638" alt="지갑_매수" src="https://user-images.githubusercontent.com/59828706/202995109-3c07afd0-b475-4049-96b3-79674d0df482.png">

0.0002BTC 매수 후 내 자산

<img width="619" alt="지갑_매도" src="https://user-images.githubusercontent.com/59828706/202995127-0cf047bc-082f-4ff3-b94f-2a78ada144fb.png">

0.0001BTC 매도 후 내 자산

## 참고 자료
거래량 및 가격을 차트로 표현하는 부분까지는 참고하였으나, 나머지 매수, 매도, 자산 확인 파트는 전부 직접 만들었습니다.
1. [key 발급 방법](https://www.hides.kr/795)
2. [비트코인 거래소 API를 활용한 비트코인 나만의 웹서비스 개발](https://kim-oriental.tistory.com/10)
