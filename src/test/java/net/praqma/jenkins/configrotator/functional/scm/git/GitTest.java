package net.praqma.jenkins.configrotator.functional.scm.git;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import net.praqma.jenkins.configrotator.*;
import net.praqma.jenkins.configrotator.scm.clearcaseucm.ClearCaseUCMTarget;
import net.praqma.jenkins.configrotator.scm.git.Git;
import net.praqma.jenkins.configrotator.scm.git.GitTarget;
import net.praqma.logging.PraqmaticLogFormatter;
import net.praqma.util.test.junit.LoggingRule;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class GitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public GitRule git = new GitRule();

    @ClassRule
    public static LoggingRule lrule = new LoggingRule( Level.ALL, "net.praqma" ).setFormat( PraqmaticLogFormatter.NORMAL_FORMAT );

    @ClassRule
    public static ConfigRotatorRule2 crRule = new ConfigRotatorRule2( GitTest.class );

    @Test
    public void basic() throws IOException, GitAPIException, InterruptedException {
        git.initialize( folder.newFolder() );
        RevCommit commit1 = git.createCommit( "text.txt", "1" );

        ProjectBuilder builder = new ProjectBuilder( new Git(new ArrayList<GitTarget>()) ).setName( "git-test-01" );
        ConfigRotatorProject project = builder.getProject();
        project.addTarget( new GitTarget( "test", git.getRepo(), "master", commit1.getName(), false ) );

        AbstractBuild<?, ?> build = crRule.buildProject( project.getJenkinsProject(), false, null );

        FilePath path = new FilePath( project.getJenkinsProject().getLastBuiltOn().getWorkspaceFor( (FreeStyleProject)project.getJenkinsProject() ), "test" );
        File filePath = new File( path.toURI() );

        SystemValidator<ClearCaseUCMTarget> val = new SystemValidator<ClearCaseUCMTarget>( build );
        val.checkExpectedResult( Result.SUCCESS ).
                checkAction( true ).
                checkCompatability( true ).
                checkTargets( new GitTarget( "test", git.getRepo(), "master", commit1.getName(), false ) ).
                checkContent( new File( filePath, "text.txt" ), "1" ).
                validate();
        git.cleanup();
    }

    @Test
    public void basicNothingToDo() throws IOException, GitAPIException, InterruptedException {
        git.initialize( folder.newFolder() );
        RevCommit commit1 = git.createCommit( "text.txt", "1" );

        ProjectBuilder builder = new ProjectBuilder( new Git(new ArrayList<GitTarget>()) ).setName( "git-test-02" );
        ConfigRotatorProject project = builder.getProject();
        project.addTarget( new GitTarget( "test", git.getRepo(), "master", commit1.getName(), false ) );

        AbstractBuild<?, ?> build = crRule.buildProject( project.getJenkinsProject(), false, null );

        FilePath path = new FilePath( project.getJenkinsProject().getLastBuiltOn().getWorkspaceFor( (FreeStyleProject)project.getJenkinsProject() ), "test" );
        File filePath = new File( path.toURI() );

        SystemValidator<ClearCaseUCMTarget> val = new SystemValidator<ClearCaseUCMTarget>( build );
        val.checkExpectedResult( Result.SUCCESS ).
                checkAction( true ).
                checkCompatability( true ).
                checkTargets( new GitTarget( "test", git.getRepo(), "master", commit1.getName(), false ) ).
                checkContent( new File( filePath, "text.txt" ), "1" ).
                validate();

        AbstractBuild<?, ?> build2 = crRule.buildProject( project.getJenkinsProject(), false, null );

        SystemValidator<ClearCaseUCMTarget> val2 = new SystemValidator<ClearCaseUCMTarget>( build2 );
        val2.checkExpectedResult( Result.NOT_BUILT ).checkAction( false ).validate();
        git.cleanup();
    }


    @Test
    public void basicMultiple() throws IOException, GitAPIException, InterruptedException {
        git.initialize( folder.newFolder() );
        RevCommit commit1 = git.createCommit( "text.txt", "1" );
        RevCommit commit2 = git.createCommit( "text.txt", "2" );

        ProjectBuilder builder = new ProjectBuilder( new Git(new ArrayList<GitTarget>()) ).setName( "git-test-03" );
        ConfigRotatorProject project = builder.getProject();
        project.addTarget( new GitTarget( "test", git.getRepo(), "master", commit1.getName(), false ) );

        AbstractBuild<?, ?> build1 = crRule.buildProject( project.getJenkinsProject(), false, null );
        AbstractBuild<?, ?> build2 = crRule.buildProject( project.getJenkinsProject(), false, null );

        FilePath path = new FilePath( project.getJenkinsProject().getLastBuiltOn().getWorkspaceFor( (FreeStyleProject)project.getJenkinsProject() ), "test" );
        File filePath = new File( path.toURI() );

        SystemValidator<ClearCaseUCMTarget> val2 = new SystemValidator<ClearCaseUCMTarget>( build2 );
        val2.checkExpectedResult( Result.SUCCESS ).
                checkAction( true ).
                checkCompatability( true ).
                checkTargets( new GitTarget( "test", git.getRepo(), "master", commit2.getName() , false ) ).
                checkContent( new File( filePath, "text.txt" ), "2" ).
                validate();

        AbstractBuild<?, ?> build3 = crRule.buildProject( project.getJenkinsProject(), false, null );

        SystemValidator<ClearCaseUCMTarget> val3 = new SystemValidator<ClearCaseUCMTarget>( build3 );
        val3.checkExpectedResult( Result.NOT_BUILT ).validate();
        git.cleanup();
    }

    @Test
    public void basicTag() throws IOException, GitAPIException, InterruptedException {
        git.initialize(folder.newFolder());
        RevCommit commit1 = git.createCommit("text.txt", "1");
        String tagName = "configRotator";
        git.createTag(tagName);

        ProjectBuilder builder = new ProjectBuilder(new Git(new ArrayList<GitTarget>())).setName("git-test-04");
        ConfigRotatorProject project = builder.getProject();
        project.addTarget(new GitTarget("test", git.getRepo(), "master", tagName, false));

        AbstractBuild<?, ?> build = crRule.buildProject(project.getJenkinsProject(), false, null);

        FilePath path = new FilePath(project.getJenkinsProject().getLastBuiltOn().getWorkspaceFor((FreeStyleProject) project.getJenkinsProject()), "test");
        File filePath = new File(path.toURI());

        SystemValidator<ClearCaseUCMTarget> val = new SystemValidator<ClearCaseUCMTarget>(build);
        val.checkExpectedResult(Result.SUCCESS).
                checkAction(true).
                checkCompatability(true).
                checkTargets(new GitTarget("test", git.getRepo(), "master", commit1.getName(), false)).
                checkContent(new File(filePath, "text.txt"), "1").
                validate();
        git.cleanup();
    }
}
