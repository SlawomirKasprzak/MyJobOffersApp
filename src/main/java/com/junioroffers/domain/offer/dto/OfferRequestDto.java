package com.junioroffers.domain.offer.dto;

public record OfferRequestDto(
        String companyName,
        String position,
        String salary,
        String offerUrl
) {
}
