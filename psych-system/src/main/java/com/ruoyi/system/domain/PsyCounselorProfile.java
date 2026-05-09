package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PsyCounselorProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long counselorId;

    private Long userId;

    private String counselorNo;

    private String realName;

    private String title;

    private String specialty;

    private String introduction;

    private String availableStatus;

    private String contactPhone;

    private Double serviceScore;

    public Long getCounselorId()
    {
        return counselorId;
    }

    public void setCounselorId(Long counselorId)
    {
        this.counselorId = counselorId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getCounselorNo()
    {
        return counselorNo;
    }

    public void setCounselorNo(String counselorNo)
    {
        this.counselorNo = counselorNo;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSpecialty()
    {
        return specialty;
    }

    public void setSpecialty(String specialty)
    {
        this.specialty = specialty;
    }

    public String getIntroduction()
    {
        return introduction;
    }

    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    public String getAvailableStatus()
    {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus)
    {
        this.availableStatus = availableStatus;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public Double getServiceScore()
    {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore)
    {
        this.serviceScore = serviceScore;
    }
}
