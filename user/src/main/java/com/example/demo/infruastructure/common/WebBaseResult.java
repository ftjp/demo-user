package com.example.demo.infruastructure.common;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.github.pagehelper.PageInfo;


public class WebBaseResult<T> extends BaseResult<T> {
    private PageInfo<T> pageInfo;

    public WebBaseResult() {
    }

    public void setSuccess(PageInfo<T> pageInfo) {
        super.setCodeAndMsg(BaseResultEnum.SUCCESS);
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "WebBaseResult(super=" + super.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof WebBaseResult)) {
            return false;
        } else {
            WebBaseResult<?> other = (WebBaseResult) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$pageInfo = this.getPageInfo();
                Object other$pageInfo = other.getPageInfo();
                if (this$pageInfo == null) {
                    if (other$pageInfo != null) {
                        return false;
                    }
                } else if (!this$pageInfo.equals(other$pageInfo)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof WebBaseResult;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        Object $pageInfo = this.getPageInfo();
        result = result * 59 + ($pageInfo == null ? 43 : $pageInfo.hashCode());
        return result;
    }

    public PageInfo<T> getPageInfo() {
        return this.pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
