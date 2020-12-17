package com.transpeed.park.service;

import com.transpeed.park.entity.ParkingInfo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public interface ParkingInfoService {

    ParkingInfo selectByMacNoAndPlate(String device_id, String plate);

    int updateByPrimaryKeySelective(ParkingInfo space);
}
