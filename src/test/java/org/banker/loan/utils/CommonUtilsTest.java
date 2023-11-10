package org.banker.loan.utils;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.banker.loan.entity.LoanSupportingDocument;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class CommonUtilsTest {
    @Inject
    CommonUtils commonUtils;
    @Test
    public void testSavingLoanSupportingDocumentDetails() throws IOException, SQLException {
        FileUpload supportFile = Mockito.mock(FileUpload.class);
        URL resourceUrl = CommonUtilsTest.class.getClassLoader().getResource("application.properties");
        File file1 = new File(resourceUrl.getPath());
        Path filePath = file1.toPath();
        when(supportFile.fileName()).thenReturn("application.properties");
        when(supportFile.filePath()).thenReturn(filePath);
        LoanSupportingDocument result = commonUtils.savingLoanSupportingDocmentDetails(supportFile);
        assertEquals("application.properties", result.getDocName());
        assertEquals("properties", result.getType());
    }



}