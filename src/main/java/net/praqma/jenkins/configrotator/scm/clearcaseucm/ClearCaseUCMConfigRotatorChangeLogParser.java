package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import java.io.*;
import net.praqma.jenkins.configrotator.scm.ConfigRotatorChangeLogParser;
import org.xml.sax.SAXException;

/**
 * Keps for backwards compatibility.
 * @author Praqma
 */
public class ClearCaseUCMConfigRotatorChangeLogParser extends ConfigRotatorChangeLogParser {
    @Override
    public ChangeLogSet<? extends Entry> parse(AbstractBuild build, File changelogFile) throws IOException, SAXException {
        return null;
    }
}