package com.junioroffers.features;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.junioroffers.BaseIntegrationTest;
import com.junioroffers.SampleJobOfferResponse;
import com.junioroffers.domain.offer.dto.OfferResponseDto;
import com.junioroffers.infrastructure.offer.scheduler.HttpOffersScheduler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TypicalScenarioUserWantToSeeOffersIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @Autowired
    HttpOffersScheduler httpOffersScheduler;
    @Test
    public void user_want_to_see_offers_but_have_to_be_logged_in_and_external_server_should_have_some_offers() throws Exception {
        //step 1: HTTP server does not have any job offers
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));


        // step 2: Planner ran for the first time - made GET to external server - system added 0 offers to database
        // given && when
        List<OfferResponseDto> newOffers = httpOffersScheduler.fetchAllOffersAndSaveAllIfNotExists();
        // then
         assertThat(newOffers).isEmpty();


        //step 3: User tried to get a token by requesting a POST/token with userName=randomUser; password=randomPassword - system returned unauthorized 401
        //step 4: User made GET/offers with no token and system returned unauthorized 401
        //step 5: User made POST/register with userName=randomUser and password=randomPassword - system successfully register user with status OK 200
        //step 6: User tried to get token by requesting POST/token with userName=randomUser; password=randomPassword and system returned OK 200 plus randomToken= A11.B22.C33


        //step 7: User made GET/offers with header "Authorized: User A11.B22.C33" and system returned OK 200 with 0 offers
        //given
        String offersUrl = "/offers";
        // when
        ResultActions perform = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResult2 = perform.andExpect(status().isOk()).andReturn();
        String jsonWithOffers = mvcResult2.getResponse().getContentAsString();
        List<OfferResponseDto> offers = objectMapper.readValue(jsonWithOffers, new TypeReference<>() {});
        assertThat(offers).isEmpty();

        //step 8: There are 2 new offers in external HTTP server
        //step 9: Planner ran 2nd time and made GET to external server and system added 2 new offers with ID: 1000 and 2000 to database
        //step 10: User made GET/offers with header "Authorized: User A11.B22.C33" and system returned OK 200 with 2 offers with IDs: 1000 and 2000


        //step 11: User made GET/offers 9999 and system returned NOT_FOUND 404 with message “Offer with ID: 9999 not found”
        //given
        //when
        ResultActions performGetOffersExistingId = mockMvc.perform(get("/offers/9999"));
        //then
        performGetOffersExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message": "Offer with id 9999 not found",
                        "status": "NOT_FOUND"
                        }
                        """.trim()));

        //step 12: User made GET/offers/1000 and system returned OK 200 with offer
        //step 13: There are 2 new offers in external HTTP server
        //step 14: Planner ran 3rd time and made GET to external server and system added 2 new offers  with IDs 3000 and 4000 to database
        //step 15: User made GET/offers with header “authorized: User A11.B22.C33” and system returned OK 200 with 4 offers with 1000, 2000, 3000 and 4000
        //step 16: User made POST/offers with header “authorized: User A11.B22.C33” - system returned CREATED 201 with saved offer
        //step 17: User made GET/offers with header “authorized: User A11.B22.C33” and system returned OK 200 with 1 offer

    }
}
