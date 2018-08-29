package com.zxh.myBlog.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static void zipFolder(String bkAttachDir, String attachPath) throws Exception{
		// TODO Auto-generated method stub
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		
		fileWriter = new FileOutputStream(attachPath);
		zip = new ZipOutputStream(fileWriter);
		
		addFolderToZip("", bkAttachDir, zip);
        zip.flush();
        zip.close();
	}

	public static void addFileToZip(String path, String srcFile, ZipOutputStream zip)
            throws Exception {

        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
        }
    }

    public static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
        File folder = new File(srcFolder);
        if (null != path && folder.isDirectory()) {
            String[] fileList = folder.list();
            if (fileList != null) {
                for (String fileName : fileList) {
                    if (path.equals("")) {
                        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
                    } else {
                        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
                    }
                }
            }
        }
    }

}

	
