package com.vishal;

import com.vishal.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
    info = @Info(
            title = "Accounts Microservice REST API documentation",
            version = "v1",
            description = "Accounts Microservice",
            contact = @Contact(
                    name = "Vishal Narsinh",
                    email = "vishalnarsinh@gmail.com",
                    url = "https://github.com/vishalnarsinh"
            ),
            license = @License(
                    name = "Apache 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0"
            )
    ),
    externalDocs = @ExternalDocumentation(
            description = "Accounts Microservice Code",
            url = "https://github.com/VishalNarsinh/microservices/tree/main/accounts"
    )
)
@EnableConfigurationProperties(value = {
        AccountsContactInfoDto.class
})
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
