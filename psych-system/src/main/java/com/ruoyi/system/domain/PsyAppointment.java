package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;

public class PsyAppointment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long appointmentId;

    private Long studentUserId;

    private Long counselorUserId;

    private String counselorName;

    private String counselorTitle;

    private Date appointmentTime;

    private String topic;

    private String problemDescription;

    private String status;

    private String reviewNote;

    private String counselingSummary;

    private String evaluationContent;

    private BigDecimal evaluationScore;

    public Long getAppointmentId()
    {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId)
    {
        this.appointmentId = appointmentId;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public Long getCounselorUserId()
    {
        return counselorUserId;
    }

    public void setCounselorUserId(Long counselorUserId)
    {
        this.counselorUserId = counselorUserId;
    }

    public String getCounselorName()
    {
        return counselorName;
    }

    public void setCounselorName(String counselorName)
    {
        this.counselorName = counselorName;
    }

    public String getCounselorTitle()
    {
        return counselorTitle;
    }

    public void setCounselorTitle(String counselorTitle)
    {
        this.counselorTitle = counselorTitle;
    }

    public Date getAppointmentTime()
    {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime)
    {
        this.appointmentTime = appointmentTime;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getProblemDescription()
    {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription)
    {
        this.problemDescription = problemDescription;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getReviewNote()
    {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote)
    {
        this.reviewNote = reviewNote;
    }

    public String getCounselingSummary()
    {
        return counselingSummary;
    }

    public void setCounselingSummary(String counselingSummary)
    {
        this.counselingSummary = counselingSummary;
    }

    public String getEvaluationContent()
    {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent)
    {
        this.evaluationContent = evaluationContent;
    }

    public BigDecimal getEvaluationScore()
    {
        return evaluationScore;
    }

    public void setEvaluationScore(BigDecimal evaluationScore)
    {
        this.evaluationScore = evaluationScore;
    }
}
