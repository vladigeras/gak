package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.iate.gak.dto.StudentDto;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.StudentEntity;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.DiplomRepository;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.repository.StudentRepository;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.StudentService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private DiplomRepository diplomRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public List<StudentEntity> getStudentOfCurrentGroup(String group) {
		GroupEntity groupEntity = groupRepository.findById(group)
				.orElseThrow(() -> new RuntimeException("Группа с названием " + group + "  не найдена"));
		return studentRepository.getAllByGroupAndDeletedTimeIsNull(groupEntity);
	}

	@Override
	@Transactional
	public Long saveStudent(StudentDto student) {
		GroupEntity groupEntity = groupRepository.findById(student.group.title)
				.orElseThrow(() -> new RuntimeException("Группа с названием " + student.group.title + "  не найдена"));

		UserEntity mentorEntity = userRepository.findById(student.mentor.id)
				.orElseThrow(() -> new RuntimeException("Руководитель с id " + student.mentor.id + "  не найден"));

		UserEntity reviewerEntity = userRepository.findById(student.reviewer.id)
				.orElseThrow(() -> new RuntimeException("Рецензент с id " + student.reviewer.id + "  не найден"));

		StudentEntity studentEntity = null;
		DiplomEntity diplomEntity = null;
		if (student.id != null) {
			studentEntity = studentRepository.findById(student.id)
					.orElseThrow(() -> new RuntimeException("Студент с id " + student.id + " не найден"));

			if (studentEntity.getDiplom() != null) {
				diplomEntity = studentEntity.getDiplom();
			} else throw new RuntimeException("Произошла ошибка: у студента id = " + student.id + " не найден диплом");
		} else {
			studentEntity = new StudentEntity();
			diplomEntity = new DiplomEntity();
		}

		studentEntity.setFirstname(student.firstname);
		studentEntity.setLastname(student.lastname);
		studentEntity.setMiddlename(student.middlename);
		studentEntity.setGender(student.gender);
		studentEntity.setGroup(groupEntity);
		studentRepository.save(studentEntity);

		diplomEntity.setTitle(student.title);
		diplomEntity.setExecutionPlace(student.executionPlace);
		diplomEntity.setMentor(mentorEntity);
		diplomEntity.setReviewer(reviewerEntity);
		diplomEntity.setReport(student.report);
		diplomEntity.setPresentation(student.presentation);
		diplomEntity.setStudent(studentEntity);
		diplomRepository.save(diplomEntity);

		return studentEntity.getId();
	}

	@Override
	@Transactional
	public void saveFiles(Long studentId, MultipartFile reportFile, MultipartFile presentationFile) throws IOException {
		StudentEntity studentEntity = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Студент с id = " + studentId + "  не найден"));

		studentEntity.getDiplom().setReport(reportFile == null ? null : reportFile.getBytes());
		studentEntity.getDiplom().setPresentation(presentationFile == null ? null : presentationFile.getBytes());
		studentRepository.save(studentEntity);
	}

	@Override
	@Transactional
	public byte[] readFile(Long studentId, boolean isReport) {
		StudentEntity studentEntity = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Студент с id = " + studentId + "  не найден"));

		if (isReport) {
			if (studentEntity.getDiplom().getReport() != null) {
				return studentEntity.getDiplom().getReport();
			} else throw new RuntimeException("У этого студента еще не загружен данный фаил");

		} else {
			if (studentEntity.getDiplom().getPresentation() != null) {
				return studentEntity.getDiplom().getPresentation();
			} else throw new RuntimeException("У этого студента еще не загружен данный фаил");
		}
	}

	@Override
	@Transactional
	public void deleteStudent(Long id) {
		StudentEntity studentEntity = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Студент с id = " + id + "  не найден"));

		studentEntity.setDeletedTime(LocalDateTime.now(ZoneOffset.UTC));
		studentRepository.save(studentEntity);
	}
}
