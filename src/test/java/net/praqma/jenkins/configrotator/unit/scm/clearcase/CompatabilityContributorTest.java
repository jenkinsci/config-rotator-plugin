/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.unit.scm.clearcase;

import hudson.model.TaskListener;
import java.util.Calendar;
import java.util.Date;
import net.praqma.jenkins.configrotator.scm.contribute.CompatabilityCompatible;
import net.praqma.jenkins.configrotator.scm.contribute.CompatabilityContributor;
import net.praqma.jenkins.configrotator.scm.contribute.ConfigRotatorClearCaseConverterImpl;
import net.praqma.jenkins.configrotator.scm.contribute.RemoteCompatabilityContributor;
import static org.hamcrest.CoreMatchers.is;
import org.jenkinsci.plugins.compatibilityaction.MongoProviderImpl;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Mads
 */
public class CompatabilityContributorTest {
    
    @Test
    public void testBasicObject() throws Exception {
        TaskListener listener = Mockito.mock(TaskListener.class);
        Mockito.when(listener.getLogger()).thenReturn(System.out);
        CompatabilityContributor cc = new CompatabilityContributor(listener, null, new ConfigRotatorClearCaseConverterImpl());        
        assertThat(cc.call(), is(true));         
    }
    
    @Test
    public void testRemoteObject() throws Exception {
        MongoProviderImpl impl = Mockito.mock(MongoProviderImpl.class);
        Mockito.when(impl.create(Mockito.any(CompatabilityCompatible.class), Mockito.any(TaskListener.class))).thenReturn(null);
        RemoteCompatabilityContributor remote = new RemoteCompatabilityContributor(new CompatabilityCompatible() {

            @Override
            public Date getRegistrationDate() {
                return Calendar.getInstance().getTime();
            }

            @Override
            public void setRegistrationDate(Date registrationDate) {
                //NOOP
            }
        }, impl, TaskListener.NULL);
        
        assertThat(remote.call(), is(true));
    }         
}
