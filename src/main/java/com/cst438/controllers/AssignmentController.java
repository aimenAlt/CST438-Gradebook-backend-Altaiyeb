package com.cst438.controllers;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.GradebookDTO;
import com.cst438.services.RegistrationService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class AssignmentController {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    AssignmentGradeRepository assignmentGradeRepository;

    @Autowired
    CourseRepository courseRepository;

//    @Autowired
//    RegistrationService registrationService;

    @PostMapping("/assignment")
    public int getAssignmentsNeedGrading(@RequestBody AssignmentListDTO.AssignmentDTO assignmentDTO) throws ParseException {

        String email = "dwisneski@csumb.edu";
        Assignment newAssignment = new Assignment();
        newAssignment.setCourse(courseRepository.findById(assignmentDTO.courseId));
        newAssignment.setDueDate((Date) new SimpleDateFormat("dd/MM/yyyy").parse(assignmentDTO.dueDate));
        newAssignment.setName(assignmentDTO.assignmentName);
        newAssignment.setNeedsGrading(0);
        newAssignment = assignmentRepository.save(newAssignment);
        return newAssignment.getId();
    }

}
