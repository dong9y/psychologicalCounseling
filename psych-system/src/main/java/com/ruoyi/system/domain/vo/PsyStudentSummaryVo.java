package com.ruoyi.system.domain.vo;

import java.util.List;
import com.ruoyi.system.domain.PsyAssessmentRecord;
import com.ruoyi.system.domain.PsyEmotionRecord;
import com.ruoyi.system.domain.PsyStudentProfile;

public class PsyStudentSummaryVo
{
    private PsyStudentProfile profile;

    private List<PsyEmotionRecord> latestEmotionRecords;

    private List<PsyAssessmentRecord> latestAssessmentRecords;

    public PsyStudentProfile getProfile()
    {
        return profile;
    }

    public void setProfile(PsyStudentProfile profile)
    {
        this.profile = profile;
    }

    public List<PsyEmotionRecord> getLatestEmotionRecords()
    {
        return latestEmotionRecords;
    }

    public void setLatestEmotionRecords(List<PsyEmotionRecord> latestEmotionRecords)
    {
        this.latestEmotionRecords = latestEmotionRecords;
    }

    public List<PsyAssessmentRecord> getLatestAssessmentRecords()
    {
        return latestAssessmentRecords;
    }

    public void setLatestAssessmentRecords(List<PsyAssessmentRecord> latestAssessmentRecords)
    {
        this.latestAssessmentRecords = latestAssessmentRecords;
    }
}
