package hr.filip.BankingSystem;

import hr.filip.BankingSystem.service.ImportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CsvImporter implements CommandLineRunner {

    private ImportingService importingService;

    @Autowired
    public CsvImporter(ImportingService importingService) {
        this.importingService = importingService;
    }

    @Override
    public void run(String... args) throws Exception {
        importingService.readCsv();
    }
}