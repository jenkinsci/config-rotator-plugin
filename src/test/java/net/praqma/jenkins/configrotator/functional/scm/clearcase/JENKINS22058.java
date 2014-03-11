/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.functional.scm.clearcase;

import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.Slave;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.praqma.clearcase.Rebase;
import net.praqma.clearcase.test.junit.ClearCaseRule;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.configrotator.ConfigRotatorProject;
import net.praqma.jenkins.configrotator.ConfigRotatorRule2;
import net.praqma.jenkins.configrotator.ProjectBuilder;
import net.praqma.jenkins.configrotator.SystemValidator;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCM;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMTarget;
import net.praqma.logging.PraqmaticLogFormatter;
import net.praqma.util.test.junit.LoggingRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 *
 * @author Praqma
 */
public class JENKINS22058 {
    
    private static final String ISSUE_NR = "jenkins_22058";    
    public static final ClearCaseRule ccenv = new ClearCaseRule(ISSUE_NR);
    public static final LoggingRule lrule = new LoggingRule( "net.praqma" ).setFormat( PraqmaticLogFormatter.TINY_FORMAT );
   
    @ClassRule
    public static final TestRule chain = RuleChain.outerRule( lrule ).around( ccenv );
    
    @ClassRule
    public static final ConfigRotatorRule2 crrule = new ConfigRotatorRule2(JENKINS22058.class);
        
    @Test
    public void testJENKINS22058() throws Exception {        
        ProjectBuilder builder = new ProjectBuilder( new ClearCaseUCM( ccenv.getPVob() ) ).setName( ISSUE_NR );
        Slave sl = crrule.createSlave();
        
        ConfigRotatorProject project = builder.getProject();
        project.addTarget( new ClearCaseUCMTarget( "model-1@" + ccenv.getPVob() + ", INITIAL, false" ) ).
                addTarget( new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) );
        
        hudson.model.Project<?, ?> jProject = project.getJenkinsProject();


        //Create 1 build to get a view up and running
        AbstractBuild<?,?> build1 = crrule.buildProject(jProject, false, sl);
        
        //Validate that the build went ok.
        SystemValidator<ClearCaseUCMTarget> targets = new SystemValidator<ClearCaseUCMTarget>(build1);
        targets.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-1@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();
        
        String viewTag = "cr-"+ISSUE_NR + "-" + System.getenv("COMPUTERNAME");
        String devStream = viewTag+ "@"+ ccenv.getPVob();

        
        //View root
        File viewRoot =  new File(jProject.getLastBuiltOn().getWorkspaceFor( (FreeStyleProject)jProject ).getRemote(),"view/");
        System.out.println(viewRoot);
        
        //Select our baselines
        Baseline bl1 = ccenv.context.baselines.get("model-2");
        Baseline bl2 = ccenv.context.baselines.get("client-1");
        
        List<Baseline> bls = Arrays.asList(bl1, bl2);
   
        //Initiate rebase...why does the build afterwards not detect a rebase in progress
        System.out.println("Attempting to rebase. Do not complete. Still in progress");
        Stream st = Stream.get(devStream);
        Rebase rb = new Rebase(st).addBaselines(bls).setViewTag(viewTag).dropFromStream();//.rebase(false);
        boolean result = rb.rebase(false);
        System.out.println( String.format( "Rebasing done. Is in progress = %s", result ) );
        
        //Create 2 build to get a view up and running
        AbstractBuild<?,?> build2 = crrule.buildProject(jProject, false, sl);
        
        SystemValidator<ClearCaseUCMTarget> targets2 = new SystemValidator<ClearCaseUCMTarget>(build2);
        targets2.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-2@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();

    }
           
}
