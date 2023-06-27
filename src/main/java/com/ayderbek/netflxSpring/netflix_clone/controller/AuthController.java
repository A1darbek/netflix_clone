package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.TokenResponse;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserService userService;

    @GetMapping("/home")
    public Map<String, Object> home(@AuthenticationPrincipal OidcUser principal,
                                    HttpServletRequest request) {
        OAuth2AuthorizedClient authorizedClient = null;
        if (principal != null) {
            String clientRegistrationId = "auth0";
            // replace with your client registration ID
            authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId, principal.getName());
        }

        if (authorizedClient != null) {
            Map<String, Object> attributes = new HashMap<>(principal.getAttributes());
            attributes.put("access_token", authorizedClient.getAccessToken().getTokenValue());
            if (authorizedClient.getRefreshToken() != null) {
                attributes.put("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
            }
            return attributes;
        }

        return Collections.emptyMap();
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.createUser(principal);
        model.addAttribute("name", user.getName());
        // handle the logged in user...
        return "loginSuccess";
    }

    @GetMapping("/token")
    public TokenResponse getAccessToken() {
        String url = "https://dev-oxmdtkbnab0ux3co.us.auth0.com/oauth/token";
        String clientId = "ouG7BGdSvFBoDVM1uYzMlVZEYNiOlBGd";
        String clientSecret = "6i0Hj6whJDbcrE9FkyWVyErWvceeoav1OGW79apg9NEdrBCSLIKtkc6n4CXxreYI";
        String audience = "https://dev-oxmdtkbnab0ux3co.us.auth0.com/api/v2/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("audience", audience);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TokenResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException exception) {
            throw new RuntimeException("Token is invalid");
        }
    }
}
