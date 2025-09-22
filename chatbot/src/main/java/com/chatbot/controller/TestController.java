package com.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.config.WhatsAppProperties;

@RestController
@RequestMapping("/test")
public class TestController {

    private final WhatsAppProperties props;

    public TestController(WhatsAppProperties props) {
        this.props = props;
    }

    @GetMapping("/props")
    public String testProps() {
        return "API URL = " + props.getApiUrl()
                + ", Phone ID = " + props.getPhoneNumberId();
    }
}
