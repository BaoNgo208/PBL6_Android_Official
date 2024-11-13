package com.example.pbl6_android.models;

import java.util.UUID;

public class Video {

        private UUID videoId;
        private String videoUrl;
        private String description;

        // Getters and Setters
        public UUID getVideoId() {
            return videoId;
        }

        public void setVideoId(UUID videoId) {
            this.videoId = videoId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
}
