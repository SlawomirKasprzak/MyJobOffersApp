package com.junioroffers.features;

import com.junioroffers.BaseIntegrationTest;
import org.junit.Test;

public class UserWantToSeeOffersIntegrationTest extends BaseIntegrationTest {
    @Test
    public void User_have_to_be_logged_in_to_see_job_offers_and_server_need_to_store_some_offers() {
//        step 1: HTTP server does not have any job offers
//        step 2: Planner ran for the first time - made GET to external server - system added 0 offers to database
//        step 3: User tried to get a token by requesting a POST/token with userName=randomUser; password=randomPassword - system returned unauthorized 401
//        step 4: User made GET/offers with no token and system returned unauthorized 401
//        step 5: User made POST/register with userName=randomUser and password=randomPassword - system successfully register user with status OK 200
//        step 6: User tried to get token by requesting POST/token with userName=randomUser; password=randomPassword and system returned OK 200 plus randomToken= A11.B22.C33
//        step 7: User made GET/offers with header “authorized: User A11.B22.C33 and system returned OK 200 with 0 offers
//        step 8: There are 2 new offers in external HTTP server
//        step 9: Planner ran 2nd time and made GET to external server and system added 2 new offers with ID: 1000 and 2000 to database
//        step 10: User made GET/offers with header “authorized: User A11.B22.C33 and system returned OK 200 with 2 offers with IDs: 1000 and 2000
//        step 11: User made GET/offers 9999 and system returned NOT_FOUND 404 with message “Offer with ID: 9999 not found”
//        step 12: User made GET/offers/1000 and system returned OK 200 with offer
//        step 13: There are 2 new offers in external HTTP server
//        step 14: Planner ran 3rd time and made GET to external server and system added 2 new offers  with IDs 3000 and 4000 to database
//        step 15: User made GET/offers with header “authorized: User A11.B22.C33” and system returned OK 200 with 4 offers with 1000, 2000, 3000 and 4000
//        step 16: User made POST/offers with header “authorized: User A11.B22.C33” - system returned CREATED 201 with saved offer
//        step 17: User made GET/offers with header “authorized: User A11.B22.C33” and system returned OK 200 with 1 offer

    }
}