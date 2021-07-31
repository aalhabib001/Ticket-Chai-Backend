package tech.ticketchai.TicketChaiBackend.jwt.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.Repository.ProfileRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class EmailSmsService {
//    private final JavaMailSender javaMailSender;
    private final ProfileRepository profileRepository;

//    public String sendMail(String sendTo, String subject, String body) throws MessagingException, IOException {
//
//        MimeMessage msg = javaMailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//        helper.setFrom("dokaneeteam@gmail.com", "Utopian Team");
//
//        helper.setTo(sendTo);
//
//        helper.setSubject(subject);
//
//        helper.setText(body, true);
//
//        javaMailSender.send(msg);
//        return "Send to " + sendTo;
//    }

    public boolean sendSms(String sendTo, String text) {
        RestTemplate restTemplate = new RestTemplate();

        String apiKey = "C200842660dda1fc0640f5.80024956";
        String senderId = "8809612436500";
        String apiUrl = "http://esms.dianahost.com/smsapi";

        String finalUrl = apiUrl + "?api_key=" + apiKey + "&type=text&contacts=" + sendTo +
                "&senderid=" + senderId + "&msg=" + text;

//        System.out.println(finalUrl);

        ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);

        System.out.println(response);

        if (response.getStatusCodeValue() != 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a problem on SMS Server. " + response.getStatusCodeValue());
        }

        if (response.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is a problem on SMS Server");
        }

        String responseText = response.getBody();

        if (responseText.contains("SMS SUBMITTED:")) {
            return true;
        } else if (Objects.equals(response.getBody(), "1003")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SMS API Not Found.");
        } else if (Objects.equals(response.getBody(), "1004")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are trying to send too much sms. Spam Detected");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a problem on sms server. Code: " + responseText);
        }
    }

    public ResponseEntity<ApiResponse<String>> generateOtpAndSend(Optional<ProfileModel> profileModelOptional) {
        if (profileModelOptional.isPresent()) {

            ProfileModel profileModel = profileModelOptional.get();

            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            int otp = Integer.parseInt(String.format("%06d", number));

            profileModel.setOtp(otp);
            profileModel.setOtpCreatedOn(System.currentTimeMillis());

            profileRepository.save(profileModel);

            if (!profileModel.getPhoneNo().isEmpty()) {
                String text = "Hello, " + profileModel.getName() + ".\nYour OTP is: " +
                        otp + ".\nThanks, Ticket-Chai.Tech";

                boolean isSend = sendSms(profileModel.getPhoneNo(), text);

                if (isSend) {
                    return new ResponseEntity<>(new ApiResponse<>(201, "OTP Generated", null), HttpStatus.CREATED);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a problem on OTP.");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Phone No Found on Profile. Please Add One First");
            }


        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found");
        }
    }
}
