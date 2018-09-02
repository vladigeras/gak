package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.iate.gak.dto.StudentDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.StudentService;
import ru.iate.gak.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/groups")
    @GakSecured(roles = {Roles.ADMIN})
    public List<StudentDto> getStudentsOfGroup(@RequestParam(value = "group") String group) {
        if (StringUtil.isStringNullOrEmptyTrim(group)) throw new RuntimeException("Неверное значение для группы");
        return studentService.getStudentOfCurrentGroup(group).stream().map(StudentDto::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/", consumes = "application/json")
    @GakSecured(roles = {Roles.ADMIN})
    public Long saveStudent(@RequestBody StudentDto studentDto) {
        if (StringUtil.isStringNullOrEmptyTrim(studentDto.firstname) || StringUtil.isStringNullOrEmptyTrim(studentDto.lastname)
                || StringUtil.isStringNullOrEmptyTrim(studentDto.title) || StringUtil.isStringNullOrEmptyTrim(studentDto.executionPlace)) {
            throw new RuntimeException("Имя, фамилия, тема работы, место выполнения не могут быть пустыми");
        }

        if (studentDto.group == null || StringUtil.isStringNullOrEmptyTrim(studentDto.group.title))
            throw new RuntimeException("Не выбрана группа");
        if (studentDto.mentor == null || studentDto.reviewer == null || studentDto.mentor.id == null || studentDto.reviewer.id == null)
            throw new RuntimeException("Выберите руководителя и рецензента");

        return studentService.saveStudent(studentDto);
    }

    @PostMapping(value = "/files", consumes = "multipart/form-data")
    @GakSecured(roles = {Roles.ADMIN})
    public void saveFiles(@RequestParam(name = "student", required = true) Long id,
                          @RequestParam(name = "reportFile", required = false) MultipartFile reportFile,
                          @RequestParam(name = "presentationFile", required = false) MultipartFile presentationFile) {
        if (id > 0) {
            String fileFormat;
            if (reportFile != null) {
                fileFormat = getLastSplittingStringBySymbol(reportFile.getOriginalFilename(), "\\.");
                if (!fileFormat.equals("pdf"))
                    throw new RuntimeException("Отчет должен быть в pdf формате");
            }

            if (presentationFile != null) {
                fileFormat = getLastSplittingStringBySymbol(presentationFile.getOriginalFilename(), "\\.");
                if (!fileFormat.equals("pdf"))
                    throw new RuntimeException("Презентация должна быть в pdf формате");
            }

            try {
                studentService.saveFiles(id, reportFile, presentationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw new RuntimeException("Некорректный id");
    }

    @GetMapping(value = "/readFile")
    @GakSecured(roles = {Roles.ADMIN, Roles.MEMBER, Roles.PRESIDENT})
    public void readFile(@RequestParam(name = "student", required = true) Long id,
                         @RequestParam(name = "isReport", required = true) boolean isReport,
                         HttpServletResponse response) {
        if (id > 0) {
            byte[] content = studentService.readFile(id, isReport);
            String contentType = "application/pdf";
            response.setContentType(contentType);
            try (BufferedOutputStream fos = new BufferedOutputStream(response.getOutputStream())) {
                fos.write(content);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw new RuntimeException("Некорректный id");
    }

    @DeleteMapping(value = "/{studentId}")
    @GakSecured(roles = {Roles.ADMIN})
    public void deleteStudent(@PathVariable(value = "studentId") Long id) {
        if (id > 0) {
            studentService.deleteStudent(id);
        } else throw new RuntimeException("Некорректный id");
    }

    /**
     * Split string by symbol and get last splitting
     *
     * @param input  input string (reg. expression)
     * @param symbol splitting symbol
     * @return last splitting if input != null, else return empty
     */
    private String getLastSplittingStringBySymbol(String input, String symbol) {
        if (input != null && symbol != null) {
            Integer splittingCount = input.split(symbol).length - 1;
            return input.split(symbol)[splittingCount];
        } else return "";
    }
}
