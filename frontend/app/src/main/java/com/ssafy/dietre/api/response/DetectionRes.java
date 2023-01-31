package com.ssafy.dietre.api.response;

import java.util.List;

public class DetectionRes {
    public List<String> getDetected() {
        return detected;
    }

    public void setDetected(List<String> detected) {
        this.detected = detected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    List<String> detected;
    String path;
}
