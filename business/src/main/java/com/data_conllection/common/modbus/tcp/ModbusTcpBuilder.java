package com.data_conllection.common.modbus.tcp;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ModbusTcpBuilder {

    /**
     * 获取modbus tcp连接通道
     * @param ip ip地址
     * @param port 端口地址
     */
    public static ModbusMaster getModbusTcp(String ip, Integer port){
        // 设置主机TnCP参数
        TcpParameters tcpParameters = new TcpParameters();

        // 设置TCP的ip地址
        InetAddress adress = null;
        try {
            adress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException("请检查配置的ip格式是否正确!");
        }
        // TCP参数设置ip地址
        tcpParameters.setHost(adress);

        // TCP设置长连接
        tcpParameters.setKeepAlive(true);

        // TCP设置端口
        tcpParameters.setPort(port);

        // 创建一个设备主机
        ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        //设置通道为长连接
        Modbus.setAutoIncrementTransactionId(true);
        return master;
    }

    public static void main(String[] args) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusMaster modbusTcp = getModbusTcp("192.168.100.49", 502);
        System.out.println(modbusTcp.isConnected());
//        int[] ints = modbusTcp.readHoldingRegisters(1, 0, 1);
//        for (int s : ints) {
//            System.out.println(s);
//        }
    }

}
