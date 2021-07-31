package tech.ticketchai.TicketChaiBackend.jwt.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.Repository.ProfileRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.EditProfile;
import tech.ticketchai.TicketChaiBackend.jwt.dto.response.ProfileResponse;
import tech.ticketchai.TicketChaiBackend.jwt.security.jwt.JwtProvider;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthProfileService {

    private final ProfileRepository profileRepository;
    private final JwtProvider jwtProvider;

    public ResponseEntity<ApiResponse<ProfileResponse>> getLoggedUserProfile(String jwtToken) {

        String userName = jwtProvider.getUserNameFromJwt(jwtToken);


        if (profileRepository.existsByUsernameAndIsDeleted(userName, true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is Banned Permanently");
        }
        if (profileRepository.existsByUsernameAndIsActive(userName, false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is Temporary Banned");
        }

        Optional<ProfileModel> profileModelOptional = profileRepository.findByUsername(userName);

        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            ProfileResponse profileResponse = getProfileResponseFromProfileModel(profileModel);

            return new ResponseEntity<>(new ApiResponse<>(200, "User Found", profileResponse), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }

    }

    public ResponseEntity<ApiResponse<ProfileResponse>> editLoggedInProfile(String jwtToken, EditProfile editProfile) {
        String userName = jwtProvider.getUserNameFromJwt(jwtToken);
        Optional<ProfileModel> profileModelOptional = profileRepository.findByUsername(userName);

        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            if (editProfile.getEmail().equals(profileModel.getEmail())) {

            } else if (profileModel.getEmail() == null) {
                profileModel.setEmail(editProfile.getEmail());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't Change Default Email");
            }


            if (editProfile.getPhoneNo().equals(profileModel.getPhoneNo())) {

            } else if (profileModel.getPhoneNo() == null) {
                profileModel.setPhoneNo(editProfile.getPhoneNo());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't Change Default Phone No");
            }

            profileModel.setName(editProfile.getName());

            profileRepository.save(profileModel);

            ProfileResponse profileResponse = getProfileResponseFromProfileModel(profileModel);

            return new ResponseEntity<>(new ApiResponse<>(200, "Profile Edit Successful", profileResponse), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found");
        }

    }

    private ProfileResponse getProfileResponseFromProfileModel(ProfileModel profileModel) {
        return new ProfileResponse(profileModel.getName(), profileModel.getPhoneNo(),
                profileModel.getUsername(), profileModel.getEmail());
    }

//
//    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(String jwtToken, String imageLink) {
//        String username = jwtProvider.getUserNameFromJwt(jwtToken);
//
//        Optional<ProfileModel> profileModelOptional = profileRepository.findByUsername(username);
//        if (profileModelOptional.isPresent()) {
//            ProfileModel profileModel = profileModelOptional.get();
//            profileModel.setProfilePhotoLink(imageLink);
//
//            profileRepository.save(profileModel);
//
//            return new ResponseEntity<>(new ApiResponse<>(200, "Profile Photo Updated", null), HttpStatus.OK);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Profile found");
//        }
//    }


//    public ResponseEntity<ApiResponse<PaginationResponse<List<UserResponse>>>> getUserListAdmin(
//            Integer pageNo, Integer pageSize, String userName, String phoneNo) {
//        ProfileModel exampleProfile = ProfileModel.builder()
//                .username(userName)
//                .phoneNo(phoneNo)
//                .build();
//
//        Sort sort = Sort.by(Sort.Direction.ASC, "createdOn");
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//        Page<ProfileModel> profilePage = profileRepository.findAll(Example.of(exampleProfile), pageable);
//
//        List<UserResponse> userResponseList = new ArrayList<>();
//
//        for (ProfileModel profileModel : profilePage.getContent()) {
//            UserResponse userResponse = new UserResponse(
//                    profileModel.getId(), profileModel.getName(), profileModel.getCreatedOn(), profileModel.getPhoneNo(),
//                    profileModel.getUsername(), profileModel.getEmail(), profileModel.getIsDeleted(),
//                    profileModel.getIsActive(), profileModel.getReferralId(), profileModel.getUserLevel(),
//                    profileModel.getProfilePhotoLink(), profileModel.getUserPoint(), profileModel.getDirectReferAmount(), profileModel.getTeamDepositAmount(),
//                    profileModel.getTotalIncome(), profileModel.getTotalDeposit(), profileModel.getTotalWithdrawal(),
//                    profileModel.getAdIncome(), profileModel.getIncentiveIncome(), profileModel.getTeamIncentiveIncome(),
//                    profileModel.getGenerationIncome(), profileModel.getSalaryIncome(),
//                    profileModel.getDepositInsuranceAmount(), profileModel.getWithdrawInsuranceAmount(),
//                    profileModel.getNewAccountIncome()
//            );
//
//            userResponseList.add(userResponse);
//        }
//
//        PaginationResponse<List<UserResponse>> paginationResponse = new PaginationResponse<>(
//                pageSize, pageNo, profilePage.getContent().size(), profilePage.isLast(), profilePage.getTotalElements(),
//                profilePage.getTotalPages(), userResponseList
//        );
//
//        if (profilePage.isEmpty()) {
//            return new ResponseEntity<>(new ApiResponse<>(404, "User Not Found", paginationResponse),
//                    HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>(new ApiResponse<>(200, "User Found", paginationResponse),
//                    HttpStatus.OK);
//        }
//    }


}
