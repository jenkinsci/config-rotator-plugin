package net.praqma.jenkins.configrotator.unit.scm.clearcase;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.praqma.clearcase.cleartool.Cleartool;
import net.praqma.clearcase.exceptions.UnableToInitializeEntityException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.configrotator.AbstractConfigurationComponent;
import net.praqma.jenkins.configrotator.ConfigurationRotatorException;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCM;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfigurationComponent;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMTarget;
import net.praqma.util.debug.Logger;
import net.praqma.util.debug.appenders.Appender;
import net.praqma.util.debug.appenders.ConsoleAppender;
import net.praqma.util.execute.CmdResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author Praqma
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Baseline.class, Component.class, Stream.class, Cleartool.class })
public class ClearCaseUCMConfigurationTest extends TestCase {
    
    static {
		Appender appender = new ConsoleAppender();
		appender.setMinimumLevel( Logger.LogLevel.DEBUG );
		Logger.addAppender( appender );
	}
	
	/* Typical jenkins objects */
	AbstractProject<?, ?> project;
	AbstractBuild<?, ?> build;
	Launcher launcher;
	TaskListener tasklistener;
	BuildListener buildlistener;
	FilePath workspace = new FilePath( new File( "" ) );
	
	@Before
	public void initialize() {
		project = Mockito.mock( FreeStyleProject.class );
		build = PowerMockito.mock( FreeStyleBuild.class );
		launcher = Mockito.mock( Launcher.class );
		tasklistener = Mockito.mock( TaskListener.class );
		buildlistener = Mockito.mock( BuildListener.class );
		
		/* Behaviour */
		Mockito.when( tasklistener.getLogger() ).thenReturn( System.out );
		Mockito.when( buildlistener.getLogger() ).thenReturn( System.out );
	}
    
    @Test 
    public void testClearCaseConfigurationGetChangedComponentMethods() throws ConfigurationRotatorException, IOException, UnableToInitializeEntityException {
        ClearCaseUCM ccucm = new ClearCaseUCM( "" );
        List<ClearCaseUCMTarget> targets = new ArrayList<ClearCaseUCMTarget>();
		targets.add( new ClearCaseUCMTarget( "bl1@\\pvob", Project.PromotionLevel.INITIAL, false ) );
        targets.add( new ClearCaseUCMTarget("bl2@\\pvob", Project.PromotionLevel.INITIAL, false ) );
		ccucm.targets = targets;
		ClearCaseUCM spy = Mockito.spy( ccucm );
        
        Integer expectedIndex = -1; 
        List<Integer> expecteds = new ArrayList<Integer>();
        
        List<ClearCaseUCMConfigurationComponent> comps = new ArrayList<ClearCaseUCMConfigurationComponent>();
        
        ClearCaseUCMConfigurationComponent comp = new ClearCaseUCMConfigurationComponent(Baseline.get("bl1@\\pvob"), Project.PromotionLevel.INITIAL, false);
        ClearCaseUCMConfigurationComponent comp2 = new ClearCaseUCMConfigurationComponent(Baseline.get("bl1@\\pvob"), Project.PromotionLevel.INITIAL, false);
        
        comps.add(comp);
        comps.add(comp2);

        net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration ccc = new net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration( comps );
               
        net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMConfiguration cccSpy = Mockito.spy(ccc);
        
        Mockito.doReturn(comps).when(cccSpy).getList();
        
        Mockito.doReturn(expecteds).when(cccSpy).getChangedComponentIndecies();
        
        Integer indexZero = 0;
        List<Integer> expecteds2 = new ArrayList<Integer>();
        expecteds2.add(indexZero);
        
        ccc.getList().get(0).setChangedLast(true);
        Mockito.doReturn(expecteds2).when(cccSpy).getChangedComponentIndecies();
        
        ClearCaseUCMConfigurationComponent compIndexOne = cccSpy.getList().get(0);
        List<ClearCaseUCMConfigurationComponent> compsIndexOnelist = new ArrayList<ClearCaseUCMConfigurationComponent>(Arrays.asList(compIndexOne));
        
        
        //Assert that the object returned matches the changed component
        Mockito.doReturn(compsIndexOnelist).when(cccSpy).getChangedComponents();        
        
        ccc.getList().get(0).setChangedLast(false);
        Mockito.doReturn(expecteds).when(cccSpy).getChangedComponents();     
    }
    
    
    @Test
    public void testItemization() throws Exception {        
        PowerMockito.mockStatic( Cleartool.class );
        CmdResult result = new CmdResult();
        //Dummy result
        String blResult = "GEPA_BUILD_D_Int_V0.0.0.267_12-08-2015-153648.8879::component:prototypeLEController@\\appComponents::stream:GEPA_BUILD_D_Int@\\appComponents::INITIAL::Z647::20150812.214009::Not Labeled::GMA_appComponents@\\appComponents";        
        result.stdoutBuffer = new StringBuffer().append(blResult);
        
		PowerMockito.when( Cleartool.run( Mockito.any( String.class ) ) ).thenReturn( result );
        
        ClearCaseUCMConfigurationComponent ccucmComp1 = new ClearCaseUCMConfigurationComponent("baselineModel1@\\somewhere", "INITIAL", false);
        ClearCaseUCMConfigurationComponent ccucmComp2 = new ClearCaseUCMConfigurationComponent("baselineSystem2@\\somewhere", "INITIAL", false);
       
        List<? extends AbstractConfigurationComponent> previousConfig = new ArrayList<AbstractConfigurationComponent>(Arrays.asList(ccucmComp1,ccucmComp2));        
        
        ClearCaseUCMConfigurationComponent ccucmComp1changed = new ClearCaseUCMConfigurationComponent("baselineModel2@\\somewhere", "INITIAL", false);
        ClearCaseUCMConfigurationComponent ccucmComp2changed = new ClearCaseUCMConfigurationComponent("baselineSystem3@\\somewhere", "INITIAL", false);
        List<? extends AbstractConfigurationComponent> changedComponents = new ArrayList<AbstractConfigurationComponent>(Arrays.asList(ccucmComp1changed,ccucmComp2changed));
        
        List<Integer> changedIndicies = new ArrayList<Integer>();
        changedIndicies.add(0);
        changedIndicies.add(1);
        
        String html = ClearCaseUCMConfiguration.itemizeForHtml(previousConfig, changedComponents, changedIndicies);
        String expected = "Baseline changed from baselineModel1@\\somewhere to baselineModel2@\\somewhere<br/>Baseline changed from baselineSystem2@\\somewhere to baselineSystem3@\\somewhere";
        assertEquals(expected, html);
        
        //No changes
        changedIndicies = new ArrayList<Integer>();
        
        List<? extends AbstractConfigurationComponent> changedComponentsEmpty = Collections.EMPTY_LIST;
        String htmlEmpty = ClearCaseUCMConfiguration.itemizeForHtml(previousConfig, changedComponentsEmpty, changedIndicies);
        assertEquals("New Configuration - no changes yet", htmlEmpty);
    }
    
    
    @Test
    public void testGetHtml () {
        ClearCaseUCMConfiguration configuration = new ClearCaseUCMConfiguration();
        Assert.assertNotNull(configuration.toHtml());
    }
}
