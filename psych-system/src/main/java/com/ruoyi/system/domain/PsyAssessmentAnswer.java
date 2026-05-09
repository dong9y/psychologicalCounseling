package com.ruoyi.system.domain;

import java.math.BigDecimal;

public class PsyAssessmentAnswer
{
    private Long answerId;

    private Long recordId;

    private Long questionId;

    private Long optionId;

    private BigDecimal optionScore;

    public Long getAnswerId()
    {
        return answerId;
    }

    public void setAnswerId(Long answerId)
    {
        this.answerId = answerId;
    }

    public Long getRecordId()
    {
        return recordId;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public Long getOptionId()
    {
        return optionId;
    }

    public void setOptionId(Long optionId)
    {
        this.optionId = optionId;
    }

    public BigDecimal getOptionScore()
    {
        return optionScore;
    }

    public void setOptionScore(BigDecimal optionScore)
    {
        this.optionScore = optionScore;
    }
}
