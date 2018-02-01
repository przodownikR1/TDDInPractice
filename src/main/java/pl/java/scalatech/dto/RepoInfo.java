package pl.java.scalatech.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Validated
public class RepoInfo {

    @JsonProperty("full_name")
    @NotEmpty
    private String repositoryName;

    @JsonProperty("description")
    @NotEmpty
    private String desc;

    @JsonProperty("clone_url")
    @org.hibernate.validator.constraints.URL
    private URL gitCloneUrl;

    @JsonProperty("stargazers_count")
    private int starCount;

    @JsonProperty("created_at")
    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime creationTime;
}
