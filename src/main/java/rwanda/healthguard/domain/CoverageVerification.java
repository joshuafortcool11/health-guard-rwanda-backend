package rwanda.healthguard.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "coverage_verification")
public class CoverageVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String healthCenter;

    private String remarks;

    private int dependents;

    private LocalDate verifiedDate;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(String healthCenter) {
        this.healthCenter = healthCenter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getDependents() {
        return dependents;
    }

    public void setDependents(int dependents) {
        this.dependents = dependents;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
}
