package tech.ticketchai.TicketChaiBackend.jwt.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.Repository.ProfileRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest1;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest2;
import tech.ticketchai.TicketChaiBackend.jwt.model.UserModel;
import tech.ticketchai.TicketChaiBackend.jwt.repository.UserRepository;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import java.util.Optional;


@Service
@AllArgsConstructor

public class ForgetPasswordService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final EmailSmsService emailSmsService;
    PasswordEncoder encoder;

    public ResponseEntity<ApiResponse<String>> generateOTP(GenerateOTPRequest generateOTPRequest) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByPhoneNo(generateOTPRequest.getPhoneNo());

        System.out.println(1);

        return emailSmsService.generateOtpAndSend(profileModelOptional);
    }


    public ResponseEntity<ApiResponse<String>> verifyOTP(GenerateOTPRequest1 generateOTPRequest) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByPhoneNo(generateOTPRequest.getPhoneNo());

        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            if (profileModel.getOtp() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Otp Found");
            } else if (!profileModel.getOtp().equals(generateOTPRequest.getOtp())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Matched");
            } else {
                if ((profileModel.getOtpCreatedOn() + 300000) < System.currentTimeMillis()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Expired");
                } else {
                    return new ResponseEntity<>(new ApiResponse<>(200, "Otp Matched", null), HttpStatus.OK);
                }
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Problem");
        }
    }

    public ResponseEntity<ApiResponse<String>> forgetPassChange(GenerateOTPRequest2 generateOTPRequest) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByPhoneNo(generateOTPRequest.getPhoneNo());

        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            if (profileModel.getOtp() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Otp Found");
            } else if (!profileModel.getOtp().equals(generateOTPRequest.getOtp())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Matched");
            } else {
                if ((profileModel.getOtpCreatedOn() + 300000) < System.currentTimeMillis()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Expired");
                }
            }

            Optional<UserModel> userModelOptional = userRepository.findByUsername(profileModel.getUsername());
            if (userModelOptional.isPresent()) {
                UserModel user = userModelOptional.get();

                user.setPassword(encoder.encode(generateOTPRequest.getNewPassword()));

                userRepository.save(user);

                profileModel.setOtp(0);

                profileRepository.save(profileModel);

                String text = "Hello, " + profileModel.getName() + ".\nYour password is Changed. If you didn't do this then"
                        + " contact with us immediately.\nThanks, UtopiansGlobal.";

//                emailSmsService.sendSms(profileModel.getPhoneNo(), text);


                return new ResponseEntity<>(new ApiResponse<>(200, "Password Changed", null),
                        HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Problem");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Problem");
        }

    }
}