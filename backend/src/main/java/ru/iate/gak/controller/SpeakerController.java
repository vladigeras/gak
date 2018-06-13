package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.domain.Status;
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.util.FilesUtil;
import ru.iate.gak.util.StringUtil;
import ru.iate.gak.util.TranslitUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(value = "/speakers")
public class SpeakerController {

    @Autowired
    private SpeakerService speakerService;

    @Value("${template.protocol.temp.directory}")
    String protocolsTempDirectory;

    @PostMapping(value = "/save", consumes = "application/json")
    @GakSecured(roles = {Roles.ADMIN})
    public void saveList(@RequestBody List<SpeakerDto> speakers) {
        speakerService.fillList(speakers.stream().map(SpeakerDto::toSpeaker).collect(Collectors.toList()));
    }

    @GetMapping(value = "/ofGroupOfDay")
    public List<SpeakerDto> getSpeakerListOfGroupToday(@RequestParam(value = "group") String group,
                                                       @RequestParam(value = "date", required = false) Long date) {
        if (StringUtil.isStringNullOrEmptyTrim(group)) throw new RuntimeException("Неверное значение для группы");
        LocalDateTime localDateTime = (date == null) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);

        return speakerService.getSpeakerListOfCurrentGroupOfDay(group, localDateTime).stream().map(SpeakerDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/zippedProtocols")
    @GakSecured(roles = {Roles.SECRETARY})
    public void getZippedSpeakersProtocols(@RequestParam(value = "group") String group,
                                           HttpServletResponse response) {

        if (StringUtil.isStringNullOrEmptyTrim(group)) throw new RuntimeException("Неверное значение для группы");
        List<File> files = speakerService.getSpeakerProtocolsForTodaySpeakersOfGroup(group);

        try {
            response.setContentType("Content-type: text/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + TranslitUtil.cyr2latin(group) + "protocols.zip");

            ServletOutputStream out = response.getOutputStream();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

            for (File file : files) {
                if (file != null) {
                    zos.putNextEntry(new ZipEntry(file.getName()));

                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                    } catch (FileNotFoundException ex) {
                        continue;
                    }

                    BufferedInputStream fif = new BufferedInputStream(fis);

                    int data = 0;
                    while ((data = fif.read()) != -1) {
                        zos.write(data);
                    }
                    fif.close();

                    zos.closeEntry();
                }
            }

            zos.close();

            FilesUtil.deleteDirectory(new File(this.protocolsTempDirectory));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/update")
    @GakSecured(roles = {Roles.PRESIDENT})
    public void updateDiplomStatus(@RequestBody Long speakerId) {

        if (speakerId > 0) {
            speakerService.updateDiplomStatus(speakerId, Status.SPEAKING_TIME);

        }
    }








}
