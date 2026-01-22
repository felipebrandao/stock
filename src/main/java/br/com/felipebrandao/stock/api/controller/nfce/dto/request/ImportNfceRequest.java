package br.com.felipebrandao.stock.api.controller.nfce.dto.request;

import lombok.Data;

@Data
public class ImportNfceRequest {

    private String qrCodeUrl;
    private String accessKey;
}
