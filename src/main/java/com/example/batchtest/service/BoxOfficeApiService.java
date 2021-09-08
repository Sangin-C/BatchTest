package com.example.batchtest.service;

import com.example.batchtest.api.BoxOfficeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoxOfficeApiService {

    private final BoxOfficeApi boxOfficeApi;

    public void insertDailyBoxOffice(){
        boxOfficeApi.dailyBoxOffice();
    }
}
