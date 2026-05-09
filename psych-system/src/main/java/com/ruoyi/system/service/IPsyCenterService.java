package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.PsyAppointment;
import com.ruoyi.system.domain.PsyAssessmentRecord;
import com.ruoyi.system.domain.PsyCounselorProfile;
import com.ruoyi.system.domain.PsyEmotionRecord;
import com.ruoyi.system.domain.PsyMessage;
import com.ruoyi.system.domain.PsyScale;
import com.ruoyi.system.domain.PsyWarning;
import com.ruoyi.system.domain.vo.PsyAdminDashboardVo;
import com.ruoyi.system.domain.vo.PsyCounselorDashboardVo;
import com.ruoyi.system.domain.vo.PsyScaleDetailVo;
import com.ruoyi.system.domain.vo.PsyStudentDashboardVo;
import com.ruoyi.system.domain.vo.PsyStudentSummaryVo;

public interface IPsyCenterService
{
    void ensureStudentProfileExists(Long userId);

    void ensureCounselorProfileExists(Long userId);

    PsyStudentDashboardVo getStudentDashboard(Long userId);

    PsyEmotionRecord submitEmotion(Long userId, String text, Integer anonymousFlag);

    List<PsyEmotionRecord> listEmotionRecords(Long userId);

    List<PsyScale> listScales(PsyScale scale);

    PsyScaleDetailVo getScaleDetail(Long scaleId);

    PsyAssessmentRecord submitAssessment(Long userId, Long scaleId, List<Map<String, Object>> answers);

    List<PsyAssessmentRecord> listAssessmentRecords(Long userId);

    List<PsyCounselorProfile> listCounselors();

    int createAppointment(Long userId, PsyAppointment appointment);

    List<PsyAppointment> listStudentAppointments(Long userId);

    int evaluateAppointment(Long userId, Long appointmentId, String evaluationContent, BigDecimal evaluationScore);

    List<PsyMessage> listMessages(Long userId, Integer limit);

    PsyCounselorDashboardVo getCounselorDashboard(Long userId);

    List<PsyWarning> listCounselorWarnings(Long userId, PsyWarning warning);

    PsyWarning getWarningDetail(Long userId, Long warningId);

    int handleWarning(Long userId, PsyWarning warning);

    PsyStudentSummaryVo getStudentSummary(Long studentUserId);

    List<PsyAppointment> listCounselorAppointments(Long userId);

    int reviewAppointment(Long userId, Long appointmentId, String status, String reviewNote, String counselingSummary);

    PsyAdminDashboardVo getAdminDashboard();

    List<PsyWarning> listAdminWarnings(PsyWarning warning);

    List<Map<String, Object>> listCounselorTransferOptions();

    int saveScale(PsyScale scale, String operator);
}
