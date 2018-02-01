package pl.java.scalatech;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pl.java.scalatech.dto.RepoInfo;

public final class TestTools {

    private TestTools() {
        throw new AssertionError();
    }

    public static RepoInfo prepareRepoInfoSample(String repoName) throws MalformedURLException {
        RepoInfo expectedAnswer = new RepoInfo(repoName, "desc", new URL("http://someUrl"), 5, LocalDateTime.of(2016, 3, 3, 22, 44));
        return expectedAnswer;
    }
    
    public static JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        return javaTimeModule;        
}


}
