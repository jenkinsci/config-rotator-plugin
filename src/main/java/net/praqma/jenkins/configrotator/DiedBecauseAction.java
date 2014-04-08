package net.praqma.jenkins.configrotator;

import hudson.model.Action;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cwolfgang
 */
public class DiedBecauseAction implements Action {

    private String cause;
    private Die die;
    private List<? extends AbstractTarget> targets;

    /**
     * @return the targets
     */
    public List<? extends AbstractTarget> getTargets() {
        if(targets == null) {
            return Collections.EMPTY_LIST;
        }
        return targets;
    }

    /**
     * @param targets the targets to set
     */
    public void setTargets(List<AbstractTarget> targets) {
        this.targets = targets;
    }

    public enum Die {
        die,
        survive
    }

    public DiedBecauseAction( String cause, Die die ) {
        this.cause = cause;
        this.die = die;
        this.targets = new ArrayList<AbstractTarget>();
    }
    
    public DiedBecauseAction( String cause, Die die, List<AbstractTarget> targets ) {
        this.cause = cause;
        this.die = die;
        this.targets = targets;
    }

    public boolean died() {
        return die.equals( Die.die );
    }

    public String getCause() {
        return cause;
    }
    
    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Died because";
    }

    @Override
    public String getUrlName() {
        return null;
    }

    @Override
    public String toString() {
        return die + " because \"" + cause + "\"";
    }
}
