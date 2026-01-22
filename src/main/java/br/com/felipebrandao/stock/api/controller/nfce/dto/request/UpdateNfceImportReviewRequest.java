package br.com.felipebrandao.stock.api.controller.nfce.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateNfceImportReviewRequest {

    private List<UpdateNfceImportReviewItemRequest> items;
}
