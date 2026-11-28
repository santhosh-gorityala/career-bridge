package com.jobPortal.careerBridge.exception;

public class FeedbackAlreadySubmittedException extends RuntimeException {
    public FeedbackAlreadySubmittedException(String message) {
        super(message);
    }
}
