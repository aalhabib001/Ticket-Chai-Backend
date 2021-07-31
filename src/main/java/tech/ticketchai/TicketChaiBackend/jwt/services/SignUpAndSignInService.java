package tech.ticketchai.TicketChaiBackend.jwt.services;

import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.Repository.ProfileRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.GenerateOTPRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.LoginForm;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.PassChangeRequest;
import tech.ticketchai.TicketChaiBackend.jwt.dto.request.SignUpForm;
import tech.ticketchai.TicketChaiBackend.jwt.dto.response.JwtResponse;
import tech.ticketchai.TicketChaiBackend.jwt.model.RegistrationOtpModel;
import tech.ticketchai.TicketChaiBackend.jwt.model.Role;
import tech.ticketchai.TicketChaiBackend.jwt.model.RoleName;
import tech.ticketchai.TicketChaiBackend.jwt.model.UserModel;
import tech.ticketchai.TicketChaiBackend.jwt.repository.RegistrationOtpRepository;
import tech.ticketchai.TicketChaiBackend.jwt.repository.RoleRepository;
import tech.ticketchai.TicketChaiBackend.jwt.repository.UserRepository;
import tech.ticketchai.TicketChaiBackend.jwt.security.jwt.JwtProvider;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@AllArgsConstructor
@Service
public class SignUpAndSignInService {

    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final EmailSmsService emailSmsService;
    private final RegistrationOtpRepository registrationOtpRepository;

