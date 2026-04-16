package model;

import java.sql.Timestamp;

/**
 * Model class for Registration Request
 */
public class RegistrationRequest {
    private int requestId;
    private int playerId;
    private int teamId;
    private Timestamp requestDate;
    private String status; // pending, approved, rejected, cancelled
    private Timestamp reviewedDate;
    private String rejectionReason;
    
    public RegistrationRequest(int requestId, int playerId, int teamId,
                                Timestamp requestDate, String status, 
                                Timestamp reviewedDate, String rejectionReason) {
        this.requestId = requestId;
        this.playerId = playerId;
        this.teamId = teamId;
        this.requestDate = requestDate;
        this.status = status;
        this.reviewedDate = reviewedDate;
        this.rejectionReason = rejectionReason;
    }
    
    public int getRequestId() { return requestId; }
    public int getPlayerId() { return playerId; }
    public int getTeamId() { return teamId; }
    public Timestamp getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
    public Timestamp getReviewedDate() { return reviewedDate; }
    public String getRejectionReason() { return rejectionReason; }
    
    public void setStatus(String status) { this.status = status; }
    public void setReviewedDate(Timestamp reviewedDate) { this.reviewedDate = reviewedDate; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}