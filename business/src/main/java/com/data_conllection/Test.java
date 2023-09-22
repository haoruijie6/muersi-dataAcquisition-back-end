package com.data_conllection;

import com.data_conllection.common.plc_convert.HuiChuanSiteConvert;
import com.data_conllection.common.plc_convert.impl.HuiChuanSiteConvertImpl;

public class Test {
    public static void main(String[] args) {
        HuiChuanSiteConvert huiChuanSiteConvert = new HuiChuanSiteConvertImpl();
        System.out.println(huiChuanSiteConvert.convert("IW00002"));
        System.out.println(huiChuanSiteConvert.convert("QW00004"));
        System.out.println(huiChuanSiteConvert.convert("MW10342"));
    }
}
