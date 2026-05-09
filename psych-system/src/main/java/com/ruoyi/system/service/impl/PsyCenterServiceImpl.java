package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PsyAppointment;
import com.ruoyi.system.domain.PsyAssessmentAnswer;
import com.ruoyi.system.domain.PsyAssessmentRecord;
import com.ruoyi.system.domain.PsyCounselorProfile;
import com.ruoyi.system.domain.PsyEmotionRecord;
import com.ruoyi.system.domain.PsyMessage;
import com.ruoyi.system.domain.PsyScale;
import com.ruoyi.system.domain.PsyScaleOption;
import com.ruoyi.system.domain.PsyScaleQuestion;
import com.ruoyi.system.domain.PsyStudentProfile;
import com.ruoyi.system.domain.PsyWarning;
import com.ruoyi.system.domain.vo.PsyAdminDashboardVo;
import com.ruoyi.system.domain.vo.PsyCounselorDashboardVo;
import com.ruoyi.system.domain.vo.PsyScaleDetailVo;
import com.ruoyi.system.domain.vo.PsyStudentDashboardVo;
import com.ruoyi.system.domain.vo.PsyStudentSummaryVo;
import com.ruoyi.system.mapper.PsyCenterMapper;
import com.ruoyi.system.service.IPsyCenterService;
import com.ruoyi.system.service.ISysUserService;

@Service
public class PsyCenterServiceImpl implements IPsyCenterService
{
    @Autowired
    private PsyCenterMapper psyCenterMapper;

    @Autowired
    private ISysUserService userService;

    @Override
    public void ensureStudentProfileExists(Long userId)
    {
        ensureStudentProfile(userId);
    }

    @Override
    public void ensureCounselorProfileExists(Long userId)
    {
        ensureCounselorProfile(userId);
    }

