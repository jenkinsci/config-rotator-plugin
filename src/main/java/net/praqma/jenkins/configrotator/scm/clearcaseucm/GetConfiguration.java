package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import java.io.File;
import java.io.IOException;

import net.praqma.clearcase.exceptions.ClearCaseException;
import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.exceptions.UnableToInitializeEntityException;
import org.jenkinsci.remoting.RoleChecker;

public class GetConfiguration implements FileCallable<ClearCaseUCMConfigurationComponent> {

    private final String[] units;

    public GetConfiguration( String[] units ) {
        this.units = units.clone();
    }

    @Override
    public ClearCaseUCMConfigurationComponent invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        try {
            if (units == null) {
                throw new IOException("Units for this configuration is null. Should NOT be possible");
            }

            return new ClearCaseUCMConfigurationComponent( units[0].trim(), units[1].trim(), Boolean.parseBoolean(units[2].trim()) );
        } catch (UnableToInitializeEntityException ninitex) {
            //this happens when the baseline has an incorrect pattern.
            String bl = (units != null && units.length > 0) ? units[0] : "No baseline";
            IOException ioe2 = new IOException(String.format("Failed to load baseline from baseline configuration string (%s)%nCheck your component configuration syntax.", bl.trim()), ninitex.getCause());
            throw ioe2;

        } catch( ClearCaseException e ) {
            // ClearCaseException can not be passed through from slave to master
            // but IOException can, so using that one, and packing out later
            IOException ioe = new IOException( e );
            throw ioe;
        }
    }
    
    @Override
    public void checkRoles(RoleChecker rc) throws SecurityException {
        //NO-op
    }
}