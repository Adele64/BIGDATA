package org.example;
import java.io.Serializable;
import java.util.Objects;

public class Etudiant implements Serializable {
    private String firstName;
    private String lastName;
    private int age;
    private String engineeringDegree;

    public Etudiant() { }

    public Etudiant(String firstName, String lastName, int age, String engineeringDegree) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.engineeringDegree = engineeringDegree;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getEngineeringDegree() { return engineeringDegree; }
    public void setEngineeringDegree(String engineeringDegree) { this.engineeringDegree = engineeringDegree; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etudiant etudiant = (Etudiant) o;
        return age == etudiant.age &&
                Objects.equals(firstName, etudiant.firstName) &&
                Objects.equals(lastName, etudiant.lastName) &&
                Objects.equals(engineeringDegree, etudiant.engineeringDegree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, engineeringDegree);
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", engineeringDegree='" + engineeringDegree + '\'' +
                '}';
    }
}