    @Override
    public PsyStudentDashboardVo getStudentDashboard(Long userId)
    {
        ensureStudentProfile(userId);
        Map<String, Object> stats = psyCenterMapper.selectStudentDashboardStats(userId);
        PsyStudentDashboardVo vo = new PsyStudentDashboardVo();
        vo.setLatestRiskLevel(getString(stats, "latestRiskLevel", "正常"));
        vo.setRecentAssessmentCount(getLong(stats, "recentAssessmentCount"));
        vo.setPendingAppointmentCount(getLong(stats, "pendingAppointmentCount"));
        vo.setUnreadMessageCount(psyCenterMapper.selectUnreadMessageCount(userId));
        vo.setLatestEmotionRecord(psyCenterMapper.selectLatestEmotionRecordByStudent(userId));
        vo.setRecentMessages(psyCenterMapper.selectMessageListByUser(userId, 5));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PsyEmotionRecord submitEmotion(Long userId, String text, Integer anonymousFlag)
    {
        ensureStudentProfile(userId);
        PsyEmotionRecord record = analyzeEmotionText(userId, text, anonymousFlag);
        record.setCreateBy(String.valueOf(userId));
        psyCenterMapper.insertEmotionRecord(record);
        if (Integer.valueOf(1).equals(record.getWarningFlag()))
        {
            createWarningAndNotify(record);
        }
        return record;
    }

    @Override
    public List<PsyEmotionRecord> listEmotionRecords(Long userId)
    {
        return psyCenterMapper.selectEmotionRecordListByStudent(userId);
    }

    @Override
    public List<PsyScale> listScales(PsyScale scale)
    {
        return psyCenterMapper.selectScaleList(scale);
    }

    @Override
    public PsyScaleDetailVo getScaleDetail(Long scaleId)
    {
        PsyScale scale = psyCenterMapper.selectScaleById(scaleId);
        if (scale == null)
        {
            return null;
        }
        PsyScaleDetailVo detailVo = new PsyScaleDetailVo();
        copyScale(scale, detailVo);
        List<PsyScaleQuestion> questions = psyCenterMapper.selectScaleQuestionListByScaleId(scaleId);
        for (PsyScaleQuestion question : questions)
        {
            question.setOptions(psyCenterMapper.selectScaleOptionListByQuestionId(question.getQuestionId()));
        }
        detailVo.setQuestions(questions);
        return detailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PsyAssessmentRecord submitAssessment(Long userId, Long scaleId, List<Map<String, Object>> answers)
    {
        ensureStudentProfile(userId);
        PsyScaleDetailVo scale = getScaleDetail(scaleId);
        BigDecimal totalScore = BigDecimal.ZERO;
        PsyAssessmentRecord record = new PsyAssessmentRecord();
        record.setStudentUserId(userId);
        record.setScaleId(scaleId);
        record.setScaleName(scale.getScaleName());

        for (Map<String, Object> item : answers)
        {
            PsyAssessmentAnswer answer = new PsyAssessmentAnswer();
            answer.setQuestionId(Long.valueOf(String.valueOf(item.get("questionId"))));
            answer.setOptionId(Long.valueOf(String.valueOf(item.get("optionId"))));
            answer.setOptionScore(new BigDecimal(String.valueOf(item.get("optionScore"))));
            totalScore = totalScore.add(answer.getOptionScore());
        }

        String resultLevel = resolveAssessmentLevel(totalScore, scale.getWarningThreshold(), scale.getMaxScore());
        record.setTotalScore(totalScore);
        record.setResultLevel(resultLevel);
        record.setResultSummary(buildAssessmentSummary(scale.getScaleName(), totalScore, resultLevel));
        record.setGuidanceAdvice(buildAssessmentAdvice(resultLevel));
        record.setWarningFlag(("高风险".equals(resultLevel) || "中风险".equals(resultLevel)) ? 1 : 0);
        record.setCreateBy(String.valueOf(userId));
        psyCenterMapper.insertAssessmentRecord(record);

        for (Map<String, Object> item : answers)
        {
            PsyAssessmentAnswer answer = new PsyAssessmentAnswer();
            answer.setRecordId(record.getRecordId());
            answer.setQuestionId(Long.valueOf(String.valueOf(item.get("questionId"))));
            answer.setOptionId(Long.valueOf(String.valueOf(item.get("optionId"))));
            answer.setOptionScore(new BigDecimal(String.valueOf(item.get("optionScore"))));
            psyCenterMapper.insertAssessmentAnswer(answer);
        }

        if (Integer.valueOf(1).equals(record.getWarningFlag()))
        {
            PsyWarning warning = new PsyWarning();
            warning.setWarningNo("WAR-A-" + System.currentTimeMillis());
            warning.setStudentUserId(userId);
            warning.setWarningSource("量表测评");
            warning.setSourceRecordId(record.getRecordId());
            warning.setRiskLevel(record.getResultLevel());
            warning.setStatus("待处理");
            warning.setTriggerSummary(record.getResultSummary());
            Long counselorUserId = psyCenterMapper.selectFirstAvailableCounselorUserId();
            if (counselorUserId != null)
            {
                warning.setCounselorUserId(counselorUserId);
            }
            warning.setCreateBy(String.valueOf(userId));
            psyCenterMapper.insertWarning(warning);
            createMessage(userId, "assessment", "测评结果提醒", "您的测评结果为" + record.getResultLevel() + "，建议及时关注情绪变化。");
            if (counselorUserId != null)
            {
                createMessage(counselorUserId, "warning", "新的测评预警", "有一条来自量表测评的预警待处理。");
            }
        }
        return record;
    }

    @Override
    public List<PsyAssessmentRecord> listAssessmentRecords(Long userId)
    {
        return psyCenterMapper.selectAssessmentRecordListByStudent(userId);
    }

    @Override
    public List<PsyCounselorProfile> listCounselors()
    {
        return psyCenterMapper.selectCounselorList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createAppointment(Long userId, PsyAppointment appointment)
    {
        ensureStudentProfile(userId);
        appointment.setStudentUserId(userId);
        appointment.setStatus("待确认");
        appointment.setCreateBy(String.valueOf(userId));
        int rows = psyCenterMapper.insertAppointment(appointment);
        if (rows > 0)
        {
            createMessage(userId, "appointment", "预约提交成功", "您的咨询预约已提交，请留意咨询师确认结果。");
            if (appointment.getCounselorUserId() != null)
            {
                createMessage(appointment.getCounselorUserId(), "appointment", "新的预约申请", "您收到一条新的心理咨询预约申请。");
            }
        }
        return rows;
    }

    @Override
    public List<PsyAppointment> listStudentAppointments(Long userId)
    {
        return psyCenterMapper.selectAppointmentListByStudent(userId);
    }

    @Override
    public int evaluateAppointment(Long userId, Long appointmentId, String evaluationContent, BigDecimal evaluationScore)
    {
        return psyCenterMapper.updateAppointmentEvaluation(appointmentId, userId, evaluationContent, evaluationScore);
    }

    @Override
    public List<PsyMessage> listMessages(Long userId, Integer limit)
    {
        return psyCenterMapper.selectMessageListByUser(userId, limit);
    }

    @Override
    public PsyCounselorDashboardVo getCounselorDashboard(Long userId)
    {
        ensureCounselorProfile(userId);
        Map<String, Object> stats = psyCenterMapper.selectCounselorDashboardStats(userId);
        PsyCounselorDashboardVo vo = new PsyCounselorDashboardVo();
        vo.setTodayAppointmentCount(getLong(stats, "todayAppointmentCount"));
        vo.setPendingAppointmentCount(getLong(stats, "pendingAppointmentCount"));
        vo.setHighRiskWarningCount(getLong(stats, "highRiskWarningCount"));
        vo.setWeekCounselingCount(getLong(stats, "weekCounselingCount"));
        PsyWarning query = new PsyWarning();
        query.setCounselorUserId(userId);
        vo.setRecentWarnings(psyCenterMapper.selectWarningList(query));
        return vo;
    }

    @Override
    public List<PsyWarning> listCounselorWarnings(Long userId, PsyWarning warning)
    {
        warning.setCounselorUserId(userId);
        return psyCenterMapper.selectWarningList(warning);
    }

    @Override
    public PsyWarning getWarningDetail(Long userId, Long warningId)
    {
        ensureCounselorProfile(userId);
        return psyCenterMapper.selectWarningById(warningId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleWarning(Long userId, PsyWarning warning)
    {
        ensureCounselorProfile(userId);
        if (warning.getCounselorUserId() == null)
        {
            warning.setCounselorUserId(userId);
        }
        warning.setUpdateBy(String.valueOf(userId));
        if ("已处理".equals(warning.getStatus()))
        {
            warning.setHandledTime(new Date());
        }
        int rows = psyCenterMapper.updateWarning(warning);
        if (rows > 0)
        {
            PsyWarning dbWarning = psyCenterMapper.selectWarningById(warning.getWarningId());
            createMessage(dbWarning.getStudentUserId(), "warning", "预警跟进提醒", "您的心理预警已由咨询师更新为“" + dbWarning.getStatus() + "”。");
        }
        return rows;
    }

    @Override
    public PsyStudentSummaryVo getStudentSummary(Long studentUserId)
    {
        ensureStudentProfile(studentUserId);
        PsyStudentSummaryVo vo = new PsyStudentSummaryVo();
        vo.setProfile(psyCenterMapper.selectStudentProfileByUserId(studentUserId));
        List<PsyEmotionRecord> emotionRecords = psyCenterMapper.selectEmotionRecordListByStudent(studentUserId);
        List<PsyAssessmentRecord> assessmentRecords = psyCenterMapper.selectAssessmentRecordListByStudent(studentUserId);
        vo.setLatestEmotionRecords(emotionRecords.size() > 5 ? emotionRecords.subList(0, 5) : emotionRecords);
        vo.setLatestAssessmentRecords(assessmentRecords.size() > 5 ? assessmentRecords.subList(0, 5) : assessmentRecords);
        return vo;
    }

    @Override
    public List<PsyAppointment> listCounselorAppointments(Long userId)
    {
        return psyCenterMapper.selectAppointmentListByCounselor(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reviewAppointment(Long userId, Long appointmentId, String status, String reviewNote, String counselingSummary)
    {
        ensureCounselorProfile(userId);
        int rows = psyCenterMapper.updateAppointmentReview(appointmentId, userId, status, reviewNote, counselingSummary);
        if (rows > 0)
        {
            List<PsyAppointment> appointments = psyCenterMapper.selectAppointmentListByCounselor(userId);
            for (PsyAppointment item : appointments)
            {
                if (appointmentId.equals(item.getAppointmentId()))
                {
                    createMessage(item.getStudentUserId(), "appointment", "预约状态更新", "您的咨询预约已更新为“" + status + "”。");
                    break;
                }
            }
        }
        return rows;
    }

    @Override
    public PsyAdminDashboardVo getAdminDashboard()
    {
        PsyAdminDashboardVo vo = new PsyAdminDashboardVo();
        vo.setStudentCount(psyCenterMapper.selectStudentCount());
        vo.setCounselorCount(psyCenterMapper.selectCounselorCount());
        vo.setAssessmentCount(psyCenterMapper.selectAssessmentCount());
        vo.setWarningCount(psyCenterMapper.selectWarningCount());
        vo.setRecentWarnings(psyCenterMapper.selectWarningList(new PsyWarning()));
        return vo;
    }

    @Override
    public List<PsyWarning> listAdminWarnings(PsyWarning warning)
    {
        return psyCenterMapper.selectWarningList(warning);
    }

    @Override
    public List<Map<String, Object>> listCounselorTransferOptions()
    {
        return psyCenterMapper.selectCounselorTransferOptions();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveScale(PsyScale scale, String operator)
    {
        scale.setUpdateBy(operator);
        if (scale.getScaleId() == null)
        {
            scale.setCreateBy(operator);
            return psyCenterMapper.insertScale(scale);
        }
        return psyCenterMapper.updateScale(scale);
    }

    private void ensureStudentProfile(Long userId)
    {
        PsyStudentProfile profile = psyCenterMapper.selectStudentProfileByUserId(userId);
        if (profile == null)
        {
            SysUser user = userService.selectUserById(userId);
            PsyStudentProfile newProfile = new PsyStudentProfile();
            newProfile.setUserId(userId);
            newProfile.setStudentNo("STU" + userId);
            newProfile.setRealName(user != null ? user.getNickName() : "学生用户");
            newProfile.setCollege("未分配学院");
            newProfile.setMajor("未分配专业");
            newProfile.setClassName("未分配班级");
            newProfile.setContactPhone(user != null ? user.getPhonenumber() : "");
            newProfile.setStatus("0");
            newProfile.setCreateBy("system");
            psyCenterMapper.insertStudentProfile(newProfile);
        }
    }

    private void ensureCounselorProfile(Long userId)
    {
        PsyCounselorProfile profile = psyCenterMapper.selectCounselorProfileByUserId(userId);
        if (profile == null)
        {
            SysUser user = userService.selectUserById(userId);
            PsyCounselorProfile newProfile = new PsyCounselorProfile();
            newProfile.setUserId(userId);
            newProfile.setCounselorNo("COU" + userId);
            newProfile.setRealName(user != null ? user.getNickName() : "咨询师用户");
            newProfile.setTitle("心理咨询师");
            newProfile.setSpecialty("综合心理辅导");
            newProfile.setIntroduction("系统自动创建的咨询师档案");
            newProfile.setAvailableStatus("0");
            newProfile.setContactPhone(user != null ? user.getPhonenumber() : "");
            newProfile.setServiceScore(4.8D);
            newProfile.setCreateBy("system");
            psyCenterMapper.insertCounselorProfile(newProfile);
        }
    }

    private PsyEmotionRecord analyzeEmotionText(Long userId, String text, Integer anonymousFlag)
    {
        String sanitized = sanitizeText(text);
        Set<String> tags = new LinkedHashSet<>();
        BigDecimal score = BigDecimal.ZERO;

        score = score.add(matchScore(sanitized, tags, "焦虑", new String[] { "焦虑", "紧张", "睡不着", "压力", "担心", "考试" }, "22"));
        score = score.add(matchScore(sanitized, tags, "抑郁", new String[] { "难过", "无助", "绝望", "崩溃", "痛苦", "提不起劲" }, "28"));
        score = score.add(matchScore(sanitized, tags, "孤独", new String[] { "孤单", "孤独", "没人懂", "室友", "朋友少" }, "14"));
        score = score.add(matchScore(sanitized, tags, "愤怒", new String[] { "生气", "愤怒", "烦躁", "讨厌", "委屈" }, "18"));

        if (score.compareTo(BigDecimal.ZERO) == 0)
        {
            tags.add("平稳");
            score = new BigDecimal("12");
        }

        String primaryEmotion = tags.iterator().next();
        String riskLevel = resolveEmotionRiskLevel(score);
        BigDecimal confidence = BigDecimal.valueOf(Math.min(0.98D, 0.55D + tags.size() * 0.09D)).setScale(2, RoundingMode.HALF_UP);

        PsyEmotionRecord record = new PsyEmotionRecord();
        record.setStudentUserId(userId);
        record.setAnonymousFlag(anonymousFlag == null ? 0 : anonymousFlag);
        record.setOriginalText(text);
        record.setProcessedText(sanitized);
        record.setPrimaryEmotion(primaryEmotion);
        record.setEmotionTags(String.join(",", tags));
        record.setConfidence(confidence);
        record.setRiskLevel(riskLevel);
        record.setEmotionScore(score.setScale(2, RoundingMode.HALF_UP));
        record.setAnalysisSummary(buildEmotionSummary(primaryEmotion, riskLevel, tags));
        record.setGuidanceAdvice(buildEmotionAdvice(primaryEmotion, riskLevel));
        record.setWarningFlag(("中风险".equals(riskLevel) || "高风险".equals(riskLevel)) ? 1 : 0);
        return record;
    }

    private BigDecimal matchScore(String text, Set<String> tags, String tag, String[] keywords, String increment)
    {
        BigDecimal score = BigDecimal.ZERO;
        for (String keyword : keywords)
        {
            if (text.contains(keyword))
            {
                tags.add(tag);
                score = score.add(new BigDecimal(increment));
                break;
            }
        }
        return score;
    }

    private String sanitizeText(String text)
    {
        if (text == null)
        {
            return "";
        }
        String result = text.replaceAll("\\d{11}", "[手机号]");
        result = result.replaceAll("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+", "[邮箱]");
        return result.trim();
    }

    private String resolveEmotionRiskLevel(BigDecimal score)
    {
        if (score.compareTo(new BigDecimal("55")) >= 0)
        {
            return "高风险";
        }
        if (score.compareTo(new BigDecimal("35")) >= 0)
        {
            return "中风险";
        }
        if (score.compareTo(new BigDecimal("18")) >= 0)
        {
            return "关注";
        }
        return "正常";
    }

    private String resolveAssessmentLevel(BigDecimal totalScore, BigDecimal warningThreshold, BigDecimal maxScore)
    {
        BigDecimal threshold = warningThreshold == null ? new BigDecimal("45") : warningThreshold;
        BigDecimal max = maxScore == null ? new BigDecimal("80") : maxScore;
        if (totalScore.compareTo(threshold.add(new BigDecimal("10"))) >= 0)
        {
            return "高风险";
        }
        if (totalScore.compareTo(threshold) >= 0)
        {
            return "中风险";
        }
        if (totalScore.compareTo(max.multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP)) >= 0)
        {
            return "关注";
        }
        return "正常";
    }

    private String buildEmotionSummary(String primaryEmotion, String riskLevel, Set<String> tags)
    {
        return "系统识别出您当前以“" + primaryEmotion + "”为主情绪，综合标签为[" + String.join("、", tags) + "]，风险等级为" + riskLevel + "。";
    }

    private String buildEmotionAdvice(String primaryEmotion, String riskLevel)
    {
        if ("高风险".equals(riskLevel))
        {
            return "建议立即联系心理咨询师或辅导员，并尽快安排线下面谈。如存在明显危机感，请优先寻求校园应急支持。";
        }
        if ("中风险".equals(riskLevel))
        {
            return "建议近期减少高压刺激，保持规律作息，并预约心理咨询师进行进一步交流。";
        }
        if ("关注".equals(riskLevel))
        {
            return "建议通过运动、冥想、与朋友交流等方式主动调节情绪，必要时可以预约咨询。";
        }
        return "整体情绪状态较平稳，建议继续保持良好作息与表达习惯。";
    }

    private String buildAssessmentSummary(String scaleName, BigDecimal totalScore, String resultLevel)
    {
        return scaleName + "测评总分为" + totalScore.stripTrailingZeros().toPlainString() + "分，当前评估等级为" + resultLevel + "。";
    }

    private String buildAssessmentAdvice(String resultLevel)
    {
        if ("高风险".equals(resultLevel))
        {
            return "建议尽快联系专业咨询师进行评估与干预，并保持与辅导员或家人沟通。";
        }
        if ("中风险".equals(resultLevel))
        {
            return "建议关注最近一段时间的情绪波动，并预约心理咨询师进行进一步评估。";
        }
        if ("关注".equals(resultLevel))
        {
            return "建议保持规律作息，尝试放松训练，持续关注近期心理状态。";
        }
        return "当前结果处于正常范围，建议继续保持良好的学习与生活节奏。";
    }

    private void createWarningAndNotify(PsyEmotionRecord record)
    {
        PsyWarning warning = new PsyWarning();
        warning.setWarningNo("WAR-E-" + System.currentTimeMillis());
        warning.setStudentUserId(record.getStudentUserId());
        warning.setWarningSource("心理倾诉");
        warning.setSourceRecordId(record.getRecordId());
        warning.setRiskLevel(record.getRiskLevel());
        warning.setStatus("待处理");
        warning.setTriggerSummary(record.getAnalysisSummary());
        Long counselorUserId = psyCenterMapper.selectFirstAvailableCounselorUserId();
        if (counselorUserId != null)
        {
            warning.setCounselorUserId(counselorUserId);
        }
        warning.setCreateBy(String.valueOf(record.getStudentUserId()));
        psyCenterMapper.insertWarning(warning);

        createMessage(record.getStudentUserId(), "emotion", "情绪分析完成", "系统已完成本次心理倾诉分析，风险等级为“" + record.getRiskLevel() + "”。");
        if (counselorUserId != null)
        {
            createMessage(counselorUserId, "warning", "新的学生预警", "系统分配了一条“" + record.getRiskLevel() + "”预警，请及时处理。");
        }
    }

    private void createMessage(Long userId, String type, String title, String content)
    {
        PsyMessage message = new PsyMessage();
        message.setReceiverUserId(userId);
        message.setMessageType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setReadStatus("0");
        message.setCreateBy("system");
        psyCenterMapper.insertMessage(message);
    }

    private void copyScale(PsyScale source, PsyScale target)
    {
        target.setScaleId(source.getScaleId());
        target.setScaleCode(source.getScaleCode());
        target.setScaleName(source.getScaleName());
        target.setScaleType(source.getScaleType());
        target.setDescription(source.getDescription());
        target.setQuestionCount(source.getQuestionCount());
        target.setMaxScore(source.getMaxScore());
        target.setWarningThreshold(source.getWarningThreshold());
        target.setStatus(source.getStatus());
        target.setResultTemplate(source.getResultTemplate());
        target.setCreateBy(source.getCreateBy());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateBy(source.getUpdateBy());
        target.setUpdateTime(source.getUpdateTime());
        target.setRemark(source.getRemark());
    }

    private Long getLong(Map<String, Object> map, String key)
    {
        return getLong(map, key, 0L);
    }

    private Long getLong(Map<String, Object> map, String key, Long defaultValue)
    {
        if (map == null || map.get(key) == null)
        {
            return defaultValue;
        }
        return Long.valueOf(String.valueOf(map.get(key)));
    }

    private String getString(Map<String, Object> map, String key, String defaultValue)
    {
        if (map == null || map.get(key) == null)
        {
            return defaultValue;
        }
        return String.valueOf(map.get(key));
    }
}
