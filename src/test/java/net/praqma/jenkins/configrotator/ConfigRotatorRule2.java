package net.praqma.jenkins.configrotator;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Slave;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 */
public class ConfigRotatorRule2 extends JenkinsRule {

    private static Logger logger = Logger.getLogger( ConfigRotatorRule2.class.getName() );

    private File outputDir;

    public ConfigRotatorRule2() {

        //System.out.println( "ENVS: " +  System.getenv() );

        if( System.getenv().containsKey( "BUILD_NUMBER" ) ) {
            String bname = System.getenv( "JOB_NAME" );
            Integer number = new Integer( System.getenv( "BUILD_NUMBER" ) );

            this.outputDir = new File( new File( new File( System.getProperty( "user.dir" ) ), "test-logs" ), number.toString() );
        } else {
            this.outputDir = new File( new File( System.getProperty( "user.dir" ) ), "runs" );
        }

        this.outputDir.mkdirs();
    }

    public AbstractBuild<?, ?> buildProject( AbstractProject<?, ?> project, boolean fail, Slave slave ) throws IOException {

        if( slave != null ) {
            logger.fine( "Running on " + slave );
            project.setAssignedNode( slave );
        }

        AbstractBuild<?, ?> build = null;
        try {
            EnableLoggerAction action = new EnableLoggerAction( outputDir );
            build = project.scheduleBuild2( 0, new Cause.UserCause(), action ).get();
        } catch( Exception e ) {
            e.printStackTrace();
        }

        PrintStream out = new PrintStream( new File( outputDir, "jenkins." + build.getNumber() + ".log" ) );

        out.println( "Build      : " + build );
        out.println( "Workspace  : " + build.getWorkspace() );
        out.println( "Logfile    : " + build.getLogFile() );
        out.println( "Description: " + build.getDescription() );
        out.println();
        out.println( "-------------------------------------------------" );
        out.println( "                JENKINS LOG: " );
        out.println( "-------------------------------------------------" );
        out.println( getLog( build ) );
        out.println( "-------------------------------------------------" );
        out.println( "-------------------------------------------------" );
        out.println();

        return build;
    }
}
