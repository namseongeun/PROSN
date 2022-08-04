package com.ssafy.prosn.controller;

import com.ssafy.prosn.domain.study.StudyGroup;
import com.ssafy.prosn.dto.*;
import com.ssafy.prosn.repository.study.StudyGroupRepository;
import com.ssafy.prosn.repository.study.UserStudyRepository;
import com.ssafy.prosn.service.StudyService;
import com.ssafy.prosn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * created by yeomyeong on 2022/08/01
 * updated by yeomyeong on 2022/08/02
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("api/study")
public class StudyController {

    private final UserService userService;
    private final StudyService studyService;
    private final StudyGroupRepository studyGroupRepository;
    private final UserStudyRepository userStudyRepository;
    //스터디 그룹
    //생성
    @PostMapping
    public ResponseEntity<?> createStudy(@RequestBody @Valid StudyGroupRequestDto dto) {
        UserResponseDto user = userService.getMyInfoBySecret();
        return ResponseEntity.ok(studyService.create(dto, user.getId()));
    }
    //목록 조회
    @GetMapping
    public ResponseEntity<?> studyGroupList() {
        return ResponseEntity.ok(studyGroupRepository.showStudyGroupList());
    }
    //상세내용
    @GetMapping("/{studyid}")
    public ResponseEntity<?> showStudy(@PathVariable(value = "studyid") Long studyid) {
        UserResponseDto user = userService.getMyInfoBySecret();
        return ResponseEntity.ok(studyService.showStudyGroup(user.getId(), studyid));
    }
    //수정
    @PutMapping
    public ResponseEntity<?> updateStudy(@RequestBody @Valid StudyGroupRequestDto dto) {
        StudyGroup studyGroup = studyGroupRepository.findById(dto.getId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다"));
        return ResponseEntity.ok(studyService.update(studyGroup.getId(), dto));
    }
    //삭제
    @DeleteMapping("/{studyid}")
    public ResponseEntity<?> deleteStudy(@PathVariable(value = "studyid") Long studyid) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyid).orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다"));
        studyService.deleteStudy(studyGroup);
        return ResponseEntity.ok("해당 스터디 삭제 완료");
    }

    //내스터디
    //목록 조회
    @GetMapping("/me")
    public ResponseEntity<?>  myStudyGroupList() {
        UserResponseDto user = userService.getMyInfoBySecret();
        return ResponseEntity.ok(userStudyRepository.findByUserId(user.getId()));
    }
    //가입
    @PostMapping("/me")
    public ResponseEntity<?> joinStudy(@RequestBody Long studyid) {
        UserResponseDto user = userService.getMyInfoBySecret();
        StudyGroup studyGroup = studyGroupRepository.findById(studyid).orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다"));
        return ResponseEntity.ok(studyService.joinStudy(user.getId(), studyGroup));
    }
    //탈퇴
    @DeleteMapping("me")
    public ResponseEntity<?> removeStudy(@RequestBody Long studyid) {
        UserResponseDto user = userService.getMyInfoBySecret();
        StudyGroup studyGroup = studyGroupRepository.findById(studyid).orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다"));
        studyService.removeStudy(user.getId(), studyGroup);
        return ResponseEntity.ok("해당 스터디 탈퇴 완료");
    }
}
