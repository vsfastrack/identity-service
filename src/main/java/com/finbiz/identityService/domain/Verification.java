//package com.finbiz.identityService.domain;
//
//import com.finbiz.identityService.enums.VerificationStatusEnum;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "verification")
//public class Verification {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private String verificationReference;
//    private String claimedResource;
//    @Enumerated(EnumType.STRING)
//    private VerificationStatusEnum status;
//    @CreationTimestamp
//    private LocalDateTime dateCreated;
//    @UpdateTimestamp
//    private LocalDateTime lastUpdated;
//
//    public static Verification of(String verificationReference , String claimedResource , VerificationStatusEnum verificationStatusEnum){
//        Verification verification = new Verification();
//        verification.setVerificationReference(verificationReference);
//        verification.setClaimedResource(claimedResource);
//        verification.setStatus(verificationStatusEnum);
//        return  verification;
//    }
//}
