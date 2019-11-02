package com.hgz.v17center.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huangguizhao
 * 针对富文本框返回的数据做了封装
 */
@Data
@AllArgsConstructor
public class WangEditorResultBean {
    private String errno;
    private String[] data;
}
