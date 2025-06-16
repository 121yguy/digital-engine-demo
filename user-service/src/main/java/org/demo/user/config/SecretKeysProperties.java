package org.demo.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "secret-keys")
public class SecretKeysProperties {

    private String emailSecretKey;
    private long phoneSecretKey;

}
