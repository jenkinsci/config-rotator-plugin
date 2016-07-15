/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator.scm.contribute;

import hudson.model.Action;
import java.io.Serializable;

/**
 * We follow the notion that each action in Jenkins should be able to tell us something about the compatibility
 * of a given configuration in a given context.
 * @author Mads
 * @param <T> Action to convert
 */
public interface ConfigRotatorCompatabilityConverter<T extends Action> extends Serializable {
    public CompatabilityCompatible convert(T t);
}
