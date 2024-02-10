package com.junioroffers.domain.offer.dto;

public record JobOfferResponse(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
