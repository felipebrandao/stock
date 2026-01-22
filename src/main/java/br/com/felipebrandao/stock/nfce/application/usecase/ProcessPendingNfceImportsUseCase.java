package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessPendingNfceImportsUseCase {

    private final NfceImportRepository repository;
    private final ProcessNfceImportUseCase processNfceImportUseCase;

    @Transactional
    public void execute() {

        List<NfceImport> pendings =
                repository.findNextPending(5);

        for (NfceImport nfceImport : pendings) {
            nfceImport.markProcessing();
            repository.save(nfceImport);
            processNfceImportUseCase.execute(nfceImport);
        }
    }
}
