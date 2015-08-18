/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.functional.scm.clearcase;

import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Result;
import hudson.model.Slave;
import net.praqma.clearcase.test.junit.ClearCaseRule;
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
 * @author Mads
 */
public class JENKINS29975 {
    private static final String ISSUE_NR = "jenkins_29975";    
    public static final ClearCaseRule ccenv = new ClearCaseRule(ISSUE_NR,"setup_no_label.xml");
    public static final LoggingRule lrule = new LoggingRule( "net.praqma" ).setFormat( PraqmaticLogFormatter.TINY_FORMAT );
    
    @ClassRule
    public static final TestRule chain = RuleChain.outerRule( lrule ).around( ccenv );
    
    @ClassRule
    public static final ConfigRotatorRule2 crrule = new ConfigRotatorRule2(JENKINS29975.class);
        
    @Test
    public void testJENKINS29975() throws Exception {
        ProjectBuilder builder = new ProjectBuilder( new ClearCaseUCM( ccenv.getPVob() ) ).setName( ISSUE_NR );
        Slave sl = crrule.createSlave();
        
        ConfigRotatorProject project = builder.getProject();
        project.addTarget( new ClearCaseUCMTarget( "model-1@" + ccenv.getPVob() + ", INITIAL, false" ) ).
                addTarget( new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) );
        
        hudson.model.Project<?, ?> jProject = project.getJenkinsProject();
        
        //model-1
        //client-1
        AbstractBuild<?,?> build1 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
        
        SystemValidator<ClearCaseUCMTarget> targets1 = new SystemValidator<ClearCaseUCMTarget>(build1);
        targets1.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-1@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();

        //model-2
        //client-1
        AbstractBuild<?,?> build2 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();

        SystemValidator<ClearCaseUCMTarget> targets2 = new SystemValidator<ClearCaseUCMTarget>(build2);
        targets2.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-2@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();        
        
        //model-3
        //client-1
        AbstractBuild<?,?> build3 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
        
        SystemValidator<ClearCaseUCMTarget> targets3 = new SystemValidator<ClearCaseUCMTarget>(build3);
        targets3.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-3@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-1@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();
                
        //model-3
        //client-2
        AbstractBuild<?,?> build4 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
        
        SystemValidator<ClearCaseUCMTarget> targets4 = new SystemValidator<ClearCaseUCMTarget>(build4);
        targets4.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-3@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-2@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();
        
        //model-3
        //client-3
        AbstractBuild<?,?> build5 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
        
        SystemValidator<ClearCaseUCMTarget> targets5 = new SystemValidator<ClearCaseUCMTarget>(build5);
        targets5.checkAction(true)
                .checkTargets( new ClearCaseUCMTarget( "model-3@" + ccenv.getPVob() + ", INITIAL, false" ), new ClearCaseUCMTarget( "client-3@" + ccenv.getPVob() + ", INITIAL, false" ) )
                .checkExpectedResult(Result.SUCCESS).validate();        
        
        AbstractBuild<?,?> build6 = jProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
                
        //Last build. We have an unlabeled baseline. We should NOT build this.
        SystemValidator<ClearCaseUCMTarget> targets6 = new SystemValidator<ClearCaseUCMTarget>(build6);
        targets6.checkExpectedResult(Result.NOT_BUILT);
         
    }
}
