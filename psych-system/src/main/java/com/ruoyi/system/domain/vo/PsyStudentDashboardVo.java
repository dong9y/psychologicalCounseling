package com.ruoyi.system.domain.vo;

import java.util.List;
import com.ruoyi.system.domain.PsyEmotionRecord;
import com.ruoyi.system.domain.PsyMessage;

public class PsyStudentDashboardVo
{
    private String latestRiskLevel;

    private Long recentAssessmentCount;

    private Long pendingAppointmentCount;

    private Long unreadMessageCount;

    private PsyEmotionRecord latestEmotionRecord;

    private List<PsyMessage> recentMessages;

    public String getLatestRiskLevel()
    {
        return latestRiskLevel;
    }

    public void setLatestRiskLevel(String latestRiskLevel)
    {
        this.latestRiskLevel = latestRiskLevel;
    }

    public Long getRecentAssessmentCount()
    {
        return recentAssessmentCount;
    }

    public void setRecentAssessmentCount(Long recentAssessmentCount)
    {
        this.recentAssessmentCount = recentAssessmentCount;
    }

    public Long getPendingAppointmentCount()
    {
        return pendingAppointmentCount;
    }

    public void setPendingAppointmentCount(Long pendingAppointmentCount)
    {
        this.pendingAppointmentCount = pendingAppointmentCount;
    }

    public Long getUnreadMessageCount()
    {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Long unreadMessageCount)
    {
        this.unreadMessageCount = unreadMessageCount;
    }

    public PsyEmotionRecord getLatestEmotionRecord()
    {
        return latestEmotionRecord;
    }

    public void setLatestEmotionRecord(PsyEmotionRecord latestEmotionRecord)
    {
        this.latestEmotionRecord = latestEmotionRecord;
    }

    public List<PsyMessage> getRecentMessages()
    {
        return recentMessages;
    }

    public void setRecentMessages(List<PsyMessage> recentMessages)
    {
        this.recentMessages = recentMessages;
    }
}
