package net.praqma.jenkins.configrotator;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jgit.transport.RefSpec;

/**
 * @author cwolfgang
 */
public class GitRule implements TestRule {

    private static Logger logger = Logger.getLogger( GitRule.class.getName() );

    private File gitPath;
    private Repository gitRepo;
    private Git git;

    public void initialize( File gitPath ) {
        this.gitPath = gitPath;

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            gitRepo = builder.setGitDir( new File( gitPath, ".git" ) ).build();
            gitRepo.create();
        } catch( IOException e ) {
            e.printStackTrace();
        }
        git = new Git( gitRepo );
    }

    public void cleanup() {
        if(gitRepo != null)
            for(int i = 0; i < 5; i++)
                gitRepo.close(); // usage counter shenanigans
    }

    public RevCommit createCommit( String filename, String content ) throws IOException, GitAPIException {

        File file = new File( gitPath, filename );
        boolean create = !file.exists();
        FileUtils.write( file, content, false );

        git.add().addFilepattern( filename ).call();
        return git.commit().setMessage( ( create ? "Creating " : "Updating" ) + " " + filename ).setAll( true ).call();
    }

    public Ref createTag(String tagName) throws GitAPIException {
        return git.tag().setName(tagName).call();
    }

    public boolean tagExists(String tagname) throws GitAPIException  {
        List<Ref> list = git.tagList().call();
        for(Ref r : list) {
            if(r.getName().equals(tagname)) {
                return true;
            }
        }
        return false;
    }

    public String getRepo() {
        return gitRepo.getDirectory().getAbsolutePath();
    }


    @Override
    public Statement apply( final Statement base, Description description ) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }

    protected void listPath( File path ) {
        logger.info( "Listing " + path + "(" + path.exists() + ")" );
        for( String f : path.list() ) {
            logger.info( " * " + f );
        }
    }
    
}
