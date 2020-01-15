/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * 字符串压缩/解压缩工具类
 * @author zhaoj
 * @version ZipUtil.java, v 0.1 2019-03-13 11:44
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 以gzip的方式压缩字符串
     *
     * @param str 需要压缩的字符串
     * @return 压缩后的字符串
     */
    public static String compress(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            return out.toString(StandardCharsets.ISO_8859_1.name());
        } catch (IOException e) {
            logger.error("compress str occurs exception", e);
        }
        return str;
    }

    /**
     * 将gzip压缩形式的字符串解压
     *
     * @param str gzip格式压缩的字符串
     * @return 解压缩后的字符串
     */
    public static String unCompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            logger.error("unCompress str occurs exception", e);
        }
        return str;
    }

    /**
     * zip压缩文件
     * @param dir
     * @param zippath
     */
    public static void zip(String dir ,String zippath){
        List<String> paths = getFiles(dir);
        compressFilesZip(paths.toArray(new String[paths.size()]),zippath,dir);
    }
    /**
     * 递归取到当前目录所有文件
     * @param dir
     * @return
     */
    public static List<String> getFiles(String dir){
        List<String> lstFiles = null;
        if(lstFiles == null){
            lstFiles = new ArrayList<String>();
        }
        File file = new File(dir);
        File [] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                lstFiles.add(f.getAbsolutePath());
                lstFiles.addAll(getFiles(f.getAbsolutePath()));
            }else{
                String str =f.getAbsolutePath();
                lstFiles.add(str);
            }
        }
        return lstFiles;
    }

    /**
     * 文件名处理
     * @param dir
     * @param path
     * @return
     */
    public static String getFilePathName(String dir,String path){
        String p = path.replace(dir+ File.separator, "");
        p = p.replace("\\", "/");
        return p;
    }

    /**
     * 把文件压缩成zip格式
     * @param files         需要压缩的文件
     * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
     */
    public static void compressFilesZip(String[] files,String zipFilePath,String dir) {
        if(files == null || files.length <= 0) {
            return ;
        }
        ZipArchiveOutputStream zaos = null;
        try {
            File zipFile = new File(zipFilePath);
            zaos = new ZipArchiveOutputStream(zipFile);
            zaos.setUseZip64(Zip64Mode.AsNeeded);
            //将每个文件用ZipArchiveEntry封装
            //再用ZipArchiveOutputStream写到压缩文件中
            for(String strfile : files) {
                File file = new File(strfile);
                if(file != null) {
                    String name = getFilePathName(dir,strfile);
                    ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file,name);
                    zaos.putArchiveEntry(zipArchiveEntry);
                    if(file.isDirectory()){
                        continue;
                    }
                    InputStream is = null;
                    try {
                        is = new BufferedInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[1024 ];
                        int len = -1;
                        while((len = is.read(buffer)) != -1) {
                            //把缓冲区的字节写入到ZipArchiveEntry
                            zaos.write(buffer, 0, len);
                        }
                        zaos.closeArchiveEntry();
                    }catch(Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        if(is != null) {
                            is.close();
                        }
                    }

                }
            }
            zaos.finish();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                if(zaos != null) {
                    zaos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * 把zip文件解压到指定的文件夹
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/" ()
     */
    public static void unzip(String zipFilePath, String saveFileDir) {
        if(!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/") ){
            saveFileDir += File.separator;
        }
        File dir = new File(saveFileDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(zipFilePath);
        if (file.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is);
                ArchiveEntry archiveEntry = null;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if(entryFileName.endsWith("/")){
                            entryFile.mkdirs();
                        }else{
                            os = new BufferedOutputStream(new FileOutputStream(
                                    entryFile));
                            byte[] buffer = new byte[1024 ];
                            int len = -1;
                            while((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zais != null) {
                        zais.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 把zip文件解压到指定的文件夹(包含编码格式)
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/" ()
     */
    public static void unzipEcode(String zipFilePath, String saveFileDir,String ecoding) {
        if(!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/") ){
            saveFileDir += File.separator;
        }
        File dir = new File(saveFileDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(zipFilePath);
        if (file.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is,ecoding);
                ArchiveEntry archiveEntry = null;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if(entryFileName.endsWith("/")){
                            entryFile.mkdirs();
                        }else{
                            os = new BufferedOutputStream(new FileOutputStream(
                                    entryFile));
                            byte[] buffer = new byte[1024 ];
                            int len = -1;
                            while((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zais != null) {
                        zais.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 移除路径下文件
     *
     * @param path
     */
    public static void removeFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    public static void main(String[] args) {
        String str = "action://store(/tmp/testshell0.sh(IyEvYmluL2Jhc2gKICAgIGRhdGU7CiAgICBlY2hvICJ1cHRpbWU6IgogICAgdXB0aW1lCiAgICBlY2hvICJDdXJyZW50bHkgY29ubmVjdGVkOiIKICAgIHcKICAgIGVjaG8gIi0tLS0tLS0tLS0tLS0tLS0tLS0tIgogICAgZWNobyAiTGFzdCBsb2dpbnM6IgogICAgbGFzdCAtYSB8aGVhZCAtMwogICAgZWNobyAiLS0tLS0tLS0tLS0tLS0tLS0tLS0iCiAgICBlY2hvICJEaXNrIGFuZCBtZW1vcnkgdXNhZ2U6IgogICAgZGYgLWggfCB4YXJncyB8IGF3ayAne3ByaW50ICJGcmVlL3RvdGFsIGRpc2s6ICIgJDExICIgLyAiICQ5fScKICAgIGZyZWUgLW0gfCB4YXJncyB8IGF3ayAne3ByaW50ICJGcmVlL3RvdGFsIG1lbW9yeTogIiAkMTcgIiAvICIgJDggIiBNQiJ9JwogICAgZWNobyAiLS0tLS0tLS0tLS0tLS0tLS0tLS0iCiAgICBzdGFydF9sb2c9YGhlYWQgLTEgL3Zhci9sb2cvbWVzc2FnZXMgfGN1dCAtYyAxLTEyYAogICAgb29tPWBncmVwIC1jaSBraWxsIC92YXIvbG9nL21lc3NhZ2VzYAogICAgZWNobyAtbiAiT09NIGVycm9ycyBzaW5jZSAkc3RhcnRfbG9nIDoiICRvb20KICAgIGVjaG8gIiIKICAgIGVjaG8gIi0tLS0tLS0tLS0tLS0tLS0tLS0tIgogICAgZWNobyAiVXRpbGl6YXRpb24gYW5kIG1vc3QgZXhwZW5zaXZlIHByb2Nlc3NlczoiCiAgICB0b3AgLWIgfGhlYWQgLTMKICAgIGVjaG8KICAgICAgICB0b3AgLWIgfGhlYWQgLTEwIHx0YWlsIC00CiAgICBlY2hvICItLS0tLS0tLS0tLS0tLS0tLS0tLSIKICAgIGVjaG8gIk9wZW4gVENQIHBvcnRzOiIKICAgIG5tYXAgLXAtIC1UNCAxMjcuMC4wLjEKICAgIGVjaG8gIi0tLS0tLS0tLS0tLS0tLS0tLS0tIgogICAgZWNobyAiQ3VycmVudCBjb25uZWN0aW9uczoiCiAgICBzcyAtcwogICAgZWNobyAiLS0tLS0tLS0tLS0tLS0tLS0tLS0iCiAgICBlY2hvICJwcm9jZXNzZXM6IgogICAgcHMgYXV4ZiAtLXdpZHRoPTIwMAogICAgZWNobyAiLS0tLS0tLS0tLS0tLS0tLS0tLS0iCiAgICBlY2hvICJ2bXN0YXQ6IgogICAgdm1zdGF0IDEgNSA=))store(/tmp/testshell1.sh(IyEvYmluL2Jhc2gKIyBjaGVja194dS5zaAojIDAgKiAqICogKiAvaG9tZS9jaGVja194dS5zaAoKREVMQVk9MQpDT1VOVD0xCiMgd2hldGhlciB0aGUgcmVzcG9uc2libGUgZGlyZWN0b3J5IGV4aXN0CiMgZ2VuZXJhbCBjaGVjawpleHBvcnQgVEVSTT1saW51eAovdXNyL2Jpbi90b3AgLWIgLWQgJHtERUxBWX0gLW4gJHtDT1VOVH0KIyBjcHUgY2hlY2sKL3Vzci9iaW4vc2FyIC11ICR7REVMQVl9ICR7Q09VTlR9CiMvdXNyL2Jpbi9tcHN0YXQgLVAgMCAke0RFTEFZfSAke0NPVU5UfQojL3Vzci9iaW4vbXBzdGF0IC1QIDEgJHtERUxBWX0gJHtDT1VOVH0KIyBtZW1vcnkgY2hlY2sKL3Vzci9iaW4vdm1zdGF0ICR7REVMQVl9ICR7Q09VTlR9CiMgSS9PIGNoZWNrCi91c3IvYmluL2lvc3RhdCAke0RFTEFZfSAke0NPVU5UfQojIG5ldHdvcmsgY2hlY2sKL3Vzci9iaW4vc2FyIC1uIERFViAke0RFTEFZfSAke0NPVU5UfQojL3Vzci9iaW4vc2FyIC1uIEVERVYgJHtERUxBWX0gJHtDT1VOVH0=))store(/tmp/testshell2.sh(IyEvYmluL3NoCmludD0xCndoaWxlKCggJGludDw9MTAwICkpCiAgZG8KICAgIGRhdGUKICAgIGxldCAiaW50KysiCmRvbmUg))";

        String compressedStr = ZipUtil.compress(str);
        System.out.println(compressedStr);

        String unCompressStr = ZipUtil.unCompress(compressedStr);
        System.out.println(unCompressStr);

        assertTrue(compressedStr.length() < str.length());

        assertTrue(unCompressStr.equals(str));
    }
}
