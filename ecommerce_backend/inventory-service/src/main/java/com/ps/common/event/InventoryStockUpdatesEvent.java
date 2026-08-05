package com.ps.common.event;

import lombok.*;

@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class InventoryStockUpdatesEvent {
    private String orderId;
    private String action;
    private Long productId;
    private Integer quantity;
}
