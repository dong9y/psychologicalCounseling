package com.ruoyi.system.domain;

import java.util.List;

public class PsyScaleQuestion
{
    private Long questionId;

    private Long scaleId;

    private Integer questionSort;

    private String questionTitle;

    private List<PsyScaleOption> options;

    public Long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public Long getScaleId()
    {
        return scaleId;
    }

    public void setScaleId(Long scaleId)
    {
        this.scaleId = scaleId;
    }

    public Integer getQuestionSort()
    {
        return questionSort;
    }

    public void setQuestionSort(Integer questionSort)
    {
        this.questionSort = questionSort;
    }

    public String getQuestionTitle()
    {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle)
    {
        this.questionTitle = questionTitle;
    }

    public List<PsyScaleOption> getOptions()
    {
        return options;
    }

    public void setOptions(List<PsyScaleOption> options)
    {
        this.options = options;
    }
}
