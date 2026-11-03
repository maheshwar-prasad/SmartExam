package com.smartexam.payment.mapper;

import com.smartexam.payment.dto.PaymentResponse;
import com.smartexam.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "subscription.id", target = "subscriptionId")
    PaymentResponse toResponse(Payment payment);
}