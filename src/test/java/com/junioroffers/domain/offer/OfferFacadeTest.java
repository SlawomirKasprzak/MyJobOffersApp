package com.junioroffers.domain.offer;

import com.junioroffers.domain.offer.dto.JobOfferResponse;
import com.junioroffers.domain.offer.dto.OfferRequestDto;
import com.junioroffers.domain.offer.dto.OfferResponseDto;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


public class OfferFacadeTest {

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        List<OfferResponseDto> result = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        //then
        assertThat(result).hasSize(6);
    }

    @Test
    public void should_save_only_two_offers_when_repository_had_four_added_with_offer_urls() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                List.of(
                        new JobOfferResponse("Junior", "IBM", "14000", "01"),
                        new JobOfferResponse("Senior", "mBank", "23000", "02"),
                        new JobOfferResponse("Junior", "Samsung", "9000", "03"),
                        new JobOfferResponse("Senior", "ING", "21000", "04"),
                        new JobOfferResponse("Junior", "Google", "15000", "https://randomurl.pl/05"),
                        new JobOfferResponse("Senior", "Santander", "22000", "https://randomurl.pl/06")
                )
        ).offerFacadeForTests();
        offerFacade.saveOffer(new OfferRequestDto("id", "Junior", "9000", "01"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Senior", "17000", "02"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Junior", "8000", "03"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Senior", "18000", "04"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);
        //when
        List<OfferResponseDto> response = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        //then
        assertThat(List.of(
                        response.get(0).offerUrl(),
                        response.get(1).offerUrl()
                )
        ).containsExactlyInAnyOrder("https://randomurl.pl/05", "https://randomurl.pl/06");
    }

    @Test
    public void should_save_four_offers_when_there_are_no_offers_in_database() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        //when
        offerFacade.saveOffer(new OfferRequestDto("id", "Junior", "9000", "01"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Senior", "17000", "02"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Junior", "8000", "03"));
        offerFacade.saveOffer(new OfferRequestDto("id", "Senior", "18000", "04"));
        //then
        assertThat(offerFacade.findAllOffers()).hasSize(4);
    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("id", "Junior", "8000", "01"));
        //when
        OfferResponseDto offerById = offerFacade.findOfferById(offerResponseDto.id());
        //then
        assertThat(offerById).isEqualTo(OfferResponseDto.builder()
                .id(offerResponseDto.id())
                .companyName("id")
                .position("Junior")
                .salary("8000")
                .offerUrl("01")
                .build()
        );
    }

    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById("100"));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 100 not found");
    }
    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exist() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(
                new OfferRequestDto("id", "Senior", "21000", "random.pl"));
        String saveId = offerResponseDto.id();
        assertThat(offerFacade.findOfferById(saveId).id()).isEqualTo(saveId);
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(
                new OfferRequestDto("yz", "zx", "yx", "random.pl")));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferDuplicateException.class)
                .hasMessage("Offer with offerUrl [random.pl] already exist");
    }
}