    public ResponseEntity<ApiResponse<String>> signUp(@Valid SignUpForm signUpRequest) {

        String email = signUpRequest.getEmail();
        String userName = signUpRequest.getUserName();
        String phoneNo = signUpRequest.getPhoneNo();

        if (EmailValidator.getInstance().isValid(email)) {
            if (profileRepository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please Enter a Valid Email");
        }

        if (profileRepository.existsByUsername(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Exists.");
        }

        if (profileRepository.existsByPhoneNo(phoneNo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Already Exists.");
        }

        Optional<RegistrationOtpModel> registrationOtpModelOptional =
                registrationOtpRepository.findByPhoneNo(signUpRequest.getPhoneNo());
        if (registrationOtpModelOptional.isPresent()) {
            RegistrationOtpModel registrationOtpModel = registrationOtpModelOptional.get();

            if (!registrationOtpModel.getOtp().equals(signUpRequest.getOtp())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Matched");
            } else {
                if ((registrationOtpModel.getCreatedOn() + 300000) < System.currentTimeMillis()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Expired");
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Otp Generated with this phone No");
        }

        Set<String> rolesString = new HashSet<>();
        rolesString.add("USER");

        ProfileModel profileModel = ProfileModel.builder()
                .id(UUID.randomUUID().toString())
                .name(signUpRequest.getName())
                .createdOn(System.currentTimeMillis())
                .phoneNo(phoneNo)
                .username(userName)
                .email(email)
                .isDeleted(false)
                .isActive(true)
                .otp(0)
                .build();

        UserModel userModel = UserModel.builder()
                .id(UUID.randomUUID().toString())
                .username(userName)
                .email(email)
                .roles(getRolesFromStringRoles(rolesString))
                .profileModel(profileModel)
                .password(encoder.encode(signUpRequest.getPassword()))
                .isDeleted(false)
                .build();

        profileModel.setUserModel(userModel);


        profileRepository.save(profileModel);

        String text = "Hello, " + profileModel.getName() + ".\nYour Username is: " + profileModel.getUsername() +
                "\nYour Password is: " + signUpRequest.getPassword() + "\nThanks, Ticket-Chai.Tech.";

        boolean isSend = emailSmsService.sendSms(profileModel.getPhoneNo(), text);

        if (isSend) {
            return new ResponseEntity<>(new ApiResponse<String>(201, "Account Created", userName), HttpStatus.CREATED);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Is not sent");
        }

    }

    public ResponseEntity<ApiResponse<JwtResponse>> signIn(LoginForm loginRequest) {

        if (!userRepository.existsByUsernameAndIsDeleted(loginRequest.getUsername(), false)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Id Not Found");
        }

        if (profileRepository.existsByUsernameAndIsDeleted(loginRequest.getUsername(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is Banned Permanently");
        }
        if (profileRepository.existsByUsernameAndIsActive(loginRequest.getUsername(), false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is Temporary Banned");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new ResponseEntity<>(new ApiResponse<>(200, "Login Successful",
                new JwtResponse(jwt, "Bearer")), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<String>> changePassword(String jwtToken, PassChangeRequest passChangeRequest) {
        String userName = jwtProvider.getUserNameFromJwt(jwtToken);
        Optional<UserModel> userModelOptional = userRepository.findByUsername(userName);

        if (userModelOptional.isPresent()) {
            UserModel userModel = userModelOptional.get();

            if (encoder.matches(passChangeRequest.getOldPass(), userModel.getPassword())) {
                userModel.setPassword(encoder.encode(passChangeRequest.getNewPass()));

                userRepository.save(userModel);

                return new ResponseEntity<>(new ApiResponse<>(200,
                        "Password Change Successful", null), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password Doesn't match");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No User Found");
        }
    }

    public Set<Role> getRolesFromStringRoles(Set<String> roles2) {
        Set<Role> roles = new HashSet<>();
        for (String role : roles2) {
            Optional<Role> roleOptional = roleRepository.findByName(RoleName.valueOf(role));
            if (roleOptional.isEmpty()) {
                throw new ValidationException("Role '" + role + "' does not exist.");
            }
            roles.add(roleOptional.get());
        }
        return roles;
    }

    private Set<String> getRolesStringFromRole(Set<Role> roles2) {
        Set<String> roles = new HashSet<>();
        for (Role role : roles2) {
            roles.add(role.getName().toString());
        }
        return roles;
    }

    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailable(String username) {
        if (profileRepository.existsByUsername(username)) {
            return new ResponseEntity<>(new ApiResponse<>(400, "Username is not available", false), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(200, "Username is available", true), HttpStatus.OK);
        }
    }

//    public ResponseEntity<ApiResponse<String>> deleteUser(String userName) {
//        Optional<UserModel> userModelOptional = userRepository.findByUsername(userName);
//        if (userModelOptional.isPresent()) {
//            UserModel userModel = userModelOptional.get();
//
//            ProfileModel profileModel = userModel.getProfileModel();
//            ProfileModel previousGen = profileModel.getPreviousGen();
//
//            previousGen.getNextGens().remove(profileModel);
//            profileRepository.save(previousGen);
//
//            profileModel.setNextGens(null);
//            profileModel.setPreviousGen(null);
//
//            userModel = userRepository.save(userModel);
//            profileModel = userModel.getProfileModel();
//            userRepository.delete(userModel);
//            profileRepository.delete(profileModel);
//
//            return new ResponseEntity<>(new ApiResponse<>(200, "Deleted", null), HttpStatus.OK);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
//        }
//    }

    public ResponseEntity<ApiResponse<String>> generateSignUpOtp(GenerateOTPRequest generateOTPRequest) {
        if (profileRepository.existsByPhoneNo(generateOTPRequest.getPhoneNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone No Already Exists");
        }

        Optional<RegistrationOtpModel> registrationOtpModelOptional =
                registrationOtpRepository.findByPhoneNo(generateOTPRequest.getPhoneNo());
        RegistrationOtpModel registrationOtpModel = null;

        if (registrationOtpModelOptional.isPresent()) {
            registrationOtpModel = registrationOtpModelOptional.get();
        } else {
            registrationOtpModel = new RegistrationOtpModel();
            registrationOtpModel.setId(UUID.randomUUID().toString());
        }

        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        int otp = Integer.parseInt(String.format("%06d", number));

        registrationOtpModel.setOtp(otp);
        registrationOtpModel.setCreatedOn(System.currentTimeMillis());
        registrationOtpModel.setPhoneNo(generateOTPRequest.getPhoneNo());

        String text = "Hello, Your OTP for Registration in Ticket-Chai.Tech is: " + otp + ".\nThanks";
        boolean isSent = emailSmsService.sendSms(generateOTPRequest.getPhoneNo(), text);

        registrationOtpRepository.save(registrationOtpModel);

        if (isSent) {
            return new ResponseEntity<>(new ApiResponse<>(200, "Otp Sent", null), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Is not sent");
        }
    }

    public ResponseEntity<ApiResponse<String>> banUser(String userName) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByUsername(userName);
        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            if (profileModel.getIsDeleted() != null && profileModel.getIsDeleted()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Profile is already Banned");
            }

            profileModel.setIsDeleted(true);
            profileModel.setIsActive(false);

            profileRepository.save(profileModel);

            return new ResponseEntity<>(new ApiResponse<>(200, "User is permanently banned", null),
                    HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found");
        }
    }

    public ResponseEntity<ApiResponse<String>> tempBanUser(String userName) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByUsername(userName);
        if (profileModelOptional.isPresent()) {
            ProfileModel profileModel = profileModelOptional.get();

            if (!profileModel.getIsActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is already Temporary Banned");
            }

            profileModel.setIsActive(false);

            profileRepository.save(profileModel);

            return new ResponseEntity<>(new ApiResponse<>(200, "User is Temporary banned", null),
                    HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found");
        }
    }
}
