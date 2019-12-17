package com.mtt.mvvm;

import com.mtt.test_module.api.ApiTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public class MainApiServiceTest extends ApiTest {


    private MainApiService apiService;

    @Before
    public void setUp() throws Exception {
        apiService = mRetrofit.create(MainApiService.class);

    }
    @Test
    public void getData() {
        apiService.getData().subscribe();

    }
}