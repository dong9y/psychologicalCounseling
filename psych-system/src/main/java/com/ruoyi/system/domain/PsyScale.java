package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

public class PsyScale extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long scaleId;

    private String scaleCode;

    private String scaleName;

    private String scaleType;

    private String description;

    private Integer questionCount;

    private BigDecimal maxScore;

    private BigDecimal warningThreshold;

    private String status;

    private String resultTemplate;

    public Long getScaleId()
    {
        return scaleId;
    }

    public void setScaleId(Long scaleId)
    {
        this.scaleId = scaleId;
    }

    public String getScaleCode()
    {
        return scaleCode;
    }

    public void setScaleCode(String scaleCode)
    {
        this.scaleCode = scaleCode;
    }

    public String getScaleName()
    {
        return scaleName;
    }

    public void setScaleName(String scaleName)
    {
        this.scaleName = scaleName;
    }

    public String getScaleType()
    {
        return scaleType;
    }

    public void setScaleType(String scaleType)
    {
        this.scaleType = scaleType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getQuestionCount()
    {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount)
    {
        this.questionCount = questionCount;
    }

    public BigDecimal getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore)
    {
        this.maxScore = maxScore;
    }

    public BigDecimal getWarningThreshold()
    {
        return warningThreshold;
    }

    public void setWarningThreshold(BigDecimal warningThreshold)
    {
        this.warningThreshold = warningThreshold;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getResultTemplate()
    {
        return resultTemplate;
    }

    public void setResultTemplate(String resultTemplate)
    {
        this.resultTemplate = resultTemplate;
    }
}
