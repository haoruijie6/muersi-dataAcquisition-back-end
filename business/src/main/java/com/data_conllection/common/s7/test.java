package com.data_conllection.common.s7;

import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;

import java.math.BigInteger;

public class test {

//    public static void main(String[] args) {
//        //PLC地址
//        String ipAddress = "192.168.0.10";
//        //默认端口
//        int port = 102;
//        int rack=0;
//        int slot=0;
//        int timeout=10000;
//        S7Connector s7connector=
//                S7ConnectorFactory
//                        .buildTCPConnector()
//                        .withHost(ipAddress)
//                        .withPort(port) //optional
//                        .withRack(rack) //optional
//                        .withSlot(slot) //optional
//                        .withTimeout(timeout) //连接超时时间
//                        .build();
//        S7Serializer s7Serializer2L = S7SerializerFactory.buildSerializer(s7connector);
//        byte[] getBytes = s7connector.read(DaveArea.DB, 4, 1, 2);
//        Integer intData = new BigInteger(getBytes).intValue();
//        System.out.println("温度:"+intData);
//    }

    //最早方法
//    public static void main(String[] args) {
//        //read1("192.168.0.1");
//        read2("192.168.0.10");
//    }

    //方法一
    public static void main(String[] args) {
        SiemensS7Net siemensS7Net = new SiemensS7Net(SiemensPLCS.S200Smart, "127.0.0.1");
        siemensS7Net.setPort(103);
        OperateResultExOne<Float> integerOperateResultExOne = siemensS7Net.ReadFloat("V1.1");
        Float content = integerOperateResultExOne.Content;
        System.out.println(content);
    }

    private static void read1(String ip) {
        // 创建PLC对象
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, ip, 102, 0, 1);
        System.out.println(s7PLC.isEnableReconnect());

        short m1000 = s7PLC.readInt16("M1000");
        System.out.println("机筒1温度SP：" + m1000);
        short m1002 = s7PLC.readInt16("M1002");
        System.out.println("机筒2温度SP：" + m1002);
        short m1004 = s7PLC.readInt16("M1004");
        System.out.println("机筒3温度SP：" + m1004);
        short m1006 = s7PLC.readInt16("M1006");
        System.out.println("机筒4温度SP：" + m1006);
        short m1008 = s7PLC.readInt16("M1007");
        System.out.println("机筒5温度SP：" + m1008);
        short m1010 = s7PLC.readInt16("M1010");
        System.out.println("机筒6温度SP：" + m1010);
        short m1012 = s7PLC.readInt16("M1012");
        System.out.println("机筒7温度SP：" + m1012);
        short m1014 = s7PLC.readInt16("M1014");
        System.out.println("机筒8温度SP：" + m1014);
        short m1016 = s7PLC.readInt16("M1016");
        System.out.println("机筒9温度SP：" + m1016);
        short m1018 = s7PLC.readInt16("M1018");
        System.out.println("机筒10温度SP：" + m1018);

        short m1024 = s7PLC.readInt16("M1024");
        System.out.println("连接1温度SP：" + m1024);
        short m1032 = s7PLC.readInt16("M1032");
        System.out.println("连接2温度SP：" + m1032);
        short m1036 = s7PLC.readInt16("M1036");
        System.out.println("连接3温度SP：" + m1036);
        short m1038 = s7PLC.readInt16("M1038");
        System.out.println("连接4温度SP：" + m1038);
        short m1040 = s7PLC.readInt16("M1040");
        System.out.println("连接5温度SP：" + m1040);
        short m1042 = s7PLC.readInt16("M1042");
        System.out.println("连接6温度SP：" + m1042);
        short m1044 = s7PLC.readInt16("M1044");
        System.out.println("连接7温度SP：" + m1044);

