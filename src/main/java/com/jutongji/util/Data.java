package com.jutongji.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data<T> implements Serializable {

  private static final String SUCCESS = "0";
  private static final String WARN = "1";
  private static final String FAILURE = "2";
  private static final String PLATFORM_CODE = "training-web";

  @JsonIgnore
  private Boolean ok;
  @ApiModelProperty("状态 0-成功；1-错误提示；2-失败")
  private String code;
  @ApiModelProperty("返回的数据")
  private T data;
  @ApiModelProperty("提示信息")
  private String message;

  @ApiModelProperty("平台标识")
  private String platform = PLATFORM_CODE;
  
  /**
 * @return the platform
 */
public String getPlatform() {
	return platform;
}

/**
 * @param platform the platform to set
 */
public void setPlatform(String platform) {
	this.platform = platform;
}

private Data(T data, Boolean ok, String code, String message) {
    this.ok = ok;
    this.code = code;
    this.data = data;
    this.message = message;
   }
  
  public static <T> Data<T> success(T data) {
    return new Data<>(data, true, SUCCESS, null);
  }

  public static Data<Boolean> success() {
    return new Data<>(true, true, SUCCESS, null);
  }

  public static <L> Data<Page<L>> pageData(Long count, List<L> content) {
    return success(new Page<>(count, content));
  }

  public static <T> Data<T> failure(String message) {
    return new Data<>(null, false, FAILURE, message);
  }

  public static <T> Data<T> warn(String message) {
    return new Data<>(null, false, WARN, message);
  }
  
  public static <T> Data<T> warn(String message, T data) {
    return new Data<>(data, false, WARN, message);
  }

  public Data<T> fail(String message) {
    this.setOk(false);
    this.setCode(FAILURE);
    this.setMessage(message);
    return this;
  }

  public String getCode() {
    return code;
  }

  private void setCode(String code) {
    this.code = code;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Boolean getOk() {
    return ok;
  }

  private void setOk(Boolean ok) {
    this.ok = ok;
  }

  public String getMessage() {
    return message;
  }

  public Data<T> setMessage(String message) {
    this.message = message;
    return this;
  }

  public static class Page<L> implements Serializable {

    private long totalElements;
    private List<L> elements;

    public Page(long totalElements, List<L> elements) {
      this.totalElements = totalElements;
      this.elements = elements;
    }

    public long getTotalElements() {
      return totalElements;
    }

    public void setTotalElements(long totalElements) {
      this.totalElements = totalElements;
    }

    public List<L> getElements() {
      return elements;
    }

    public void setElements(List<L> elements) {
      this.elements = elements;
    }
  }
}
