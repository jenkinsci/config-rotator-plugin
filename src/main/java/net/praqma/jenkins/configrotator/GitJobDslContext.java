package net.praqma.jenkins.configrotator;

import java.util.ArrayList;
import java.util.List;
import javaposse.jobdsl.dsl.Context;
import static javaposse.jobdsl.plugin.ContextExtensionPoint.executeInContext;
import net.praqma.jenkins.configrotator.scm.git.GitTarget;

class GitJobDslContext implements Context {

    List<GitTarget> targets = new ArrayList<GitTarget>();

    public void target(Runnable closure) {
        GitTargetJobDslContext context = new GitTargetJobDslContext();
        executeInContext(closure, context);

        targets.add(new GitTarget(context.name, context.repository, context.branch, context.commit, context.fixed));
    }

}