        short m1020 = s7PLC.readInt16("M1020");
        System.out.println("水箱1温度SP：" + m1020);
        short m1022 = s7PLC.readInt16("M1022");
        System.out.println("水箱2温度SP：" + m1022);
        short m1026 = s7PLC.readInt16("M1026");
        System.out.println("换网1温度SP：" + m1026);
        short m1028 = s7PLC.readInt16("M1028");
        System.out.println("换网2温度SP：" + m1028);
        short m1030 = s7PLC.readInt16("M1030");
        System.out.println("换网3温度SP：" + m1030);

        Float m1404 = s7PLC.readFloat32("M1404");
        System.out.println("螺杆机电机电流：" + m1404);
        Float m1824 = s7PLC.readFloat32("M1824");
        System.out.println("螺杆机电机负载：" + m1824);
        Float m1400 = s7PLC.readFloat32("M1400");
        System.out.println("螺杆机电机转速：" + m1400);
        Float m1436 = s7PLC.readFloat32("M1436");
        System.out.println("计量泵电机电流：" + m1436);
        Float m1864 = s7PLC.readFloat32("M1864");
        System.out.println("计量泵电机负载：" + m1864);
        Float m1432 = s7PLC.readFloat32("M1432");
        System.out.println("计量泵电机转速：" + m1432);
        Float m1420 = s7PLC.readFloat32("M1420");
        System.out.println("网前压力：" + m1420);
        Float m1424 = s7PLC.readFloat32("M1424");
        System.out.println("计量泵泵前压力：" + m1424);
        Float m1428 = s7PLC.readFloat32("M1428");
        System.out.println("计量泵泵后压力：" + m1428);

        short m512 = s7PLC.readInt16("M512");
        System.out.println("机筒1温度PV：" + m512);
        short m532 = s7PLC.readInt16("M532");
        System.out.println("机筒2温度PV：" + m532);
        short m552 = s7PLC.readInt16("M552");
        System.out.println("机筒3温度PV：" + m552);
        short m572 = s7PLC.readInt16("M572");
        System.out.println("机筒4温度PV：" + m572);
        short m592 = s7PLC.readInt16("M592");
        System.out.println("机筒4温度PV：" + m592);
        short m612 = s7PLC.readInt16("M612");
        System.out.println("机筒5温度PV：" + m612);
        short m632 = s7PLC.readInt16("M632");
        System.out.println("机筒6温度PV：" + m632);
        short m652 = s7PLC.readInt16("M652");
        System.out.println("机筒7温度PV：" + m652);
        short m672 = s7PLC.readInt16("M672");
        System.out.println("机筒8温度PV：" + m672);
        short m692 = s7PLC.readInt16("M692");
        System.out.println("机筒9温度PV：" + m692);

        short m752 = s7PLC.readInt16("M752");
        System.out.println("连接1温度PV：" + m752);
        short m832 = s7PLC.readInt16("M832");
        System.out.println("连接2温度PV：" + m832);
        short m872 = s7PLC.readInt16("M872");
        System.out.println("连接3温度PV：" + m872);
        short m892 = s7PLC.readInt16("M892");
        System.out.println("连接4温度PV：" + m892);
        short m912 = s7PLC.readInt16("M912");
        System.out.println("连接5温度PV：" + m912);
        short m932 = s7PLC.readInt16("M932");
        System.out.println("连接6温度PV：" + m932);
        short m952 = s7PLC.readInt16("M952");
        System.out.println("连接7温度PV：" + m952);

        short m712 = s7PLC.readInt16("M712");
        System.out.println("水箱1PV：" + m712);
        short m732 = s7PLC.readInt16("M732");
        System.out.println("水箱2PV：" + m732);
        short m772 = s7PLC.readInt16("M772");
        System.out.println("换网1温度PV：" + m772);
        short m792 = s7PLC.readInt16("M792");
        System.out.println("换网2温度PV：" + m792);
        short m812 = s7PLC.readInt16("M812");
        System.out.println("换网3温度PV：" + m812);


        short m852 = s7PLC.readInt16("M852");
        System.out.println("换网3温度PV：" + m852);
        short m972 = s7PLC.readInt16("M972");
        System.out.println("换网3温度PV：" + m972);

