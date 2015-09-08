package net.praqma.jenkins.configrotator;

import javaposse.jobdsl.dsl.Context;
import net.praqma.clearcase.ucm.entities.Project.PromotionLevel;

class ClearCaseTargetJobDslContext implements Context {

    String baseline;

    public void baseline(String value) {
        baseline = value;
    }

    PromotionLevel promotionLevel = PromotionLevel.INITIAL;

    public void promotionLevel(String value) {
        promotionLevel = PromotionLevel.valueOf(value);
    }

    boolean fixed = false;

    public void fixed() {
        fixed = true;
    }

    public void fixed(boolean value) {
        fixed = value;
    }
}
