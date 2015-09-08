package net.praqma.jenkins.configrotator;

import javaposse.jobdsl.dsl.Context;

class GitTargetJobDslContext implements Context {

    String name;

    public void name(String value) {
        name = value;
    }

    String repository;

    public void repository(String value) {
        repository = value;
    }

    String branch;

    public void branch(String value) {
        branch = value;
    }

    String commit;

    public void commit(String value) {
        commit = value;
    }

    boolean fixed = false;

    public void fixed() {
        fixed = true;
    }

    public void fixed(boolean value) {
        fixed = value;
    }

}
