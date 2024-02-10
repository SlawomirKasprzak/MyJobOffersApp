package com.junioroffers.domain.offer;

import com.junioroffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class OfferFacadeTestConfiguration {
    private final InMemoryFetcherTestImpl inMemoryFetcherTest;
    private final InMemoryOfferRepository offerRepository;

    OfferFacadeTestConfiguration() {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(
                List.of(
                        new JobOfferResponse("Junior", "IBM", "14000", "01"),
                        new JobOfferResponse("Senior", "mBank", "23000", "02"),
                        new JobOfferResponse("Junior", "Samsung", "9000", "03"),
                        new JobOfferResponse("Senior", "ING", "21000", "04"),
                        new JobOfferResponse("Junior", "Google", "15000", "05"),
                        new JobOfferResponse("Senior", "Santander", "22000", "06")
                )
        );
        this.offerRepository = new InMemoryOfferRepository();
    }

    OfferFacadeTestConfiguration(List<JobOfferResponse> remoteClientOffers) {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(remoteClientOffers);
        this.offerRepository = new InMemoryOfferRepository();
    }

    OfferFacade offerFacadeForTests() {
        return new OfferFacade(offerRepository, new OfferService(inMemoryFetcherTest, offerRepository));
    }
}
