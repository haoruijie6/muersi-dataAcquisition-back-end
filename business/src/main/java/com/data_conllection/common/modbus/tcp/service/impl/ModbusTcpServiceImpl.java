package com.data_conllection.common.modbus.tcp.service.impl;

import com.data_conllection.common.modbus.tcp.service.ModbusTcpService;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import org.springframework.stereotype.Service;

@Service
public class ModbusTcpServiceImpl implements ModbusTcpService {


    @Override
    public int[] read(EquipmentCollectionGroupPointPo p, ModbusMaster modbusTcpCache) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        switch (p.getRegisterType()){
            case 0: //读coils 0x
                return modbusTcpCache.readCoils(p.getSlaveId(), p.getAddress(), p.getOffset())[0] ? new int[]{1} : new int[]{0};
            case 1: //1x 读coils
                return modbusTcpCache.readCoils(p.getSlaveId(), p.getAddress(), p.getOffset())[0] ? new int[]{1} : new int[]{0};
            case 2: //3x 保持寄存器
                return modbusTcpCache.readHoldingRegisters(p.getSlaveId(), p.getAddress(), p.getOffset());
            case 3: //4x 只读寄存器
                return modbusTcpCache.readInputRegisters(p.getSlaveId(), p.getAddress(), p.getOffset());
        }
        return new int[0];
    }

    /**
     * 处理数据转换格式
     * @param pointPo 采集数据与采集点对象
     */
    @Override
    public String resultOfHandling(EquipmentCollectionGroupPointPo pointPo, int[] registerValues) {
        if (registerValues.length <= 1){
            return String.valueOf(registerValues[0]);
        }
        switch (pointPo.getDataType()){
            case 0: //Signed
                return String.valueOf(registerValues[0]);
            case 1: //long AB CD √
                return String.valueOf(((long) registerValues[0] << 32) | (registerValues[1] & 0xFFFFL));
            case 2: //long CD AB √
                return String.valueOf(((long) registerValues[1] << 32) | (registerValues[0] & 0xFFFFL));
            case 3: //long BA DC √
                return String.valueOf(((registerValues[0] & 0xff) << 24) | ((registerValues[0] & 0xff00) << 8) | ((registerValues[1] & 0xff) << 8) | ((registerValues[1] & 0xff00) >>> 8));
            case 4: //long DC BA √
                return String.valueOf(((registerValues[1] & 0xff) << 24) | ((registerValues[1] & 0xff00) << 8) | ((registerValues[0] & 0xff) << 8) | ((registerValues[0] & 0xff00) >>> 8));
            case 5: //float AB CD √
                return String.valueOf(Float.intBitsToFloat((registerValues[0] << 16) | (registerValues[1] & 0xffff)));
            case 6: //float CD AB √
                return String.valueOf(Float.intBitsToFloat((registerValues[1] << 16) | (registerValues[0] & 0xffff)));
            case 7: //float DC BA √
                return String.valueOf(Float.intBitsToFloat(((registerValues[0] & 0xff) << 24) | ((registerValues[0] & 0xff00) << 8) | ((registerValues[1] & 0xff) << 8) | ((registerValues[1] & 0xff00) >>> 8)));
            case 8: //float DC BA √
                return String.valueOf(Float.intBitsToFloat(((registerValues[1] & 0xff) << 24) | ((registerValues[1] & 0xff00) << 8) | ((registerValues[0] & 0xff) << 8) | (registerValues[0] & 0xff00)));
            case 9: //double AB CD EF GH √
                if (registerValues.length <= 2)
                    return String.valueOf(registerValues[0]);
                return String.valueOf(Double.longBitsToDouble(((long) registerValues[0] << 48) | ((long) registerValues[1] << 32) | ((long) registerValues[2] << 16) | registerValues[3]));
            case 10: //double GH EF CD AB √
                if (registerValues.length <= 2)
                    return String.valueOf(registerValues[0]);
                return String.valueOf(Double.longBitsToDouble(((long) registerValues[3] << 48) | ((long) registerValues[2] << 32) | ((long) registerValues[1] << 16) | registerValues[0]));
            case 11: //double BA DC FE HG √
                if (registerValues.length <= 2)
                    return String.valueOf(registerValues[0]);
                return String.valueOf(Double.longBitsToDouble(((long) registerValues[3] << 48) | ((long) registerValues[2] << 32) | ((long) registerValues[1] << 16) | ((long) registerValues[0])));
            case 12: //double HG FE DC BA √
                if (registerValues.length <= 2)
                    return String.valueOf(registerValues[0]);
                return String.valueOf(Double.longBitsToDouble(((long) registerValues[0] << 48) | ((long) registerValues[1] << 32) | ((long) registerValues[2] << 16) | ((long) registerValues[3])));
        }
        return null;
    }
}
