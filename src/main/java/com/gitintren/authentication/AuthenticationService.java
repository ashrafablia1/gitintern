package com.gitintren.authentication;

import org.springframework.beans.factory.annotation.Value;
import com.gitintren.config.JwtService;
import com.gitintren.dto.CompanyDto;
import com.gitintren.dto.InternDto;
import com.gitintren.model.CompanyUser;
import com.gitintren.model.InternUser;
import com.gitintren.model.Role;
import com.gitintren.model.User;
import com.gitintren.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${custom.base-url}") private String baseUrl;

    @Value("${spring.mail.username}") private String sender;


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final EmailSenderService emailSenderService;


    public AuthenticationResponse register(CompanyDto companyDto) {
        User user = createCompanyUser(companyDto);
        userRepository.save(user);
        return generateAuthenticationResponseJwt(user);
    }

    public AuthenticationResponse register(InternDto internDto) {
        User user = createInternUser(internDto);
        userRepository.save(user);
        return generateAuthenticationResponseJwt(user);
    }

    private User createInternUser(InternDto internDto) {
        var internUser = InternUser.builder().firstName(internDto.getFirstName()).lastName(internDto.getLastName()).build();
        var user = User.builder().email(internDto.getEmail()).password(passwordEncoder.encode(internDto.getPassword())).role(Role.INTERN).build();
        internUser.setUser(user);
        user.setInternUser(internUser);
        return user;
    }

    private User createCompanyUser(CompanyDto companyDto) {
        var companyUser = CompanyUser.builder().companyName(companyDto.getName()).build();
        var user = User.builder().email(companyDto.getEmail()).password(passwordEncoder.encode(companyDto.getPassword())).role(Role.COMPANY).build();
        user.isEnabled();
        companyUser.setUser(user);
        user.setCompanyUser(companyUser);
        return user;
    }

    public AuthenticationResponse authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return generateAuthenticationResponseJwt(user);
    }

    private AuthenticationResponse generateAuthenticationResponseJwt(User user) {
        Map<String, Object> roles = Map.of("role", user.getRole());
        var jwtToken = jwtService.generateToken(roles, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public boolean confirmToken(String token) {
        String email = jwtService.extractEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtService.isTokenValid(token, userDetails);
    }

    public void enableUser(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteUser(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("GitIntern, Confirmation Link!");
        mailMessage.setFrom(sender);
        mailMessage.setText("Thank you for registering. Please click on the below link to activate your account. "
                + baseUrl + "/api/registration/confirm?token=" + token);
        emailSenderService.sendEmail(mailMessage);
    }

    public boolean checkIfUserIsEnabled(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.isEnabled();
    }

    public boolean checkIfUserIsCompany(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole().equals(Role.COMPANY);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {return null;}
    }
}
