package com.example.batchtest.api;

import com.example.batchtest.domain.DailyMovie;
import com.example.batchtest.domain.DailyMovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class BoxOfficeApi {

    private final DailyMovieRepository dailyMovieRepository;

    //발급키
    String key = "f778bea14d8ca8349bc583598d1288e9";

    public void dailyBoxOffice(){

        String dailyResponse = "";

        //전날 박스오피스 조회 ( 오늘 날짜꺼는 안나옴.. )
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        String targetDt =  time.format(DateTimeFormatter.ofPattern("yyyMMdd"));

        //ROW 개수
        String itemPerPage = "10";

        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "";

        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "";

        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
            dailyResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, repNationCd, wideAreaCd);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(dailyResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");

            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");

            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_dailyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("dailyBoxOfficeList");
            for(int i=0; i<parse_dailyBoxOfficeList.size(); i++){
                JSONObject dailyBoxOffice = (JSONObject) parse_dailyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                DailyMovie dailyMovie = objectMapper.readValue(dailyBoxOffice.toString(), DailyMovie.class);
                //DB저장
                dailyMovie.setTargetDt(targetDt);
                dailyMovie.setBoxofficeType(boxofficeType);
                dailyMovie.setShowRange(showRange);
                dailyMovieRepository.save(dailyMovie);
            }
        }catch(Exception e){
            e.getMessage();
        }
    }
}
