ALTER TABLE orders
    DROP CONSTRAINT orders_status_check,
    ADD CONSTRAINT orders_status_check
        check (status IN ('PENDING', 'FAILED', 'CANCELLED', 'PROCESSING',
                          'REFUNDED', 'RETURNED', 'OUT_FOR_DELIVERY',
                          'DELIVERED'));