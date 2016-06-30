/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.scm.contribute;

import hudson.model.TaskListener;
import hudson.remoting.Callable;
import net.praqma.jenkins.configrotator.ConfigurationRotator;
import org.jenkinsci.plugins.compatibilityaction.CompatibilityDataException;
import org.jenkinsci.plugins.compatibilityaction.CompatibilityDataProvider;
import org.jenkinsci.remoting.RoleChecker;
/**
 *
 * @author Mads
 */
public class RemoteCompatabilityContributor implements Callable<Boolean, CompatibilityDataException> {

    public final CompatabilityCompatible item;
    public final CompatibilityDataProvider provider;
    public final TaskListener listener;

    public RemoteCompatabilityContributor(CompatabilityCompatible item, CompatibilityDataProvider provider, TaskListener listner) {
        this.item = item;
        this.provider = provider;
        this.listener = listner;
    }

    @Override
    public Boolean call() throws CompatibilityDataException {
        listener.getLogger().println(ConfigurationRotator.LOGGERNAME+provider);
        provider.create(item, listener);
        listener.getLogger().println(ConfigurationRotator.LOGGERNAME+"Done writing compatability object to database");
        return true;
    }

    @Override
    public void checkRoles(RoleChecker rc) throws SecurityException {
        //NO-OP
    }

}
