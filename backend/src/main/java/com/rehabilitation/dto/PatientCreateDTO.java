package com.rehabilitation.dto;

import java.time.LocalDate;

/**
 * 患者创建DTO - 用于接收前端提交的完整患者信息
 * 注意：由于Lombok在本项目中存在编译问题，手动添加getter/setter方法
 */
public class PatientCreateDTO {

    // ========== 基本信息 ==========
    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身高（cm）
     */
    private Integer height;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 联系地址（省市区）
     */
    private String contactAddress;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 备注说明
     */
    private String remarks;

    // ========== 入组信息 ==========
    /**
     * 档案号（系统自动生成，前端传null即可）
     */
    private String medicalRecordNo;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 居住地址
     */
    private String address;

    // ========== 手动添加的 Getter 和 Setter 方法 ==========

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getEnrollmentInstitution() {
        return enrollmentInstitution;
    }

    public void setEnrollmentInstitution(String enrollmentInstitution) {
        this.enrollmentInstitution = enrollmentInstitution;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
