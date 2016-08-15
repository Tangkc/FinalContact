package com.jc.android.contact.data.entity;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Dept {
    private long id; 			/*部门编号*/
    private String name; 			/*部门名称*/
    private long parentId; 			/*上级节点Id*/
    private int deptType; 		/*标记是部门or机构节点  ’0‘--部门  ’1‘--机构*/
    private int queue; 			/*部门排序*/

    private List<Contact> userList = new ArrayList<>();/*部门下的人员	自定义属性*/

    private long orgId;				/*机构ID	自定义属性*/
    private String orgName;			/*机构名称	自定义属性*/
}
