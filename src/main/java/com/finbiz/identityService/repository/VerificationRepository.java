//package com.finbiz.identityService.repository;
//
//import com.finbiz.identityService.domain.Verification;
//import com.finbiz.identityService.enums.VerificationStatusEnum;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import javax.transaction.Transactional;
//
//@Repository
//public interface VerificationRepository extends JpaRepository<Verification , Long> {
//    Long countByVerificationReferenceAndClaimedResourceAndStatus(String verificationRefId , String claimedResource , VerificationStatusEnum status);
//    @Modifying
//    @Transactional
//    @Query("update Verification v set v.status = ?1 where v.verificationReference  = ?2")
//    void upateVerificationStatus(VerificationStatusEnum verificationStatusEnum , String referenceId);
//    @Modifying
//    @Transactional
//    @Query("delete from Verification v where v.claimedResource = ?1")
//    void deleteVerificationByClaimedResource(String claimedResourceId);
//}
