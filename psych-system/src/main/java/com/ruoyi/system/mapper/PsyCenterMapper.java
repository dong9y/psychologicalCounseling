package com.ruoyi.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
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

public interface PsyCenterMapper
{
    PsyStudentProfile selectStudentProfileByUserId(Long userId);

    int insertStudentProfile(PsyStudentProfile profile);

    PsyCounselorProfile selectCounselorProfileByUserId(Long userId);

    int insertCounselorProfile(PsyCounselorProfile profile);

    Map<String, Object> selectStudentDashboardStats(Long userId);

    PsyEmotionRecord selectLatestEmotionRecordByStudent(Long userId);

    List<PsyEmotionRecord> selectEmotionRecordListByStudent(Long userId);

    int insertEmotionRecord(PsyEmotionRecord record);

    List<PsyScale> selectScaleList(PsyScale scale);

    PsyScale selectScaleById(Long scaleId);

    List<PsyScaleQuestion> selectScaleQuestionListByScaleId(Long scaleId);

    List<PsyScaleOption> selectScaleOptionListByQuestionId(Long questionId);

    List<PsyScaleOption> selectAllOptionsByScaleId(Long scaleId);

    int insertAssessmentRecord(PsyAssessmentRecord record);

    int insertAssessmentAnswer(PsyAssessmentAnswer answer);

    List<PsyAssessmentRecord> selectAssessmentRecordListByStudent(Long userId);

    List<PsyCounselorProfile> selectCounselorList();

    int insertAppointment(PsyAppointment appointment);

    List<PsyAppointment> selectAppointmentListByStudent(Long userId);

    List<PsyAppointment> selectAppointmentListByCounselor(Long userId);

    int updateAppointmentEvaluation(@Param("appointmentId") Long appointmentId, @Param("studentUserId") Long studentUserId,
            @Param("evaluationContent") String evaluationContent, @Param("evaluationScore") BigDecimal evaluationScore);

    int updateAppointmentReview(@Param("appointmentId") Long appointmentId, @Param("counselorUserId") Long counselorUserId,
            @Param("status") String status, @Param("reviewNote") String reviewNote, @Param("counselingSummary") String counselingSummary);

    List<PsyMessage> selectMessageListByUser(@Param("userId") Long userId, @Param("limit") Integer limit);

    Long selectUnreadMessageCount(Long userId);

    int insertMessage(PsyMessage message);

    Map<String, Object> selectCounselorDashboardStats(Long userId);

    List<PsyWarning> selectWarningList(PsyWarning warning);

    PsyWarning selectWarningById(Long warningId);

    int insertWarning(PsyWarning warning);

    int updateWarning(PsyWarning warning);

    Long selectFirstAvailableCounselorUserId();

    List<Map<String, Object>> selectCounselorTransferOptions();

    Map<String, Object> selectAdminDashboardStats();

    Long selectStudentCount();

    Long selectCounselorCount();

    Long selectAssessmentCount();

    Long selectWarningCount();

    int insertScale(PsyScale scale);

    int updateScale(PsyScale scale);
}
