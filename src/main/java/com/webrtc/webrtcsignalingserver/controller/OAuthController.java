package com.webrtc.webrtcsignalingserver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    @RequestMapping
    public String oauthPage(){

        return "/webapp/WEB-INF/views/Oauth.html";
    }

}
