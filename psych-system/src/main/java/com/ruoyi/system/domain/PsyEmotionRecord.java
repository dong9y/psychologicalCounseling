package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

public class PsyEmotionRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long recordId;

    private Long studentUserId;

    private Integer anonymousFlag;

    private String originalText;

    private String processedText;

    private String primaryEmotion;

    private String emotionTags;

    private BigDecimal confidence;

    private String riskLevel;

    private BigDecimal emotionScore;

    private String analysisSummary;

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

    public Integer getAnonymousFlag()
    {
        return anonymousFlag;
    }

    public void setAnonymousFlag(Integer anonymousFlag)
    {
        this.anonymousFlag = anonymousFlag;
    }

    public String getOriginalText()
    {
        return originalText;
    }

    public void setOriginalText(String originalText)
    {
        this.originalText = originalText;
    }

    public String getProcessedText()
    {
        return processedText;
    }

    public void setProcessedText(String processedText)
    {
        this.processedText = processedText;
    }

    public String getPrimaryEmotion()
    {
        return primaryEmotion;
    }

    public void setPrimaryEmotion(String primaryEmotion)
    {
        this.primaryEmotion = primaryEmotion;
    }

    public String getEmotionTags()
    {
        return emotionTags;
    }

    public void setEmotionTags(String emotionTags)
    {
        this.emotionTags = emotionTags;
    }

    public BigDecimal getConfidence()
    {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence)
    {
        this.confidence = confidence;
    }

    public String getRiskLevel()
    {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel)
    {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getEmotionScore()
    {
        return emotionScore;
    }

    public void setEmotionScore(BigDecimal emotionScore)
    {
        this.emotionScore = emotionScore;
    }

    public String getAnalysisSummary()
    {
        return analysisSummary;
    }

    public void setAnalysisSummary(String analysisSummary)
    {
        this.analysisSummary = analysisSummary;
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
