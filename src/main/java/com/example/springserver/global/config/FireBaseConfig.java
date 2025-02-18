package com.example.springserver.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FireBaseConfig {

    @Value("${firebase.project_id}")
    private String projectId;

    @Value("${firebase.private_key_id}")
    private String privateKeyId;

    @Value("${firebase.private_key}")
    private String privateKey;

    @Value("${firebase.client_email}")
    private String clientEmail;

    @Value("${firebase.client_id}")
    private String clientId;

    @Value("${firebase.auth_uri}")
    private String authUri;

    @Value("${firebase.token_uri}")
    private String tokenUri;

    @Value("${firebase.auth_uri}")
    private String authProviderCertUrl;

    @Value("${firebase.client_id}")
    private String clientCertUrl;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        String firebaseConfig = String.format(
            "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"%s\",\n" +
                "  \"private_key_id\": \"%s\",\n" +
                "  \"private_key\": \"%s\",\n" +
                "  \"client_email\": \"%s\",\n" +
                "  \"client_id\": \"%s\",\n" +
                "  \"auth_uri\": \"%s\",\n" +
                "  \"token_uri\": \"%s\",\n" +
                "  \"auth_provider_x509_cert_url\": \"%s\",\n" +
                "  \"client_x509_cert_url\": \"%s\"\n" +
                "}",
            projectId,
            privateKeyId,
            privateKey.replace("\\n", "\n"),
            clientEmail,
            clientId,
            authUri,
            tokenUri,
            authProviderCertUrl,
            clientCertUrl
        );

        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setProjectId(projectId)
                .build();
            FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }
}
