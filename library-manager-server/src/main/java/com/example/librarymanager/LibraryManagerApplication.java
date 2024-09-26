package com.example.librarymanager;

import com.example.librarymanager.config.CloudinaryConfig;
import com.example.librarymanager.config.MailConfig;
import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.service.ReaderService;
import com.example.librarymanager.service.RoleService;
import com.example.librarymanager.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SpringBootApplication
@EnableConfigurationProperties({
        AdminInfo.class,
        MailConfig.class,
        CloudinaryConfig.class
})
@EnableScheduling
public class LibraryManagerApplication {

    RoleService roleService;

    UserService userService;

    ReaderService readerService;

    public static void main(String[] args) {
        Environment env = SpringApplication.run(LibraryManagerApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START {} Application------------------------------", appName);
        log.info("   Application         : {}", appName);
        log.info("   Url swagger-ui      : http://localhost:{}/swagger-ui.html", port);
        log.info("-------------------------START SUCCESS {} Application----------------------", appName);
    }

    @Bean
    CommandLineRunner init(AdminInfo adminInfo) {
        return args -> {
            roleService.initRoles();
            userService.initAdmin(adminInfo);
            readerService.initReaders();
        };
    }

}
