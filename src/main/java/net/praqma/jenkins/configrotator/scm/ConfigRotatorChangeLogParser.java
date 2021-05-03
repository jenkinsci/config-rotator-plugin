package net.praqma.jenkins.configrotator.scm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogParser;
import hudson.scm.ChangeLogSet;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Praqma
 */
public class ConfigRotatorChangeLogParser extends ChangeLogParser {
    private static final Logger LOGGER = Logger.getLogger( ConfigRotatorChangeLogParser.class.getName() );

    @Override
    public ChangeLogSet<? extends ChangeLogSet.Entry> parse( AbstractBuild build, File changelogFile ) throws IOException, SAXException {
        Digester digester = new Digester();
        if (!Boolean.getBoolean(this.getClass().getName() + ".UNSAFE")) {
            digester.setXIncludeAware(false);
            try {
                digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                digester.setFeature("http://xml.org/sax/features/external-general-entities", false);
                digester.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                digester.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            } catch (ParserConfigurationException ex) {
                throw new SAXException("Failed to securely configure xml digester parser", ex);
            }
        }
        List<ConfigRotatorChangeLogEntry> changesetList = new ArrayList<>();
        digester.push( changesetList );
        digester.addObjectCreate( "*/changelog/commit", ConfigRotatorChangeLogEntry.class );
        digester.addSetProperties( "*/changelog/commit" );
        digester.addBeanPropertySetter( "*/changelog/commit/user" );
        digester.addBeanPropertySetter( "*/changelog/commit/commitMessage" );
        digester.addObjectCreate( "*/changelog/commit/versions/version/", ConfigRotatorVersion.class );
        digester.addBeanPropertySetter( "*/changelog/commit/versions/version/name" );
        digester.addBeanPropertySetter( "*/changelog/commit/versions/version/user" );
        digester.addBeanPropertySetter( "*/changelog/commit/versions/version/file" );

        digester.addSetNext( "*/changelog/commit/versions/version", "addVersion" );
        digester.addSetNext( "*/changelog/commit", "add" );
        try {

            try(InputStreamReader reader = new InputStreamReader(new FileInputStream(changelogFile), "utf-8")) {
                digester.parse( reader );
            }
        } catch( SAXException sex ) {
            LOGGER.log(Level.WARNING, "SAXException caught. Trace written.", sex);
            return new ConfigRotatorChangeLogSet( build );
        }

        ConfigRotatorChangeLogSet clogSet = new ConfigRotatorChangeLogSet( build, changesetList );

        return clogSet;

    }
}
