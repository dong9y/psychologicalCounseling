package com.ruoyi.web.controller.psych;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.PsyScale;
import com.ruoyi.system.domain.PsyWarning;
import com.ruoyi.system.service.IPsyCenterService;

@RestController
@RequestMapping("/psych/admin")
public class PsyAdminController extends BaseController
{
    @Autowired
    private IPsyCenterService psyCenterService;

    @PreAuthorize("@ss.hasPermi('psych:admin:portal')")
    @GetMapping("/dashboard")
    public AjaxResult dashboard()
    {
        return success(psyCenterService.getAdminDashboard());
    }

    @PreAuthorize("@ss.hasPermi('psych:scale:list')")
    @GetMapping("/scale/list")
    public TableDataInfo scaleList(PsyScale scale)
    {
        startPage();
        return getDataTable(psyCenterService.listScales(scale));
    }

    @PreAuthorize("@ss.hasPermi('psych:scale:add')")
    @PostMapping("/scale")
    public AjaxResult addScale(@RequestBody PsyScale scale)
    {
        return toAjax(psyCenterService.saveScale(scale, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('psych:scale:edit')")
    @PutMapping("/scale")
    public AjaxResult editScale(@RequestBody PsyScale scale)
    {
        return toAjax(psyCenterService.saveScale(scale, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('psych:warning:list')")
    @GetMapping("/warning/list")
    public TableDataInfo warningList(PsyWarning warning)
    {
        startPage();
        return getDataTable(psyCenterService.listAdminWarnings(warning));
    }
}
