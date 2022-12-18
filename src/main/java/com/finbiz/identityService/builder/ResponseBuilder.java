package com.finbiz.identityService.builder;

import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.exception.BusinessException;
import com.finbiz.transactionmanager.api.spec.model.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class ResponseBuilder {
    @Autowired
    private HttpServletRequest request;

    public void populateTransactionDetails(BaseApiResponse baseApiResponse){
        baseApiResponse.setTimestamp(LocalDateTime.now().toString());
        baseApiResponse.setStatus(!CollectionUtils.isEmpty(baseApiResponse.getErrors()) ?
                BaseApiResponse.StatusEnum.ERROR : BaseApiResponse.StatusEnum.OK);
        baseApiResponse.setContextId((String) request.getAttribute(ApiConstants.X_TRANSACTION_ID));
    }

    public <T extends BaseApiResponse> ResponseEntity<T> buildApiResponse(T response){
        switch(response.getStatusCode()){
            case 201:
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            case 200:
                return ResponseEntity.status(HttpStatus.OK).body(response);
            case 204:
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            case 400:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            case 404:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            case 409:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            case 500:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            default:
                throw BusinessException.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR , ErrorMsgConstants.UNEXPECTED_ERR_MSG);
        }
    }

}
