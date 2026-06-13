package dasturlash.uz.smsprovider.controller;


import dasturlash.uz.smsprovider.dto.response.Response;
import dasturlash.uz.smsprovider.integration.KunUzIntegration;
import dasturlash.uz.smsprovider.integration.SmsProviderIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    @Autowired
    private KunUzIntegration kunUzIntegration;

    @Autowired
    private SmsProviderIntegration smsProviderIntegration;

    @GetMapping("/kunuz")
    public String getNews() {
        return kunUzIntegration.fetchLatestNews();
    }

    // API endpoint endi SmsResponse o'rniga Response qaytaradi
    @PostMapping("/send-sms")
    public Response sendSms(@RequestParam String phone, @RequestParam String text) {
        return smsProviderIntegration.sendSms(phone, text);
    }
}
