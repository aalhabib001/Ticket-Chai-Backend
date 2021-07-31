package tech.ticketchai.TicketChaiBackend.jwt.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.EditProfile;
import tech.ticketchai.TicketChaiBackend.jwt.dto.response.ProfileResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.response.ReferralResponse;
import tech.ticketchai.TicketChaiBackend.jwt.services.AuthProfileService;


@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final AuthProfileService authProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getLoggedInProfile(@RequestHeader(name = "Authorization", required = true)
                                                                                   String jwtToken) {
        return authProfileService.getLoggedUserProfile(jwtToken);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> editLoggedInProfile(@RequestHeader(name = "Authorization",
            required = true) String jwtToken, @RequestBody EditProfile editProfile) {

        return authProfileService.editLoggedInProfile(jwtToken, editProfile);
    }

//    @PostMapping("/uploadProfile")
//    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(@RequestHeader(name = "Authorization",
//            required = true) String jwtToken, @RequestParam String imageLink) {
//        return authProfileService.uploadProfilePhoto(jwtToken, imageLink);
//    }

//    @GetMapping("/admin/users")
//    public ResponseEntity<ApiResponse<PaginationResponse<List<UserResponse>>>> getUserListAdmin(
//            @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "50") Integer pageSize,
//            @RequestParam(required = false) String userName, String phoneNo
//    ) {
//        return authProfileService.getUserListAdmin(pageNo, pageSize, userName, phoneNo);
//    }


}
