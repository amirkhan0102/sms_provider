package dasturlash.uz.smsprovider.integration;


import dasturlash.uz.smsprovider.dto.request.SmsRequest;
import dasturlash.uz.smsprovider.dto.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SmsProviderIntegration {

    @Autowired
    private RestTemplate restTemplate;

    private final String SMS_URL = "http://localhost:8081";

    // Qaytish turi Response klassiga o'zgartirildi
    public Response sendSms(String phoneNumber, String messageText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SmsRequest body = new SmsRequest(phoneNumber, messageText);
        HttpEntity<SmsRequest> entity = new HttpEntity<>(body, headers);

        try {
            // RestTemplate endi javobni to'g'ridan-to'g'ri Response klassiga o'giradi
            ResponseEntity<Response> response = restTemplate.postForEntity(SMS_URL, entity, Response.class);
            return response.getBody();
        } catch (Exception e) {
            // Xatolik holatida ham yangi Response qaytariladi
            return new Response(false, "SmsProvider o'chiq yoki xato: " + e.getMessage());
        }
    }
}
