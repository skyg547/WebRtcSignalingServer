package com.webrtc.webrtcsignalingserver.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class OAuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/oauth")
    public String oauthPage(){

        return "templates/thymeleaf/Oauth.html";
    }

    @RequestMapping("/user")
    @ResponseBody
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal){

        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

}
