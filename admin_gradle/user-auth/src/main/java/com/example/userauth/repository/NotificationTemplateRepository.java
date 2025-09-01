package com.example.userauth.repository;

import com.example.userauth.model.NotificationTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

@Repository
public class NotificationTemplateRepository {

    private static final String COLLECTION_NAME = "notification_templates";

    public List<QueryDocumentSnapshot> findTemplates(String type) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = type == null || type.equals("all")
                ? collection
                : collection.whereEqualTo("type", type);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return querySnapshot.get().getDocuments();
    }
}