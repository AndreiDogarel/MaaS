package com.example.maas.service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OCRService {

    public String extractTextFromBytes(byte[] data) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        Image img = Image.newBuilder()
                .setContent(ByteString.copyFrom(data))
                .build();

        Feature feat = Feature.newBuilder()
                .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                .build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        requests.add(request);

        // Apelăm Vision API
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = batchResponse.getResponsesList();

            if (responses.isEmpty()) {
                return "";
            }

            AnnotateImageResponse response = responses.get(0);

            if (response.hasError()) {
                throw new IOException("Error during OCR: " + response.getError().getMessage());
            }

            return response.getFullTextAnnotation().getText();
        }
    }
}