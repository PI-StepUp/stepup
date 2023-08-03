package com.pi.stepup.domain.dance.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.dance.service.DanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DanceApiController.class)
public class DanceApiTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @MockBean
    private DanceService danceService;

    @BeforeEach
    public void init() {
//        gson = new Gson();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceApiTest() {
        final String url = "";
    }

}
