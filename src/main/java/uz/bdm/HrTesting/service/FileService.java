package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {

    ResponseData saveFileToDiscWithByte( String fileName, String fileContent);

    void imageView(String fileName, String filePath, HttpServletRequest req, HttpServletResponse res) throws IOException;


}
