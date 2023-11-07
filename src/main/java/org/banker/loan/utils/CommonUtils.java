package org.banker.loan.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.banker.loan.entity.LoanSupportingDocument;
import org.banker.loan.exception.ErrorCodes;
import org.banker.loan.exception.ServiceException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;


@ApplicationScoped
public class CommonUtils {
    @Inject
    Logger log;
    public LoanSupportingDocument savingLoanSupportingDocmentDetails(FileUpload supportFile) {
        try {
            log.info("Supporting document api called for extratcting data");
            LoanSupportingDocument loanSupportingDocumentEntity = new LoanSupportingDocument();
            loanSupportingDocumentEntity.setDocName(supportFile.fileName());
            int index = supportFile.fileName().lastIndexOf('.');
            loanSupportingDocumentEntity.setType(index > 0 ? supportFile.fileName().substring(index + 1) : "Text");
            Path filePath = supportFile.filePath();
            byte[] fileBytes = Files.readAllBytes(filePath);
            Blob blob = new SerialBlob(fileBytes);
            loanSupportingDocumentEntity.setDocByte(blob);
            log.info("Supporting document data dumpped in dto|fileName|"+supportFile.fileName());
            return loanSupportingDocumentEntity;
        }catch (IOException | SQLException e) {
            log.error("Error: "+e.getStackTrace());
            throw new ServiceException(ErrorCodes.CONNECTION_ISSUE);
        }
    }


}
