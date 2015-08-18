package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.TaskListener;
import net.praqma.jenkins.configrotator.AbstractConfigurationRotatorSCM;
import net.praqma.jenkins.configrotator.AbstractPostConfigurationRotator;
import net.praqma.jenkins.configrotator.ConfigurationRotatorBuildAction;

@Extension
public class ClearCaseUCMPostBuild extends AbstractPostConfigurationRotator {

    @Override
    public boolean perform(FilePath workspace, TaskListener listener, ConfigurationRotatorBuildAction action) {
        ClearCaseUCMConfiguration current = action.getConfiguration();
        try {
            if (current != null) {                
                String desc = ClearCaseUCMConfiguration.itemizeForHtml(action);
                action.setDescription(desc);
            }
        } catch (Exception ex) {
            listener.getLogger().println("Failed to create description for job: " + ex);
        }
        return true;
    }

    @Override
    public Class<? extends AbstractConfigurationRotatorSCM> tiedTo() {
        return ClearCaseUCM.class;
    }
}
