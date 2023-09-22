package com.data_conllection.common.modbus.tcp.service;

import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import org.springframework.stereotype.Service;

/**
 * modbustcp业务处理
 */
@Service
public interface ModbusTcpService {
    /**
     * 按寄存器类型读取数据
     */
    int[] read(EquipmentCollectionGroupPointPo p, ModbusMaster modbusTcpCache) throws ModbusProtocolException, ModbusNumberException, ModbusIOException;

    /**
     * 将32位/64位按规则进行转换
     */
    public String resultOfHandling(EquipmentCollectionGroupPointPo pointPo, int[] res);

}
