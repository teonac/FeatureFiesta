package com.example.app.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Model class representing a Person in the system
 */
public class Person {
    private UUID id;
    private String source;
    private String bsn;
    private String anpId;
    private String lastName;
    private String lastNamePrefix;
    private String initials;
    private String indicationOfNameUsage;
    private String nameNotificationPrefix;
    private String salutationNotification;
    private String nameNotification;
    private String genderDesignation;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private int indicationSecret;
    private String phone;
    private String email;
    private String localIdNen3610;
    private UUID authorizedPersonId;
    private String civisionSubnum;

    // Default constructor
    public Person() {
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getAnpId() {
        return anpId;
    }

    public void setAnpId(String anpId) {
        this.anpId = anpId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNamePrefix() {
        return lastNamePrefix;
    }

    public void setLastNamePrefix(String lastNamePrefix) {
        this.lastNamePrefix = lastNamePrefix;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getIndicationOfNameUsage() {
        return indicationOfNameUsage;
    }

    public void setIndicationOfNameUsage(String indicationOfNameUsage) {
        this.indicationOfNameUsage = indicationOfNameUsage;
    }

    public String getNameNotificationPrefix() {
        return nameNotificationPrefix;
    }

    public void setNameNotificationPrefix(String nameNotificationPrefix) {
        this.nameNotificationPrefix = nameNotificationPrefix;
    }

    public String getSalutationNotification() {
        return salutationNotification;
    }

    public void setSalutationNotification(String salutationNotification) {
        this.salutationNotification = salutationNotification;
    }

    public String getNameNotification() {
        return nameNotification;
    }

    public void setNameNotification(String nameNotification) {
        this.nameNotification = nameNotification;
    }

    public String getGenderDesignation() {
        return genderDesignation;
    }

    public void setGenderDesignation(String genderDesignation) {
        this.genderDesignation = genderDesignation;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public int getIndicationSecret() {
        return indicationSecret;
    }

    public void setIndicationSecret(int indicationSecret) {
        this.indicationSecret = indicationSecret;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocalIdNen3610() {
        return localIdNen3610;
    }

    public void setLocalIdNen3610(String localIdNen3610) {
        this.localIdNen3610 = localIdNen3610;
    }

    public UUID getAuthorizedPersonId() {
        return authorizedPersonId;
    }

    public void setAuthorizedPersonId(UUID authorizedPersonId) {
        this.authorizedPersonId = authorizedPersonId;
    }

    public String getCivisionSubnum() {
        return civisionSubnum;
    }

    public void setCivisionSubnum(String civisionSubnum) {
        this.civisionSubnum = civisionSubnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", bsn='" + bsn + '\'' +
                ", anpId='" + anpId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastNamePrefix='" + lastNamePrefix + '\'' +
                ", initials='" + initials + '\'' +
                ", genderDesignation='" + genderDesignation + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", indicationSecret=" + indicationSecret +
                '}';
    }
}