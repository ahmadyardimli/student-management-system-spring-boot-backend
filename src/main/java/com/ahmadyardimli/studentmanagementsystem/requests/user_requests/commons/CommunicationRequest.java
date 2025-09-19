package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons;

public class CommunicationRequest {
    private int senderId;
    private int receiverId;

    public CommunicationRequest() {
        // Default constructor
    }

    public CommunicationRequest(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}

