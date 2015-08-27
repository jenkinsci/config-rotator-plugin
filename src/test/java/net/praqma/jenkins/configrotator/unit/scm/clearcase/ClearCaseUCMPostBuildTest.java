/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.unit.scm.clearcase;

import hudson.model.TaskListener;
import net.praqma.jenkins.configrotator.AbstractConfiguration;
import net.praqma.jenkins.configrotator.ConfigurationRotatorBuildAction;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMPostBuild;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Mads
 */
public class ClearCaseUCMPostBuildTest {
    
    @Test
    public void testBasicPerform() {        
        TaskListener listener = Mockito.mock(TaskListener.class);
        Mockito.when(listener.getLogger()).thenReturn(System.out);
        
        ConfigurationRotatorBuildAction ac = Mockito.mock(ConfigurationRotatorBuildAction.class);
        AbstractConfiguration a = new ClearCaseUCMConfiguration();
        Mockito.when(ac.getConfiguration()).thenReturn(a);        
        
        ClearCaseUCMPostBuild postbuilder = new ClearCaseUCMPostBuild();
        boolean res = postbuilder.perform(null, listener, ac);
        assertThat(res, is(true));
    }
}
