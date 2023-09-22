package com.data_conllection.common.modbus;

import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.protocol.modbus.service.ModbusTcp;
import com.github.xingshuangs.iot.utils.HexUtil;

public class Test {

    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");
        // 报文输出设置
//        plc.setComCallback(x -> System.out.printf("长度[%d]:%s%n", x.length, HexUtil.toHexString(x)));
//        plc.writeUInt32(2, (long) 10);
//        long readUInt16 = plc.readUInt32(2);
        byte[] readInputRegister = plc.readInputRegister(3, 1);
        long uInt32 = ByteReadBuff.newInstance(readInputRegister, EByteBuffFormat.BA_DC).getUInt16();
        System.err.println(uInt32);
        plc.close();
    }

}