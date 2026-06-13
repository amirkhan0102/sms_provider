package dasturlash.uz.smsprovider.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KunUzIntegration {

    @Autowired
    private RestTemplate restTemplate;

    // Haqiqiy sayt o'rniga o'zingizning lokal loyihangiz portini yozasiz (masalan, 8082)
    private final String KUN_UZ_LOCAL_URL = "http://localhost:8080";

    public String fetchLatestNews() {
        try {
            // Lokal Kun.uz loyihasiga GET so'rovi yuboriladi
            return restTemplate.getForObject(KUN_UZ_LOCAL_URL, String.class);
        } catch (Exception e) {
            return "Lokal Kun.uz loyihasiga ulanishda xatolik: " + e.getMessage();
        }
    }
}
