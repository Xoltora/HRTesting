package uz.bdm.HrTesting.service.Impl;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.FileService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Calendar;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.path}")
    private String systemFilePath;

    @Override
    public ResponseData saveFileToDiscWithByte(String fileName, String fileContent) {
        ResponseData result = new ResponseData();
        try {
            Calendar cal = Calendar.getInstance();
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH) + 1;
            String filePath = String.format("/%s/%s", year, month); //year, month
            String _path = systemFilePath + filePath;
            fileExists(_path);
            File dir = new File(_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            _path = _path + "/" + fileName;
            dir = new File(_path);
            FileOutputStream file = new FileOutputStream(dir);
            file.write(Base64.decode(fileContent));
            file.close();

            result.setAccept(true);
            result.setData(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error creating image");
        }

        return result;
    }

    @Override
    public void imageView(String fileName, String filePath, HttpServletRequest req, HttpServletResponse res) throws IOException {
        int length = 0;
        File f = new File(systemFilePath + filePath + "/" + fileName);

        ServletOutputStream op = res.getOutputStream();
        if (f.exists() && (int) f.length() > 0) {
            res.setContentLength((int) f.length());
            FileInputStream inp = new FileInputStream(f);
            byte[] bbuf = new byte[inp.available()];
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, length);
            }
            in.close();
            inp.close();
        }
        op.flush();
        op.close();
    }

    private void fileExists(String filePath) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            try {
                dir.mkdirs();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
