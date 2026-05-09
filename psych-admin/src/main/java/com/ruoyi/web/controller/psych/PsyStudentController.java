package com.ruoyi.web.controller.psych;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.PsyAppointment;
import com.ruoyi.system.domain.PsyScale;
import com.ruoyi.system.domain.vo.PsyScaleDetailVo;
import com.ruoyi.system.service.IPsyCenterService;

@RestController
@RequestMapping("/psych/student")
public class PsyStudentController extends BaseController
{
    @Autowired
    private IPsyCenterService psyCenterService;

    @PreAuthorize("@ss.hasPermi('psych:student:portal')")
    @GetMapping("/dashboard")
    public AjaxResult dashboard()
    {
        return success(psyCenterService.getStudentDashboard(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:confession')")
    @PostMapping("/emotion")
    public AjaxResult submitEmotion(@RequestBody Map<String, Object> body)
    {
        String text = String.valueOf(body.get("text"));
        Integer anonymousFlag = body.get("anonymousFlag") == null ? 0 : Integer.valueOf(String.valueOf(body.get("anonymousFlag")));
        return success(psyCenterService.submitEmotion(getUserId(), text, anonymousFlag));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:confession')")
    @GetMapping("/emotion/list")
    public TableDataInfo emotionList()
    {
        psyCenterService.ensureStudentProfileExists(getUserId());
        startPage();
        return getDataTable(psyCenterService.listEmotionRecords(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:assessment')")
    @GetMapping("/scale/list")
    public TableDataInfo scaleList(PsyScale scale)
    {
        startPage();
        return getDataTable(psyCenterService.listScales(scale));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:assessment')")
    @GetMapping("/scale/{scaleId}")
    public AjaxResult scaleDetail(@PathVariable Long scaleId)
    {
        PsyScaleDetailVo detail = psyCenterService.getScaleDetail(scaleId);
        return success(detail);
    }

    @PreAuthorize("@ss.hasPermi('psych:student:assessment')")
    @PostMapping("/assessment")
    public AjaxResult submitAssessment(@RequestBody Map<String, Object> body)
    {
        Long scaleId = Long.valueOf(String.valueOf(body.get("scaleId")));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> answers = (List<Map<String, Object>>) body.get("answers");
        return success(psyCenterService.submitAssessment(getUserId(), scaleId, answers));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:assessment')")
    @GetMapping("/assessment/list")
    public TableDataInfo assessmentList()
    {
        psyCenterService.ensureStudentProfileExists(getUserId());
        startPage();
        return getDataTable(psyCenterService.listAssessmentRecords(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:appointment')")
    @GetMapping("/counselor/list")
    public TableDataInfo counselorList()
    {
        startPage();
        return getDataTable(psyCenterService.listCounselors());
    }

    @PreAuthorize("@ss.hasPermi('psych:student:appointment')")
    @PostMapping("/appointment")
    public AjaxResult createAppointment(@RequestBody PsyAppointment appointment)
    {
        return toAjax(psyCenterService.createAppointment(getUserId(), appointment));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:appointment')")
    @GetMapping("/appointment/list")
    public TableDataInfo appointmentList()
    {
        psyCenterService.ensureStudentProfileExists(getUserId());
        startPage();
        return getDataTable(psyCenterService.listStudentAppointments(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:appointment')")
    @PutMapping("/appointment/{appointmentId}/evaluate")
    public AjaxResult evaluateAppointment(@PathVariable Long appointmentId, @RequestBody Map<String, Object> body)
    {
        String evaluationContent = String.valueOf(body.get("evaluationContent"));
        BigDecimal evaluationScore = new BigDecimal(String.valueOf(body.get("evaluationScore")));
        return toAjax(psyCenterService.evaluateAppointment(getUserId(), appointmentId, evaluationContent, evaluationScore));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:profile')")
    @GetMapping("/messages")
    public AjaxResult messages(Integer limit)
    {
        return success(psyCenterService.listMessages(getUserId(), limit));
    }

    @PreAuthorize("@ss.hasPermi('psych:student:profile')")
    @GetMapping("/profile")
    public AjaxResult profile()
    {
        return success(psyCenterService.getStudentSummary(getUserId()));
    }
}
