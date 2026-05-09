package com.ruoyi.system.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;

public class PsyWarning extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long warningId;

    private String warningNo;

    private Long studentUserId;

    private String studentName;

    private String warningSource;

    private Long sourceRecordId;

    private String riskLevel;

    private String status;

    private Long counselorUserId;

    private String counselorName;

    private String triggerSummary;

    private String processNote;

    private String followPlan;

    private Date handledTime;

    public Long getWarningId()
    {
        return warningId;
    }

    public void setWarningId(Long warningId)
    {
        this.warningId = warningId;
    }

    public String getWarningNo()
    {
        return warningNo;
    }

    public void setWarningNo(String warningNo)
    {
        this.warningNo = warningNo;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getWarningSource()
    {
        return warningSource;
    }

    public void setWarningSource(String warningSource)
    {
        this.warningSource = warningSource;
    }

    public Long getSourceRecordId()
    {
        return sourceRecordId;
    }

    public void setSourceRecordId(Long sourceRecordId)
    {
        this.sourceRecordId = sourceRecordId;
    }

    public String getRiskLevel()
    {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel)
    {
        this.riskLevel = riskLevel;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

    public String getTriggerSummary()
    {
        return triggerSummary;
    }

    public void setTriggerSummary(String triggerSummary)
    {
        this.triggerSummary = triggerSummary;
    }

    public String getProcessNote()
    {
        return processNote;
    }

    public void setProcessNote(String processNote)
    {
        this.processNote = processNote;
    }

    public String getFollowPlan()
    {
        return followPlan;
    }

    public void setFollowPlan(String followPlan)
    {
        this.followPlan = followPlan;
    }

    public Date getHandledTime()
    {
        return handledTime;
    }

    public void setHandledTime(Date handledTime)
    {
        this.handledTime = handledTime;
    }
}
