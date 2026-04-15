package it.unipi.Server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe di configurazione per Swagger. 
 * http://localhost:8081/swagger-ui/index.html
 * Mi è stato utile in fase di testing del server prima di implementare
 * il client e ho deciso di lasciarlo anche se superfluo.
 * @author lucamaffioli
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("myTokenSecurity", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY) 
                                        .in(SecurityScheme.In.HEADER)     
                                        .name("Authorization")))          
                .addSecurityItem(new SecurityRequirement().addList("myTokenSecurity"));
    }
}