package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import java.io.IOException;
import net.praqma.clearcase.ucm.entities.UCMEntity;
import hudson.FilePath;

//TODO: This is also used in CCUCM. We cannot easily put this in a shared library as it includes references to the Jenkins object model. 
public abstract class RemoteUtil {
    
    private RemoteUtil() { }

    public static UCMEntity loadEntity(FilePath workspace, UCMEntity entity, boolean slavePolling) throws IOException, InterruptedException {
        if (slavePolling) {
            return workspace.act(new LoadEntity(entity));
        } else {
            LoadEntity t = new LoadEntity(entity);
            return t.invoke(null, null);
        }
    }
}
