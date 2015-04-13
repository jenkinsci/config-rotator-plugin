/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.scm.contribute.dto;

import java.io.Serializable;
import java.util.ArrayList;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfigurationComponent;

/**
 *
 * @author Mads
 */
public class ClearCaseUCMConfigurationDTO extends ArrayList<ClearCaseUCMConfigurationComponentDTO> implements Serializable {
    public ClearCaseUCMConfigurationDTO() {}
    
    public static ClearCaseUCMConfigurationDTO fromConfiguration(ClearCaseUCMConfiguration configuration)  {
        ClearCaseUCMConfigurationDTO dto = new ClearCaseUCMConfigurationDTO();
        
        for(ClearCaseUCMConfigurationComponent comp : configuration.getList()) {
            dto.add(ClearCaseUCMConfigurationComponentDTO.fromComponent(comp));
        }
        
        
        return dto;
    }
 }
