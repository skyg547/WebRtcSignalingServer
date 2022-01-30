package com.webrtc.webrtcsignalingserver.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping
    public String oauthPage(){

        return "templates/thymeleaf/Oauth.html";
    }

}
