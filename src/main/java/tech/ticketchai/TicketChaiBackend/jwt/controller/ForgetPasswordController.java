package tech.ticketchai.TicketChaiBackend.jwt.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest1;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest2;
import tech.ticketchai.TicketChaiBackend.jwt.services.ForgetPasswordService;

@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/forgetPass")
public class ForgetPasswordController {
    private final ForgetPasswordService forgetPasswordService;

    @PostMapping("/generateOtp")
    public ResponseEntity<ApiResponse<String>> generateOTP(@RequestBody GenerateOTPRequest generateOTPRequest) {

        return forgetPasswordService.generateOTP(generateOTPRequest);
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<ApiResponse<String>> verifyOTP(@RequestBody GenerateOTPRequest1 generateOTPRequest) {
        return forgetPasswordService.verifyOTP(generateOTPRequest);
    }

    @PostMapping("/changePass")
    public ResponseEntity<ApiResponse<String>> forgetPassChange(@RequestBody GenerateOTPRequest2 generateOTPRequest) {
        return forgetPasswordService.forgetPassChange(generateOTPRequest);
    }
}
