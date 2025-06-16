package org.demo.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "secure.auth")
public class AuthProperties {

    private List<String> includePaths;
    private List<String> excludePaths;

}
