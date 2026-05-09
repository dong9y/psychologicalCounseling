package com.ruoyi.system.domain.vo;

import java.util.List;
import com.ruoyi.system.domain.PsyWarning;

public class PsyAdminDashboardVo
{
    private Long studentCount;

    private Long counselorCount;

    private Long assessmentCount;

    private Long warningCount;

    private List<PsyWarning> recentWarnings;

    public Long getStudentCount()
    {
        return studentCount;
    }

    public void setStudentCount(Long studentCount)
    {
        this.studentCount = studentCount;
    }

    public Long getCounselorCount()
    {
        return counselorCount;
    }

    public void setCounselorCount(Long counselorCount)
    {
        this.counselorCount = counselorCount;
    }

    public Long getAssessmentCount()
    {
        return assessmentCount;
    }

    public void setAssessmentCount(Long assessmentCount)
    {
        this.assessmentCount = assessmentCount;
    }

    public Long getWarningCount()
    {
        return warningCount;
    }

    public void setWarningCount(Long warningCount)
    {
        this.warningCount = warningCount;
    }

    public List<PsyWarning> getRecentWarnings()
    {
        return recentWarnings;
    }

    public void setRecentWarnings(List<PsyWarning> recentWarnings)
    {
        this.recentWarnings = recentWarnings;
    }
}
