package com.nexentire.rental.util.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.nexentire.rental.re.vo.RTREExportEnum.FileExtType;


@Component
public class CreateFileCompression {

	public CreateFileCompression() {
		// TODO Auto-generated constructor stub
	}

	//zip파일 생성 메서드
	public static void createZipInDirectory(String dir, String zipfile)
	   throws IOException, IllegalArgumentException {
		 
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		try{
            File f = new File(dir);
        
            String[] zipList = f.list();

            //zip 객체 생성
            zos = new ZipOutputStream(new FileOutputStream(dir + "/" + zipfile + "." + FileExtType.ZIP));
            
            for(String zip : zipList){
                File f2 = new File(f,zip);
                fis = new FileInputStream(f2);
                byte[] buffer = new byte[1024];
                zos.putNextEntry(new ZipEntry(f2.getName()));

                int len;
                while((len = fis.read(buffer)) > 0 ){
                    zos.write(buffer,0,len);
                }

                zos.closeEntry();
                fis.close();
            }
            zos.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            fis.close();
            zos.close();
        }

	}

}
