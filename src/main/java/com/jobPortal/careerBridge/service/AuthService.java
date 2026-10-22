package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.*;
import com.jobPortal.careerBridge.entity.RefreshToken;
import com.jobPortal.careerBridge.entity.Role;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.entity.UserStatus;
import com.jobPortal.careerBridge.exception.EmailAlreadyExistsException;
import com.jobPortal.careerBridge.repository.RoleRepository;
import com.jobPortal.careerBridge.repository.UserRepository;
import com.jobPortal.careerBridge.security.CustomUserDetails;
import com.jobPortal.careerBridge.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final RoleRepository roleRepository;
   private final AuthenticationManager authenticationManager;
   private final JwtService jwtService;
   private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponse register(RegistrationRequest request){
        String email = request.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email is already registered");
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = new User(
        request.getEmail(),
        passwordHash,
        request.getFirstName(),
        request.getLastName(),
        request.getPhone(),
        UserStatus.ACTIVE,
        false,
        LocalDateTime.now(),
        LocalDateTime.now()
        );
        Role jobSeekerRole = roleRepository.findByName("JOB_SEEKER").orElseThrow(
                ()-> new IllegalStateException("Default role JOB_SEEKER not found")
        );
        user.addRole(jobSeekerRole);
        userRepository.save(user);
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }

    public LoginResponse authenticate(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails =
                (CustomUserDetails)authentication.getPrincipal();
        String accessToken =
                jwtService.generateAccessToken(userDetails);
        String refreshToken =
                refreshTokenService.
                        createRefreshToken(userDetails.getUser());

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer"
        );
    }

    @Transactional
    public LoginResponse refreshAccessToken(
            RefreshTokenRequest request
    ){
        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(
                request.getRefreshToken()
        );
        // connecting validated refresh token to user
        // and generate new access token
        User user = refreshToken.getUser();
        refreshToken.revoke();

        String newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String newAccessToken =
                jwtService.generateAccessToken(userDetails);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer"
        );
    }

    @Transactional
    public void logout(RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenService
                .validateRefreshToken(
                        request.getRefreshToken()
                );
        refreshToken.revoke();
    }


}
