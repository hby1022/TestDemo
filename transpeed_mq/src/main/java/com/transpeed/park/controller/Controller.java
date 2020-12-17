package com.transpeed.park.controller;

import com.transpeed.park.config.ServerConfig;
import com.transpeed.park.entity.FormData;
import com.transpeed.park.entity.ParkingInfo;
import com.transpeed.park.service.ParkingInfoService;
import com.transpeed.park.util.JsonUtils;
import com.transpeed.park.util.StringUtil;
import com.transpeed.park.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
@RestController
@RequestMapping("/park")
public class Controller {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    ParkingInfoService parkingInfoService;

    @Value("${spring.resources.static-locations}")
    private String  path;
    @Value("${server.servlet.context-path}")
    private String serverName;
    @Autowired
    private ServerConfig serverConfig;

    @RequestMapping(value = "SyncTime",method = RequestMethod.POST)
    public void SyncTime() {
        System.out.println(path.substring(path.lastIndexOf(":")+1));
        String serverPath = serverConfig.getUrl() + serverName;
        String httppath=serverPath+ "/" +  "image" +  "/" +"ShowNoImage.jpg";
        log.info(httppath);

    }


    @RequestMapping(value = "identify",method = RequestMethod.POST)
    public String parkingspace(FormData formData, MultipartFile file)  {
        log.info("原始数据打印："+formData);
        log.info("原始图片信息："+file.getOriginalFilename());


        String result="";
        if(StringUtil.testIsNull(formData.getGuid(),formData.getDevice_id())){
            log.info("交易ID或点位ID为空");
            result="code=-1&msg=交易ID或点位ID为空";
            log.info(result);
            return result;
        }

        try{

            String plate=formData.getPlate();
            String flag="Y";
            if(!StringUtil.testIsNull(plate)){
                if (plate.equals("整牌拒识")) {
                    flag="N";
                }else{
                    if(plate.length()>7){
                        plate=plate.substring(0,7);
                    }
                }
            }else {
                flag="N";
            }

            ParkingInfo space=  parkingInfoService.selectByMacNoAndPlate(formData.getDevice_id(),plate);
            if(space!=null){
                String db_plate=space.getPlate();
                String db_carStatus=space.getCarexist();
                if(db_plate.equals(plate)&&flag.endsWith(db_carStatus)){
                    log.info("该车牌"+plate+"与车位"+flag+"一致,忽略。。。");
                    space.setLastrefreshtime(new Date());
                    parkingInfoService.updateByPrimaryKeySelective(space);

                    result="code=0&msg=该车牌"+plate+"与车位状态"+flag+"一致,忽略。。。";
                    log.info(result);
                    return result;
                }else{
                    log.info("更新信息表");

                    String picname=formData.getFileName();
                    String behind=picname.substring(picname.lastIndexOf("_")+1);
                    String front=picname.substring(0,picname.lastIndexOf("_"));
                    String time=behind.substring(0,8);

                    String path=uploadPic(file,picname);

                    String serverPath = serverConfig.getUrl() + serverName;
//                    String httppath=serverPath+ "/" +  "image" +  "/" +picname;
                    String httppath=serverPath+ "/" +  "image" +  "/" +time+"/"+front+"/"+picname;
                    log.info("图片位置："+path+",网络位置："+httppath);
                    space.setCarexist(flag);
                    space.setPlate(plate);
                    space.setLastinfotime(sdf.parse(formData.getReg_time()));
                    space.setLastrefreshtime(new Date());
                    space.setImage(httppath);
                    log.info("更新的数据是："+ JsonUtils.formatAsJSON(space));
                    parkingInfoService.updateByPrimaryKeySelective(space);

                    result="code=0&msg=success";
                    log.info(result);
                    return result;
                }
            }else{

                result="code=-1&msg=无对应的点位信息";
                log.info(result);
                return result;
            }

        }catch (Exception e){
            log.error("异常："+e.getMessage());
            result="code=-1&msg=异常："+e.getMessage();
            log.info(result);
            return result;
        }

    }

    private String  uploadPic(MultipartFile file,String name) {

        String io_fileName=name;

        String path_prefix =path.substring(path.indexOf(":")+1);
        System.out.println("---------"+path_prefix);

        String behind=io_fileName.substring(io_fileName.lastIndexOf("_")+1);
        String front=io_fileName.substring(0,io_fileName.lastIndexOf("_"));
        String time=behind.substring(0,8);


        String path=path_prefix+time+"/"+front;
        log.info("图片路径=" + path +" , "+ " 图片名称=" + io_fileName);

        if(file.isEmpty()){
            log.error("上传文件为空");
            io_fileName="";
        }

        try {
            Utils.createDir(path);
            OutputStream outputStream = new FileOutputStream(new File(path,io_fileName));
            int length = 0;
            InputStream in =file.getInputStream();
            byte[] buff = new byte[1024];
            while (-1 != (length = in.read(buff))) {
                outputStream.write(buff, 0, length);
            }
            in.close();
            outputStream.close();
        }  catch (IOException e) {
            log.error("图片上传异常:"+e.getMessage());
            io_fileName="";
        }
        return io_fileName;
    }
}
