package com.ps.productservice.model.dto.common;

import com.ps.productservice.model.dto.ProductResponseDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String statusCode;
    private String statusMsg;
    private ProductResponseDTO responseDTO;
}
