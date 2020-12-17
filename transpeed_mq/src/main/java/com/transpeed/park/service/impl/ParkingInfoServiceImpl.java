package com.transpeed.park.service.impl;

import com.transpeed.park.entity.ParkingInfo;
import com.transpeed.park.mapper.ParkingInfoMapper;
import com.transpeed.park.service.ParkingInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Slf4j
@Service
public class ParkingInfoServiceImpl implements ParkingInfoService {

    @Resource
    ParkingInfoMapper parkingInfoMapper;

    @Override
    public ParkingInfo selectByMacNoAndPlate(String device_id, String plate) {
        return parkingInfoMapper.selectByMacNoAndPlate(device_id,plate);
    }

    @Override
    public int updateByPrimaryKeySelective(ParkingInfo space) {
        return parkingInfoMapper.updateByPrimaryKeySelective(space);
    }
}
