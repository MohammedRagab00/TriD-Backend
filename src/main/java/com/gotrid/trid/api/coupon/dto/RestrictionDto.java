package com.gotrid.trid.api.coupon.dto;

import com.gotrid.trid.core.coupon.model.Restriction;
import jakarta.validation.constraints.NotNull;

public record RestrictionDto(
        @NotNull(message = "ID cannot be null")
        Integer id,
        @NotNull(message = "Restriction type cannot be null")
        Restriction restrictionType,
        @NotNull(message = "Restriction value cannot be null")
        Integer restrictionValue
) {
}
