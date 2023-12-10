package com.senior.kilde.assignment.ddl;

import com.senior.cyber.frmk.metamodel.XmlUtility;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class RepairProgram {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        File folder = new File("kilde-assignment-ddl");
        File input = new File(folder, "src/main/resources/liquibase");
        File output = new File(folder, "src/main/resources/liquibase_output");

        // it will generate process the xml in input file to output folder
        // and start run DbApplication, it will init db from the output folder
        XmlUtility.process(input, output);
    }

}
