package com.transpeed.park.mapper;

import com.transpeed.park.entity.ParkingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Mapper
public interface ParkingInfoMapper {

    int updateByPrimaryKeySelective(ParkingInfo record);


    ParkingInfo selectByMacNoAndPlate(@Param("device_id") String device_id, @Param("plate")String plate);
}
