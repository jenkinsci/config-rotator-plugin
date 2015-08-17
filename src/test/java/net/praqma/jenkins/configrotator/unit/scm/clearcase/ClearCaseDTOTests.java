/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.unit.scm.clearcase;

import net.praqma.clearcase.cleartool.Cleartool;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfigurationComponent;
import net.praqma.jenkins.configrotator.scm.contribute.dto.ClearCaseUCMConfigurationComponentDTO;
import net.praqma.util.execute.CmdResult;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author Mads
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { Baseline.class, Component.class, Stream.class, Cleartool.class })
public class ClearCaseDTOTests {
    
    @Test
    public void basicTest() throws Exception {       
        PowerMockito.mockStatic( Cleartool.class );
        CmdResult result = new CmdResult();
        String blResult = "GEPA_BUILD_D_Int_V0.0.0.267_12-08-2015-153648.8879::component:prototypeLEController@\\appComponents::stream:GEPA_BUILD_D_Int@\\appComponents::INITIAL::Z647::20150812.214009::Not Labeled::GMA_appComponents@\\appComponents";        
        result.stdoutBuffer = new StringBuffer().append(blResult);
                
		PowerMockito.when( Cleartool.run( Mockito.any( String.class ) ) ).thenReturn( result );
        
        Baseline bl = Baseline.get("GEPA_BUILD_D_Int_V0.0.0.267_12-08-2015-153648.8879@\\appComponents");
        Component cmp =  Component.get("prototypeLEController@\\appComponents");       
        Stream str = Stream.get("GEPA_BUILD_D_Int@\\appComponents");
        
        ClearCaseUCMConfigurationComponentDTO dto = ClearCaseUCMConfigurationComponentDTO.fromComponent(new ClearCaseUCMConfigurationComponent(bl, bl.getPromotionLevel(), false));
        assertThat(dto.getBaseline(), is("baseline:GEPA_BUILD_D_Int_V0.0.0.267_12-08-2015-153648.8879@\\appComponents"));
        assertThat(dto.getComponent(), is("component:prototypeLEController@\\appComponents"));
        assertThat(dto.getStream(), is("stream:GEPA_BUILD_D_Int@\\appComponents"));                
        
    }
    
}
