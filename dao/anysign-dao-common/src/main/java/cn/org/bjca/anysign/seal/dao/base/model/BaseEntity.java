package cn.org.bjca.anysign.seal.dao.base.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/***************************************************************************
 * <pre>公共字段</pre>
 *
 * @文件名称: BaseEntity.class
 * @包 路   径：  cn.org.bjca.anysign.esal.dao.base.model
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/8 20:58
 ***************************************************************************/
public class BaseEntity extends Model<BaseEntity> implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value="id", type= IdType.UUID)
    private String id;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最总修改时间
     */
    private Date lastUpdateTime;
    /**
     * 逻辑删除状态
     */
    private Boolean inv;

    /**
     * 扩展字段1
     */
    private String ext1;
    /**
     * 扩展字段2
     */
    private String ext2;
    /**
     * 扩展字段3
     */
    private String ext3;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getInv() {
        return inv;
    }

    public void setInv(Boolean inv) {
        this.inv = inv;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}