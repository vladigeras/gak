package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.iate.gak.service.TexService;
import ru.iate.gak.util.TranslitUtil;

import java.io.*;
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

        String lastName = params.get("@studentLastname");
        String fileName = String.valueOf(new Random().nextInt(100));
        if (lastName != null) {
            fileName += TranslitUtil.cyr2latin(lastName);
        }

        try {
            //get string from template, replace data and save in new file
            BufferedReader template = new BufferedReader(new InputStreamReader(new
                    FileInputStream(workingDirectory.getAbsolutePath() + File.separator + this.templateFile), "utf-8"));
            BufferedWriter result = new BufferedWriter(new OutputStreamWriter(new
                    FileOutputStream(tempDirectory.getAbsolutePath() + File.separator + fileName + ".tex"), "utf-8"));

            String line;
            while ((line = template.readLine()) != null) {
                final String[] resultLine = {line};
                params.forEach((k, v) -> {
                    String replaceOnThis = "";
                    if (v != null && !v.isEmpty()) replaceOnThis = v;
                    resultLine[0] = resultLine[0].replaceAll(k, replaceOnThis);
                });
                result.write(resultLine[0]);
                result.newLine();
            }

            template.close();
            result.close();

            return fileName + ".tex";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в файле");
        }
    }

    private File generateReport(String fileName) throws InterruptedException, IOException {
        Process p = Runtime.getRuntime().exec(this.latexExeFile + " -interaction=nonstopmode -output-directory=" + this.tempDirectory + " " + this.tempDirectory + fileName);
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
