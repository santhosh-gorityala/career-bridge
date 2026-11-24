package com.jobPortal.careerBridge.entity;

import com.jobPortal.careerBridge.enums.CompanyMembershipStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "company_memberships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_company_company_memberships",
                        columnNames = {"company_id","users_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_company_membership_user",
                        columnList = "users_id"
                ),
                @Index(
                        name = "idx_company_membership_company_status",
                        columnList = "company_id , status"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "users_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private CompanyMembershipStatus status;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;


    public CompanyMembership(
         Company company,
         User user,
         CompanyMembershipStatus status
    ){
      this.company = company;
      this.user = user;
      this.status = status;
      if(status == CompanyMembershipStatus.ACTIVE){
          this.joinedAt = LocalDateTime.now();
      }
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
