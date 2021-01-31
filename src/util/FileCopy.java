package util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class FileCopy {
    Logger logger = Logger.getLogger(this.getClass().getName());
    public void CopyStart() {
        Map<String, String> backupInfo;
        backupInfo = readConfig();
        logger.info("backupFrom = " + backupInfo.get("backupFrom"));
        logger.info("backupTo = " + backupInfo.get("backupTo"));
        File from = new File(backupInfo.get("backupFrom"));
        File to = new File(backupInfo.get("backupTo"));
        logger.info(String.format("[시작] %s ==> %s 파일 백업 시작", backupInfo.get("backupFrom"), backupInfo.get("backupTo")));
        fileCopy(from, to);

    }

    public void fileCopy(File from, File to){


        File[] ff = from.listFiles();
        for (File file : ff) {
            File temp = new File(to.getAbsolutePath() + File.separator + file.getName());
            if(file.isDirectory()){
                temp.mkdir();
                fileCopy(file, temp);
            } else {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp) ;
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while((cnt=fis.read(b)) != -1){
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally{
                    try {
                        fis.close();
                        fos.close();
                        logger.info(String.format("[종료] 파일 백업 성공"));
                    } catch (IOException e) {
                        logger.info(String.format("[종료] 파일 백업 에러"));
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Map<String, String> readConfig() {
        Map<String, String> backupInfo = new HashMap<>();

        logger.info("[시작] properties 파읽 읽기");
        String resources = "config/config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resources);
        Properties properties = new Properties();

        try{
            properties.load(inputStream);
            String backupFrom = properties.getProperty("backupFrom");
            String backupTo = properties.getProperty("backupTo");
            backupInfo.put("backupFrom", backupFrom);
            backupInfo.put("backupTo", backupTo);
            logger.info("[종료] properties 파읽 읽기에 성공함.");
        }catch (Exception e){
            logger.warning("[종료] properties 파읽 읽기에 실패함..");
            e.printStackTrace();
        }
        return backupInfo;
    }

}

