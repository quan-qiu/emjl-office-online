package net.csdcodes;

import net.csdcodes.handler.PrProcessRejectionHandler;
import net.csdcodes.service.EmailService;
import net.csdcodes.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import org.springframework.jdbc.core.JdbcTemplate;


@ServletComponentScan
@SpringBootApplication

public class Springbooti18nApplication implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Logger log = LoggerFactory.getLogger(Springbooti18nApplication.class);

    public static void  main(String[] args) {
        SpringApplication.run(Springbooti18nApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("EURO-MISI APP started...");
    }
}
