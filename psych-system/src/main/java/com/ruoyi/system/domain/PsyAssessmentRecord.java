package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

public class PsyAssessmentRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long recordId;

    private Long studentUserId;

    private Long scaleId;

    private String scaleName;

    private BigDecimal totalScore;

    private BigDecimal maxScore;

    private String resultLevel;

    private String resultSummary;

    private String guidanceAdvice;

    private Integer warningFlag;

    public Long getRecordId()
    {
        return recordId;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public Long getScaleId()
    {
        return scaleId;
    }

    public void setScaleId(Long scaleId)
    {
        this.scaleId = scaleId;
    }

    public String getScaleName()
    {
        return scaleName;
    }

    public void setScaleName(String scaleName)
    {
        this.scaleName = scaleName;
    }

    public BigDecimal getTotalScore()
    {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore)
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore)
    {
        this.maxScore = maxScore;
    }

    public String getResultLevel()
    {
        return resultLevel;
    }

    public void setResultLevel(String resultLevel)
    {
        this.resultLevel = resultLevel;
    }

    public String getResultSummary()
    {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary)
    {
        this.resultSummary = resultSummary;
    }

    public String getGuidanceAdvice()
    {
        return guidanceAdvice;
    }

    public void setGuidanceAdvice(String guidanceAdvice)
    {
        this.guidanceAdvice = guidanceAdvice;
    }

    public Integer getWarningFlag()
    {
        return warningFlag;
    }

    public void setWarningFlag(Integer warningFlag)
    {
        this.warningFlag = warningFlag;
    }
}
