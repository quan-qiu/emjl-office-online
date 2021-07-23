package net.csdcodes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;

@ControllerAdvice
public class BuildPropertiesController {

    Logger logger = LoggerFactory.getLogger(BuildPropertiesController.class);

    @Autowired
    private Environment env;

    @ModelAttribute("isDev")
    public boolean isProd() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }
}
