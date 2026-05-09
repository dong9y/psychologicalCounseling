package com.ruoyi.system.domain;

import java.math.BigDecimal;

public class PsyScaleOption
{
    private Long optionId;

    private Long questionId;

    private Integer optionSort;

    private String optionLabel;

    private BigDecimal optionScore;

    public Long getOptionId()
    {
        return optionId;
    }

    public void setOptionId(Long optionId)
    {
        this.optionId = optionId;
    }

    public Long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public Integer getOptionSort()
    {
        return optionSort;
    }

    public void setOptionSort(Integer optionSort)
    {
        this.optionSort = optionSort;
    }

    public String getOptionLabel()
    {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel)
    {
        this.optionLabel = optionLabel;
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
