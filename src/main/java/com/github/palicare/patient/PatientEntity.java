package com.github.palicare.patient;

import com.github.palicare.contact.ContactEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "patient")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String religion;
    private String diet;
    private String nationality;
    private String profilePicturePath;
    private byte careLevel;
    private String misc;
    private int roomNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private ContactEntity contact;

    @ElementCollection
    @CollectionTable(name = "patient_allergies", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    @ElementCollection
    @CollectionTable(name = "patient_care_needs", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "care_need")
    private List<String> careNeeds;

    @ElementCollection
    @CollectionTable(name = "patient_symptoms", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "symptom")
    private List<String> symptoms;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setCareLevel(byte careLevel) {
        this.careLevel = careLevel;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setContact(ContactEntity contact) {
        this.contact = contact;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public void setCareNeeds(List<String> careNeeds) {
        this.careNeeds = careNeeds;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getReligion() {
        return religion;
    }

    public String getDiet() {
        return diet;
    }

    public String getNationality() {
        return nationality;
    }

    public byte getCareLevel() {
        return careLevel;
    }

    public String getMisc() {
        return misc;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public ContactEntity getContact() {
        return contact;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getCareNeeds() {
        return careNeeds;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }
}
