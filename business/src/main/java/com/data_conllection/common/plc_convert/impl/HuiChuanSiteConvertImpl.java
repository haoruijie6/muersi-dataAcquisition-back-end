package com.data_conllection.common.plc_convert.impl;

import com.data_conllection.common.enums.HuiChuanPlcTypeEnum;
import com.data_conllection.common.plc_convert.HuiChuanSiteConvert;

import javax.validation.constraints.NotNull;

/**
 * 汇川下详细型号地址规则转换
 */
public class HuiChuanSiteConvertImpl implements HuiChuanSiteConvert {

    @Override
    public String convert(String registerAddress) {
        return convert(HuiChuanPlcTypeEnum.AM_600,registerAddress);
    }

    /**
     * @param plcType plc型号
     * @return 转换后 modbus 地址
     */
    @Override
    public String convert(@NotNull HuiChuanPlcTypeEnum plcType, String registerAddress) {
        Integer modbusAddress = null;
        switch (plcType){
            //某某品牌
            case AM_600 :
                //获取汇川plc数据块
                String prefixStr = registerAddress.substring(0, 2);
                //转换地址地址
                modbusAddress = Integer.parseInt(registerAddress.substring(2));
                break;
            case AM_800:
                //获取汇川plc数据块
                String prefixStr1 = registerAddress.substring(0, 2);
                //转换地址地址
                modbusAddress = Integer.parseInt(registerAddress.substring(2));
                break;
        }
        return modbusAddress.toString();
    }
}
