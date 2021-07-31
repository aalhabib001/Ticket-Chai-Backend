package tech.ticketchai.TicketChaiBackend.jwt.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.LoginForm;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.PassChangeRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.SignUpForm;
import tech.ticketchai.TicketChaiBackend.jwt.dto.response.JwtResponse;
import tech.ticketchai.TicketChaiBackend.jwt.services.SignUpAndSignInService;

import javax.validation.Valid;

@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SignUpAndSignInService signUpAndSignInService;

    @PostMapping("/signUp/generateOtp")
    public ResponseEntity<ApiResponse<String>> generateSignUpOtp(@RequestBody GenerateOTPRequest phoneNo){
        return signUpAndSignInService.generateSignUpOtp(phoneNo);
    }

    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<String>> signUpUser(@Valid @RequestBody SignUpForm signUpRequest) {
        return signUpAndSignInService.signUp(signUpRequest);
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponse>> signInUser(@Valid @RequestBody LoginForm loginRequest) {
        return signUpAndSignInService.signIn(loginRequest);
    }


    @PutMapping("/profile/changePass")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestHeader(name = "Authorization",
            required = true) String jwtToken, @Valid @RequestBody PassChangeRequest passChangeRequest) {

        return signUpAndSignInService.changePassword(jwtToken, passChangeRequest);
    }

    @GetMapping("/check/username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailable(@PathVariable String username) {
        return signUpAndSignInService.checkUsernameAvailable(username);
    }

    @PostMapping("/admin/ban/{userName}")
    public ResponseEntity<ApiResponse<String>> banUser(@PathVariable String userName){
        return signUpAndSignInService.banUser(userName);
    }

    @PostMapping("/admin/tempBan/{userName}")
    public ResponseEntity<ApiResponse<String>> tempBanUser(@PathVariable String userName){
        return signUpAndSignInService.tempBanUser(userName);
    }

//    @GetMapping("/superAdmin/delete/{userName}")
//    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String userName) {
//        return signUpAndSignInService.deleteUser(userName);
//    }

    @GetMapping("/serverCheck")
    public String getServerStatStatus() {
        return "The Server is Running";
    }


}
