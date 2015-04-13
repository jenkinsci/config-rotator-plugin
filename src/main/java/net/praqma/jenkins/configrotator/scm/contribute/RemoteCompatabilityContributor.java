/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.scm.contribute;

import hudson.model.TaskListener;
import hudson.remoting.Callable;
import net.praqma.jenkins.configrotator.ConfigurationRotator;
import org.jenkinsci.plugins.externaldata.ExternalDataException;
import org.jenkinsci.plugins.externaldata.ExternalDataProvider;

/**
 *
 * @author Mads
 */
public class RemoteCompatabilityContributor implements Callable<Boolean, ExternalDataException> { 

    public final CompatabilityCompatible item;
    public final ExternalDataProvider provider;
    public final TaskListener listener;

    public RemoteCompatabilityContributor(CompatabilityCompatible item, ExternalDataProvider provider, TaskListener listner) { 
        this.item = item;
        this.provider = provider;
        this.listener = listner;
    }
    
    @Override
    public Boolean call() throws ExternalDataException {
        listener.getLogger().println(ConfigurationRotator.LOGGERNAME+provider);
        provider.create(item, listener);
        listener.getLogger().println(ConfigurationRotator.LOGGERNAME+"Done writing compatability object to database");
        return true;
    }
    
}
