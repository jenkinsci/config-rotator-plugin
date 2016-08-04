package net.praqma.jenkins.configrotator;

import hudson.model.Describable;
import java.io.Serializable;

public abstract class AbstractTarget<T extends Describable<T>> implements Describable<T>, Serializable {

}
