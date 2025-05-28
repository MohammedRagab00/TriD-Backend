package com.gotrid.trid.api.threedModel.dto;

import com.gotrid.trid.core.threedModel.dto.ModelDTO;

import java.util.List;

public record ModelResponse(
        ModelDTO model,
        List<String> images
) {
}
