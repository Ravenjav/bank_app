package by.itc.bank_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/applications")
    public String index() {
        return "applications";
    }
}
