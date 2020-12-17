package com.transpeed.park.entity;

import lombok.Data;

import java.util.Date;
@Data
public class ParkingInfo {
    private Long sn;

    private String area;

    private String parkingno;

    private String lpnrtype;

    private String ipcposition;

    private String macno;

    private String macip;

    private String carexist;

    private String platecolor;

    private String plate;

    private Date lastinfotime;

    private Date lastrefreshtime;

    private String image;

    private String remark;

}
