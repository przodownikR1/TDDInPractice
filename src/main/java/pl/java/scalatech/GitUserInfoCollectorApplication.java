package pl.java.scalatech;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class GitUserInfoCollectorApplication {

    public static void main(String[] args) {
        springPIDAppRun(args, GitUserInfoCollectorApplication.class);
    }

    private static void springPIDAppRun(String[] args, Class<?> clazz) {
        SpringApplication springApplication = new SpringApplication(clazz);
        springApplication.addListeners(new ApplicationPidFileWriter());
        Environment environment = springApplication.run(args).getEnvironment();

        log.info("\n----------------------------------------------------------\n" +
                "\tApplication '{}' is running! Access URLs:\n" +
                "\tLocal: \t\thttp://127.0.0.1:{}\n" +
                "\tActive Profiles: {}\n" +
                "----------------------------------------------------------",
                environment.getProperty("spring.application.name"),
                environment.getProperty("server.port"),
                environment.getActiveProfiles());
    }

}
