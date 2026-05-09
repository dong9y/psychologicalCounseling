package com.ruoyi.web.controller.psych;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.PsyWarning;
import com.ruoyi.system.service.IPsyCenterService;

@RestController
@RequestMapping("/psych/counselor")
public class PsyCounselorController extends BaseController
{
    @Autowired
    private IPsyCenterService psyCenterService;

    @PreAuthorize("@ss.hasPermi('psych:counselor:portal')")
    @GetMapping("/dashboard")
    public AjaxResult dashboard()
    {
        return success(psyCenterService.getCounselorDashboard(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:warning')")
    @GetMapping("/warning/list")
    public TableDataInfo warningList(PsyWarning warning)
    {
        psyCenterService.ensureCounselorProfileExists(getUserId());
        startPage();
        return getDataTable(psyCenterService.listCounselorWarnings(getUserId(), warning));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:warning')")
    @GetMapping("/warning/{warningId}")
    public AjaxResult warningDetail(@PathVariable Long warningId)
    {
        return success(psyCenterService.getWarningDetail(getUserId(), warningId));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:warning')")
    @PutMapping("/warning")
    public AjaxResult handleWarning(@RequestBody PsyWarning warning)
    {
        return toAjax(psyCenterService.handleWarning(getUserId(), warning));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:warning')")
    @GetMapping("/transfer/options")
    public AjaxResult transferOptions()
    {
        return success(psyCenterService.listCounselorTransferOptions());
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:appointment')")
    @GetMapping("/appointment/list")
    public TableDataInfo appointmentList()
    {
        psyCenterService.ensureCounselorProfileExists(getUserId());
        startPage();
        return getDataTable(psyCenterService.listCounselorAppointments(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:appointment')")
    @PutMapping("/appointment/{appointmentId}/review")
    public AjaxResult reviewAppointment(@PathVariable Long appointmentId, @RequestBody Map<String, Object> body)
    {
        String status = String.valueOf(body.get("status"));
        String reviewNote = body.get("reviewNote") == null ? null : String.valueOf(body.get("reviewNote"));
        String counselingSummary = body.get("counselingSummary") == null ? null : String.valueOf(body.get("counselingSummary"));
        return toAjax(psyCenterService.reviewAppointment(getUserId(), appointmentId, status, reviewNote, counselingSummary));
    }

    @PreAuthorize("@ss.hasPermi('psych:counselor:portal')")
    @GetMapping("/student/{studentUserId}")
    public AjaxResult studentSummary(@PathVariable Long studentUserId)
    {
        return success(psyCenterService.getStudentSummary(studentUserId));
    }
}
