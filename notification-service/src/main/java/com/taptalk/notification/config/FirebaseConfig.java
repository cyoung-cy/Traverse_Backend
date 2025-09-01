package com.taptalk.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        System.out.println("FirebaseConfig: firebaseApp() 메소드 호출됨");
        
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (firebaseApps.isEmpty()) {
            try {
                System.out.println("FirebaseConfig: firebase-credentials.json 파일 로드 시도");
                
                // Make sure 'firebase-credentials.json' is in 'src/main/resources'
                ClassPathResource resource = new ClassPathResource("firebase-credentials.json");
                
                System.out.println("FirebaseConfig: 리소스 존재 여부: " + resource.exists());
                System.out.println("FirebaseConfig: 리소스 파일명: " + resource.getFilename());
                
                InputStream serviceAccount = resource.getInputStream();
                
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    // 프로젝트 ID에 따라 데이터베이스 URL 등을 설정할 수 있습니다.
                    // .setDatabaseUrl("https://<YOUR_PROJECT_ID>.firebaseio.com")
                    .build();

                System.out.println("FirebaseConfig: FirebaseApp 초기화 시도");
                FirebaseApp app = FirebaseApp.initializeApp(options);
                System.out.println("FirebaseConfig: FirebaseApp 초기화 성공 - " + app.getName());
                
                return app;
                
            } catch (Exception e) {
                System.err.println("FirebaseConfig: FirebaseApp 초기화 중 오류 발생");
                e.printStackTrace();
                throw new RuntimeException("Firebase 초기화 실패", e);
            }
        } else {
            System.out.println("FirebaseConfig: 기존 FirebaseApp 인스턴스 사용");
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        System.out.println("FirebaseConfig: firebaseMessaging() 메소드 호출됨");
        try {
            FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
            System.out.println("FirebaseConfig: FirebaseMessaging 인스턴스 생성 성공");
            return messaging;
        } catch (Exception e) {
            System.err.println("FirebaseConfig: FirebaseMessaging 생성 중 오류 발생");
            e.printStackTrace();
            throw new RuntimeException("FirebaseMessaging 생성 실패", e);
        }
    }
} 