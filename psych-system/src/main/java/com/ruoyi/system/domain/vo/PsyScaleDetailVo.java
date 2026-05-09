package com.ruoyi.system.domain.vo;

import java.util.List;
import com.ruoyi.system.domain.PsyScale;
import com.ruoyi.system.domain.PsyScaleQuestion;

public class PsyScaleDetailVo extends PsyScale
{
    private static final long serialVersionUID = 1L;

    private List<PsyScaleQuestion> questions;

    public List<PsyScaleQuestion> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<PsyScaleQuestion> questions)
    {
        this.questions = questions;
    }
}
