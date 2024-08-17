package com.udacity.pricing;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getPricing() throws Exception {
    long vehicleId = ThreadLocalRandom.current().nextLong(1, 20);
    mockMvc.perform(
            MockMvcRequestBuilders.get("/prices/{vehicleId}", String.valueOf(vehicleId)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("{}"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.price").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").isNumber()
        );
  }
}
