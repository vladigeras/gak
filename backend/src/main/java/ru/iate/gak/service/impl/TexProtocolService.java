package ru.iate.gak.service.impl;

import de.nixosoft.jlr.JLRConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.iate.gak.service.TexService;
import ru.iate.gak.util.TranslitUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Service
public class TexProtocolService implements TexService {

    @Value("${template.files.directory}")
    String templateFilesDirectory;

    @Value("${template.protocol.temp.directory}")
    String tempDirectory;

    @Value("${template.protocol.name}")
    String templateFile;

    @Value("${pdf.latex.execution.file}")
    String latexExeFile;

    @Override
    public File exportDocuments(Map<String, String> params) {
        File file = null;
        try {
            file = generateReport(fillDataToReport(params));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private String fillDataToReport(Map<String, String> params) throws IOException {
        File workingDirectory = new File(this.templateFilesDirectory);

        File tempDirectory = new File(this.tempDirectory);
        if (!tempDirectory.isDirectory()) {
            tempDirectory.mkdir();
        }

        String lastName = params.get("lastname");
        String fileName = String.valueOf(new Random().nextInt(100));
        if (lastName != null) {
            fileName += TranslitUtil.cyr2latin(lastName);
        }

        File template = new File(workingDirectory.getAbsolutePath() + File.separator + this.templateFile);
        File result = new File(tempDirectory.getAbsolutePath() + File.separator + fileName + ".tex");

        JLRConverter converter = new JLRConverter(workingDirectory);

        params.forEach((k, v) -> {
            if (v == null || v.isEmpty()) converter.replace(k, "");
            else converter.replace(k, v);
        });

        if (!converter.parse(template, result)) {
            System.out.println(converter.getErrorMessage());
        }

        return result.getName();
    }

    private File generateReport(String fileName) throws InterruptedException, IOException {
        Process p;
        p = Runtime.getRuntime().exec(this.latexExeFile + " -synctex=1 -interaction=nonstopmode -output-directory=" + this.tempDirectory + " " + this.tempDirectory + fileName);
        p.waitFor();

        return selectFile(new File(this.tempDirectory), fileName);
    }

    private File selectFile(File directory, String filename) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().equals(filename.replace(".tex", ".pdf"))) return file;
            }
        }
        return null;
    }
}