        short m1996 = s7PLC.readInt16("M1996");
        System.out.println("A机料位状态：" + m1996);



        Float DB240 = s7PLC.readFloat32("DB7.240");
        System.out.println("收卷A辊速度SP：" + DB240);
        Float dB264 = s7PLC.readFloat32("DB7.264");
        System.out.println("速度MAX：" + dB264);
        Float db256 = s7PLC.readFloat32("DB7.256");
        System.out.println("输入比例：" + db256);
        Float db260 = s7PLC.readFloat32("DB7.260");
        System.out.println("输出比例：" + db260);
        Float db248 = s7PLC.readFloat32("DB7.248");
        System.out.println("速比：" + db248);
        Float db252 = s7PLC.readFloat32("DB7.252");
        System.out.println("辊径：" + db252);
    }

    private static void read2(String ip) {
        // 创建PLC对象
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, ip, 102, 0, 1);
        System.out.println(s7PLC.isEnableReconnect());

        short m1000 = s7PLC.readInt16("M1000");
        System.out.println("机筒1温度SP：" + m1000);
        short m1002 = s7PLC.readInt16("M1002");
        System.out.println("机筒2温度SP：" + m1002);
        short m1004 = s7PLC.readInt16("M1004");
        System.out.println("机筒3温度SP：" + m1004);
        short m1006 = s7PLC.readInt16("M1006");
        System.out.println("机筒4温度SP：" + m1006);
        short m1008 = s7PLC.readInt16("M1007");
        System.out.println("机筒5温度SP：" + m1008);
        short m1010 = s7PLC.readInt16("M1010");
        System.out.println("机筒6温度SP：" + m1010);
        short m1012 = s7PLC.readInt16("M1012");
        System.out.println("机筒7温度SP：" + m1012);
        short m1014 = s7PLC.readInt16("M1014");
        System.out.println("机筒8温度SP：" + m1014);
        short m1016 = s7PLC.readInt16("M1016");
        System.out.println("机筒9温度SP：" + m1016);
        short m1018 = s7PLC.readInt16("M1018");
        System.out.println("机筒10温度SP：" + m1018);

        short m1024 = s7PLC.readInt16("M1024");
        System.out.println("连接1温度SP：" + m1024);
        short m1032 = s7PLC.readInt16("M1032");
        System.out.println("连接2温度SP：" + m1032);
        short m1036 = s7PLC.readInt16("M1036");
        System.out.println("连接3温度SP：" + m1036);
        short m1038 = s7PLC.readInt16("M1038");
        System.out.println("连接4温度SP：" + m1038);
        short m1040 = s7PLC.readInt16("M1040");
        System.out.println("连接5温度SP：" + m1040);
        short m1042 = s7PLC.readInt16("M1042");
        System.out.println("连接6温度SP：" + m1042);
        short m1044 = s7PLC.readInt16("M1044");
        System.out.println("连接7温度SP：" + m1044);

        short m1020 = s7PLC.readInt16("M1020");
        System.out.println("水箱1温度SP：" + m1020);
        short m1022 = s7PLC.readInt16("M1022");
        System.out.println("水箱2温度SP：" + m1022);
        short m1026 = s7PLC.readInt16("M1026");
        System.out.println("换网1温度SP：" + m1026);
        short m1028 = s7PLC.readInt16("M1028");
        System.out.println("换网2温度SP：" + m1028);
        short m1030 = s7PLC.readInt16("M1030");
        System.out.println("换网3温度SP：" + m1030);

        Float m1404 = s7PLC.readFloat32("M1404");
        System.out.println("螺杆机电机电流：" + m1404);
        Float m1824 = s7PLC.readFloat32("M1824");
        System.out.println("螺杆机电机负载：" + m1824);
        Float m1400 = s7PLC.readFloat32("M1400");
        System.out.println("螺杆机电机转速：" + m1400);
        Float m1436 = s7PLC.readFloat32("M1436");
        System.out.println("计量泵电机电流：" + m1436);
        Float m1864 = s7PLC.readFloat32("M1864");
        System.out.println("计量泵电机负载：" + m1864);
        Float m1432 = s7PLC.readFloat32("M1432");
        System.out.println("计量泵电机转速：" + m1432);
        Float m1420 = s7PLC.readFloat32("M1420");
        System.out.println("网前压力：" + m1420);
        Float m1424 = s7PLC.readFloat32("M1424");
        System.out.println("计量泵泵前压力：" + m1424);
        Float m1428 = s7PLC.readFloat32("M1428");
        System.out.println("计量泵泵后压力：" + m1428);

        short m512 = s7PLC.readInt16("M512");
        System.out.println("机筒1温度PV：" + m512);
        short m532 = s7PLC.readInt16("M532");
        System.out.println("机筒2温度PV：" + m532);
        short m552 = s7PLC.readInt16("M552");
        System.out.println("机筒3温度PV：" + m552);
        short m572 = s7PLC.readInt16("M572");
        System.out.println("机筒4温度PV：" + m572);
        short m592 = s7PLC.readInt16("M592");
        System.out.println("机筒4温度PV：" + m592);
        short m612 = s7PLC.readInt16("M612");
        System.out.println("机筒5温度PV：" + m612);
        short m632 = s7PLC.readInt16("M632");
        System.out.println("机筒6温度PV：" + m632);
        short m652 = s7PLC.readInt16("M652");
        System.out.println("机筒7温度PV：" + m652);
        short m672 = s7PLC.readInt16("M672");
        System.out.println("机筒8温度PV：" + m672);
        short m692 = s7PLC.readInt16("M692");
        System.out.println("机筒9温度PV：" + m692);

        short m752 = s7PLC.readInt16("M752");
        System.out.println("连接1温度PV：" + m752);
        short m832 = s7PLC.readInt16("M832");
        System.out.println("连接2温度PV：" + m832);
        short m872 = s7PLC.readInt16("M872");
        System.out.println("连接3温度PV：" + m872);
        short m892 = s7PLC.readInt16("M892");
        System.out.println("连接4温度PV：" + m892);
        short m912 = s7PLC.readInt16("M912");
        System.out.println("连接5温度PV：" + m912);
        short m932 = s7PLC.readInt16("M932");
        System.out.println("连接6温度PV：" + m932);
        short m952 = s7PLC.readInt16("M952");
        System.out.println("连接7温度PV：" + m952);

        short m712 = s7PLC.readInt16("M712");
        System.out.println("水箱1PV：" + m712);
        short m732 = s7PLC.readInt16("M732");
        System.out.println("水箱2PV：" + m732);
        short m772 = s7PLC.readInt16("M772");
        System.out.println("换网1温度PV：" + m772);
        short m792 = s7PLC.readInt16("M792");
        System.out.println("换网2温度PV：" + m792);
        short m812 = s7PLC.readInt16("M812");
        System.out.println("换网3温度PV：" + m812);


        short m852 = s7PLC.readInt16("M852");
        System.out.println("换网3温度PV：" + m852);
        short m972 = s7PLC.readInt16("M972");
        System.out.println("换网3温度PV：" + m972);

        short m1996 = s7PLC.readInt16("M1996");
        System.out.println("A机料位状态：" + m1996);



        Float DB240 = s7PLC.readFloat32("DB7.240");
        System.out.println("收卷A辊速度SP：" + DB240);
        Float dB264 = s7PLC.readFloat32("DB7.264");
        System.out.println("速度MAX：" + dB264);
        Float db256 = s7PLC.readFloat32("DB7.256");
        System.out.println("输入比例：" + db256);
        Float db260 = s7PLC.readFloat32("DB7.260");
        System.out.println("输出比例：" + db260);
        Float db248 = s7PLC.readFloat32("DB7.248");
        System.out.println("速比：" + db248);
        Float db252 = s7PLC.readFloat32("DB7.252");
        System.out.println("辊径：" + db252);
    }
}
