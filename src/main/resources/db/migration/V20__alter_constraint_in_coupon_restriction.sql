ALTER TABLE coupon_restrictions
    DROP CONSTRAINT coupon_restrictions_restriction_type_check,
    ADD CONSTRAINT coupon_restrictions_restriction_type_check
        CHECK ( restriction_type IN ('STORE', 'PRODUCT', 'CATEGORY', 'CUSTOMER') );