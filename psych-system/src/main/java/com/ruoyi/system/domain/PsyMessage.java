package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PsyMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long messageId;

    private Long receiverUserId;

    private String messageType;

    private String title;

    private String content;

    private String readStatus;

    public Long getMessageId()
    {
        return messageId;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public Long getReceiverUserId()
    {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId)
    {
        this.receiverUserId = receiverUserId;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getReadStatus()
    {
        return readStatus;
    }

    public void setReadStatus(String readStatus)
    {
        this.readStatus = readStatus;
    }
}
