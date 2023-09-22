package com.data_conllection.common.plc_convert;

import com.data_conllection.common.enums.HuiChuanPlcTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 汇川品牌plc寄存器地址与modbus地址转换规则
 */
public interface HuiChuanSiteConvert {

    /**
     * 会默认plc型号为AM_600
     * @param registerAddress plc寄存器地址
     * @return 转换后 modbus 地址
     */
    public String convert(String registerAddress);

    /**
     * @param plcType plc型号
     * @param registerAddress plc寄存器地址
     * @return 转换后 modbus 地址
     */
    public String convert(@NotNull HuiChuanPlcTypeEnum plcType, String registerAddress);


}
