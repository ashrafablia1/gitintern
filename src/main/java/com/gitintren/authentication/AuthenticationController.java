package com.gitintren.authentication;

import com.gitintren.dto.CompanyDto;
import com.gitintren.dto.InternDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @GetMapping("/register/company")
    public String register(Model model) {
        CompanyDto companyDto = CompanyDto.builder().build();
        model.addAttribute("company", companyDto);
        return "register-company";
    }

    @PostMapping("/register/company")
    public String register(@Valid @ModelAttribute("company") CompanyDto company, HttpServletResponse response) {
       try {
           AuthenticationResponse jwtToken = authenticationService.register(company);
           authenticationService.sendConfirmationMail(company.getEmail(), jwtToken.getToken());
           return "redirect:/api/register/company?success=true";
       } catch (Exception e){return "redirect:/api/register/company?errorregister=true";}
    }

    @GetMapping("/register/intern")
    public String registerIntern(Model model) {
        InternDto internDto = InternDto.builder().build();
        model.addAttribute("intern", internDto);
        return "register-intern";
    }

    @PostMapping("/register/intern")
    public String registerIntern(@Valid @ModelAttribute("intern") InternDto intern, HttpServletResponse response) {
       try {
           AuthenticationResponse jwtToken = authenticationService.register(intern);
           authenticationService.sendConfirmationMail(intern.getEmail(), jwtToken.getToken());
           return "redirect:/api/register/intern?success";
       } catch (Exception e){return "redirect:/api/register/intern?errorregister=true";}
    }

    @GetMapping("/login/authenticate")
    public String login() {
        return "login";
    }

    @PostMapping("/login/authenticate")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) {
       try{
           if (!authenticationService.checkIfUserIsEnabled(email)) {return "redirect:/api/login/authenticate?notverify=true";}
        AuthenticationResponse jwtToken = authenticationService.authenticate(email, password);
           Cookie cookie = new Cookie("jwtToken", jwtToken.getToken());
           cookie.setHttpOnly(true);
           cookie.setSecure(true);
           cookie.setPath("/");
           response.addCookie(cookie);
            return "redirect:/user";
       } catch(Exception e){return "redirect:/api/login/authenticate?error=true";}
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/api/login/authenticate?logout=true";
    }

    @GetMapping("/registration/confirm")
    String confirmMail(@RequestParam("token") String token) {
        if(!authenticationService.confirmToken(token)){
            authenticationService.deleteUser(token);
            return "redirect:/api/login/authenticate?notvaildlink=true";
        }
        authenticationService.enableUser(token);
        return "redirect:/api/login/authenticate?successverify=true";
    }
}
