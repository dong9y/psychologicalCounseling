package com.ruoyi.system.domain.vo;

import java.util.List;
import com.ruoyi.system.domain.PsyWarning;

public class PsyCounselorDashboardVo
{
    private Long todayAppointmentCount;

    private Long pendingAppointmentCount;

    private Long highRiskWarningCount;

    private Long weekCounselingCount;

    private List<PsyWarning> recentWarnings;

    public Long getTodayAppointmentCount()
    {
        return todayAppointmentCount;
    }

    public void setTodayAppointmentCount(Long todayAppointmentCount)
    {
        this.todayAppointmentCount = todayAppointmentCount;
    }

    public Long getPendingAppointmentCount()
    {
        return pendingAppointmentCount;
    }

    public void setPendingAppointmentCount(Long pendingAppointmentCount)
    {
        this.pendingAppointmentCount = pendingAppointmentCount;
    }

    public Long getHighRiskWarningCount()
    {
        return highRiskWarningCount;
    }

    public void setHighRiskWarningCount(Long highRiskWarningCount)
    {
        this.highRiskWarningCount = highRiskWarningCount;
    }

    public Long getWeekCounselingCount()
    {
        return weekCounselingCount;
    }

    public void setWeekCounselingCount(Long weekCounselingCount)
    {
        this.weekCounselingCount = weekCounselingCount;
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
