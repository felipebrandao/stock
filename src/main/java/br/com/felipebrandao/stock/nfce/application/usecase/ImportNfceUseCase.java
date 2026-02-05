package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.application.service.AsyncNfceImportProcessorService;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportNfceUseCase {

    private final NfceImportRepository repository;
    private final AsyncNfceImportProcessorService asyncProcessor;

    private static final Pattern ACCESS_KEY_PATTERN = Pattern.compile("\\b\\d{44}\\b");

    public UUID execute(String qrCodeUrl) {

        String accessKey = extractAccessKey(qrCodeUrl);

        NfceImport importJob = NfceImport.createPending(
                qrCodeUrl,
                accessKey
        );

        UUID importId = saveImport(importJob);

        log.info("[import-nfce] Importação criada com sucesso. id={} accessKey={}", importId, accessKey);

        asyncProcessor.processAsync(importId);
        log.info("[import-nfce] Processamento assíncrono disparado para id={}", importId);

        return importId;
    }

    @Transactional
    protected UUID saveImport(NfceImport importJob) {
        NfceImport saved = repository.save(importJob);

        return saved.getId();
    }

    private String extractAccessKey(String qrCodeUrl) {
        if (qrCodeUrl == null || qrCodeUrl.isBlank()) {
            throw new BusinessException("qrCodeUrl é obrigatório");
        }

        if (qrCodeUrl.matches("\\d{44}")) {
            return qrCodeUrl;
        }

        int pIndex = qrCodeUrl.indexOf("p=");
        String candidate = pIndex >= 0 ? qrCodeUrl.substring(pIndex + 2) : qrCodeUrl;

        Matcher matcher = ACCESS_KEY_PATTERN.matcher(candidate);
        if (matcher.find()) {
            return matcher.group();
        }

        throw new BusinessException("Chave de Acesso inválida (esperado 44 dígitos)");
    }
}
